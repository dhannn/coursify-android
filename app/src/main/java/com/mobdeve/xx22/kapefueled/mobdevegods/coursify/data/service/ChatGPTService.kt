package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.assistant.AssistantRequest
import com.aallam.openai.api.assistant.AssistantResponseFormat
import com.aallam.openai.api.core.Role
import com.aallam.openai.api.core.Status
import com.aallam.openai.api.message.MessageContent
import com.aallam.openai.api.message.MessageRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.api.run.RunRequest
import com.aallam.openai.client.OpenAI
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseResult
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.GeneratedCourse
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.LearningPlanRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import java.util.concurrent.TimeoutException

class ChatGPTService(apiKey: String) {
    private val client = OpenAI(token = apiKey)
    private val apiScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val courseSchema = JsonObject(
        mapOf(
            "type" to JsonPrimitive("object"),
            "additionalProperties" to JsonPrimitive(false),  // Add this
            "properties" to JsonObject(
                mapOf(
                    "course_title" to JsonObject(
                        mapOf(
                            "type" to JsonPrimitive("string"),
                            "description" to JsonPrimitive("A concise, engaging title for the course")
                        )
                    ),
                    "course_description" to JsonObject(
                        mapOf(
                            "type" to JsonPrimitive("string"),
                            "description" to JsonPrimitive("A brief description of what the course covers")
                        )
                    ),
                    "weeks" to JsonObject(
                        mapOf(
                            "type" to JsonPrimitive("array"),
                            "items" to JsonObject(
                                mapOf(
                                    "type" to JsonPrimitive("object"),
                                    "additionalProperties" to JsonPrimitive(false),  // Add this
                                    "properties" to JsonObject(
                                        mapOf(
                                            "week_number" to JsonObject(
                                                mapOf(
                                                    "type" to JsonPrimitive("integer"),
                                                    "description" to JsonPrimitive("The week number, starting from 1")
                                                )
                                            ),
                                            "main_topic" to JsonObject(
                                                mapOf(
                                                    "type" to JsonPrimitive("string"),
                                                    "description" to JsonPrimitive("The main topic for this week")
                                                )
                                            ),
                                            "subtopics" to JsonObject(
                                                mapOf(
                                                    "type" to JsonPrimitive("array"),
                                                    "items" to JsonObject(
                                                        mapOf(
                                                            "type" to JsonPrimitive("string")
                                                        )
                                                    ),
                                                    "description" to JsonPrimitive("List of subtopics to cover")
                                                )
                                            ),
                                            "tasks" to JsonObject(
                                                mapOf(
                                                    "type" to JsonPrimitive("array"),
                                                    "items" to JsonObject(
                                                        mapOf(
                                                            "type" to JsonPrimitive("string")
                                                        )
                                                    ),
                                                    "description" to JsonPrimitive("List of practical tasks or exercises")
                                                )
                                            )
                                        )
                                    ),
                                    "required" to JsonArray(
                                        listOf(
                                            JsonPrimitive("week_number"),
                                            JsonPrimitive("main_topic"),
                                            JsonPrimitive("subtopics"),
                                            JsonPrimitive("tasks")
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            "required" to JsonArray(
                listOf(
                    JsonPrimitive("course_title"),
                    JsonPrimitive("course_description"),
                    JsonPrimitive("weeks")
                )
            )
        )
    )

    @OptIn(BetaOpenAI::class)
    suspend fun generateCourse(request: LearningPlanRequest): FirebaseResult<GeneratedCourse> {
        return withContext(apiScope.coroutineContext) {
            try {
                println("Starting course generation for plan: ${request.id}")
                val assistant = client.assistant(
                    request = AssistantRequest(
                        name = "Course Creation Assistant",
                        instructions = """
                        You are an expert course designer. Create a structured learning plan with the following parameters:
                        - Weekly commitment: ${request.weeklyCommitment} hours
                        - Course duration: ${request.courseDuration} weeks
                        - Learning goal: ${request.learningGoal}
                        ${if (request.learningAbility.isNotBlank()) "- Desired learning outcome: ${request.learningAbility}" else ""}
                        ${if (request.targetAudience.isNotBlank()) "- Target audience: ${request.targetAudience}" else ""}
                        ${if (request.otherComments.isNotBlank()) "- Additional requirements: ${request.otherComments}" else ""}
                        Create a detailed course structure with:
                        1. A clear progression of topics
                        2. 2-4 practical tasks per week
                        3. Relevant subtopics for each main topic
                        4. Content appropriate for the specified time commitment
                    """.trimIndent(),
                        tools = emptyList(),
                        model = ModelId("gpt-4o-2024-08-06"),
                        metadata = emptyMap(),
                        responseFormat = AssistantResponseFormat(
                            type = "json_schema",
                            jsonSchema = AssistantResponseFormat.JsonSchema(
                                name = "CourseSchema",
                                schema = courseSchema,
                                strict = true
                            )
                        )
                    )
                )

                // Create a thread
                val thread = client.thread()

                // Add a message to the thread
                client.message(
                    threadId = thread.id,
                    request = MessageRequest(
                        role = Role.User,
                        content = "I need a course plan."
                    )
                )

                // Run the assistant
                val run = client.createRun(
                    thread.id,
                    request = RunRequest(
                        assistantId = assistant.id,
                        instructions = "Please generate a course structure."
                    )
                )

                var retrievedRun = run
                var attempts = 0
                val maxAttempts = 20

                do {
                    delay(1500)
                    retrievedRun = client.getRun(threadId = thread.id, runId = run.id)
                    attempts++
                    if (attempts >= maxAttempts) {
                        throw TimeoutException("Assistant run timed out")
                    }
                } while (retrievedRun.status != Status.Completed)

                // Get the assistant's response - UPDATED THIS PART
                val messages = client.messages(thread.id)
                val assistantMessage = messages.find { it.role == Role.Assistant }
                    ?: return@withContext FirebaseResult.Error(Exception("No assistant response found"))

                val content = assistantMessage.content.firstOrNull() as? MessageContent.Text
                    ?: return@withContext FirebaseResult.Error(Exception("No text content found"))

                try {
                    val json = Json { ignoreUnknownKeys = true }
                    val generatedCourse = json.decodeFromString<GeneratedCourse>(content.text.value)
                    FirebaseResult.Success(generatedCourse)
                } catch (e: Exception) {
                    println("Error parsing course JSON: ${e.message}")
                    FirebaseResult.Error(e)
                }

            } catch (e: Exception) {
                println("Exception in generateCourse: ${e.javaClass.simpleName} - ${e.message}")
                e.printStackTrace()
                FirebaseResult.Error(e)
            }
        }
    }
}