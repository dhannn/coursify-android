package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase
import android.util.Log
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service.ChatGPTService
import com.google.firebase.firestore.ktx.toObject
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.LearningPlan
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.LearningPlanRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.*
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service.ClaudeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

class LearningPlanRepository(
    private val claudeService: ClaudeService,
    private val chatGPTService: ChatGPTService,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val generationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val plansCollection = firestore.collection("learning_plans")

    suspend fun generateAndSavePlan(request: LearningPlanRequest): FirebaseResult<String> {
        return withContext(generationScope.coroutineContext) {
            try {
                println("Starting plan generation in repository: ${request.id}")

                // Generate course using ChatGPT
                val generatedCourse = when (val result = claudeService.generateCourse(request)) {
                    is FirebaseResult.Success -> result.data
                    is FirebaseResult.Error -> return@withContext FirebaseResult.Error(result.exception)
                    else -> throw Exception("Unexpected state in course generation")
                }

                val learningPlan = LearningPlan(
                    planId = request.id,
                    title = generatedCourse.courseTitle,
                    status = "active",
                    learningGoal = request.learningGoal,
                    weeklyCommitment = request.weeklyCommitment,
                    courseDuration = request.courseDuration,
                    learningAbility = request.learningAbility,
                    targetAudience = request.targetAudience,
                    otherComments = request.otherComments,
                    weeks = generatedCourse.weeks.map { week ->
                        Week(
                            weekNumber = week.weekNumber,
                            title = "Week ${week.weekNumber}",
                            mainTopic = week.mainTopic,
                            subtopics = week.subtopics,
                            tasks = week.tasks.map { taskDescription ->
                                Task(
                                    description = taskDescription,
                                    isCompleted = false,
                                    completedAt = null
                                )
                            }
                        )
                    }
                )

                val updates = hashMapOf(
                    "title" to learningPlan.title,
                    "status" to learningPlan.status,
                    "weeks" to learningPlan.weeks,
                    "lastModified" to Timestamp.now()
                )

                plansCollection.document(request.id).update(updates).await()

                return@withContext FirebaseResult.Success(request.id)

            } catch (e: Exception) {
                println("Exception in generateAndSavePlan: ${e.javaClass.simpleName} - ${e.message}")
                e.printStackTrace()
                FirebaseResult.Error(e)
            }
        }
    }
    suspend fun getPlan(planId: String): FirebaseResult<LearningPlan> {
        Log.d("LearningPlanRepository::getPlan", planId)
        return try {
            val doc = plansCollection.document(planId).get().await()
            doc.toObject<LearningPlan>()?.let {
                FirebaseResult.Success(it)
            } ?: FirebaseResult.Error(Exception("Plan not found"))
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    fun getUserPlans(): Flow<FirebaseResult<List<LearningPlan>>> = flow {
        try {
            val userId = FirebaseManager.currentUserId ?: throw Exception("User not logged in")
            val snapshot = plansCollection  // Use plansCollection instead of planCollection
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()

            val plans = snapshot.documents.mapNotNull { document ->
                document.toObject<LearningPlan>()
            }

            Log.d("getUserPlans", "Plan ID: ${plans.get(0).planId}")
            emit(FirebaseResult.Success(plans))
        } catch (e: Exception) {
            emit(FirebaseResult.Error(e))
        }
    }

    suspend fun toggleBookmark(planId: String): FirebaseResult<Unit> {
        return try {
            val docRef = plansCollection.document(planId)
            val plan = docRef.get().await().toObject<LearningPlan>()
                ?: return FirebaseResult.Error(Exception("Plan not found"))

            docRef.update(
                mapOf(
                    "bookmarked" to !plan.bookmarked,
                    "lastModified" to Timestamp.now()
                )
            ).await()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun updateTaskCompletion(
        planId: String,
        weekNumber: Int,
        task: Task,
        isCompleted: Boolean
    ): FirebaseResult<Unit> {
        return try {
            val plan = getPlan(planId)
            if (plan !is FirebaseResult.Success) {
                return FirebaseResult.Error(Exception("Failed to update task"))
            }

            val updatedWeeks = plan.data.weeks.map { week ->
                if (week.weekNumber == weekNumber) {
                    week.copy(
                        tasks = week.tasks.map { t ->
                            if (t.description == task.description) {
                                t.copy(
                                    isCompleted = isCompleted,
                                    completedAt = if (isCompleted) Timestamp.now() else null
                                )
                            } else t
                        }
                    )
                } else week
            }

            plansCollection.document(planId).update(
                mapOf(
                    "weeks" to updatedWeeks,
                    "lastModified" to Timestamp.now()
                )
            ).await()

            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun updatePlanStatus(planId: String, status: String): FirebaseResult<Unit> {
        return try {
            plansCollection.document(planId).update(
                mapOf(
                    "status" to status,
                    "lastModified" to Timestamp.now()
                )
            ).await()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun saveCourseContent(
        planId: String,
        weeks: List<Week>
    ): FirebaseResult<Unit> {
        return try {
            plansCollection.document(planId).update(
                mapOf(
                    "weeks" to weeks,
                    "lastModified" to Timestamp.now(),
                    "status" to "active"
                )
            ).await()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun deletePlan(planId: String): FirebaseResult<Unit> {
        return try {
            plansCollection.document(planId).delete().await()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getBookmarkedPlans(): Flow<FirebaseResult<List<LearningPlan>>> = flow {
        try {
            val userId = FirebaseManager.currentUserId ?: throw Exception("User not logged in")
            val snapshot = plansCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("bookmarked", true)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()

            val plans = snapshot.documents.mapNotNull { it.toObject<LearningPlan>() }
            emit(FirebaseResult.Success(plans))
        } catch (e: Exception) {
            emit(FirebaseResult.Error(e))
        }
    }

    suspend fun saveGeneratedCourse(planId: String, generatedCourse: GeneratedCourse): FirebaseResult<Unit> {
        return try {
            val weeks = generatedCourse.weeks.map { week ->
                Week(
                    weekNumber = week.weekNumber,
                    title = "Week ${week.weekNumber}",
                    mainTopic = week.mainTopic,
                    subtopics = week.subtopics,
                    tasks = week.tasks.map { taskDescription ->
                        Task(
                            description = taskDescription,
                            isCompleted = false,
                            completedAt = null
                        )
                    }
                )
            }

            val updates = hashMapOf(
                "title" to generatedCourse.courseTitle,
                "description" to generatedCourse.courseDescription,
                "weeks" to weeks,
                "status" to "active",
                "lastModified" to Timestamp.now()
            )

            plansCollection.document(planId).update(updates).await()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }
    suspend fun createPlan(plan: LearningPlan): FirebaseResult<String> {
        return try {
            val userId = FirebaseManager.currentUserId ?:
            return FirebaseResult.Error(Exception("User not logged in"))

            val docRef = plansCollection.document()
            val planToSave = plan.copy(
                planId = docRef.id,
                userId = userId,
                createdAt = Timestamp.now(),
                lastModified = Timestamp.now()
            )

            docRef.set(planToSave).await()
            FirebaseResult.Success(docRef.id)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }
}