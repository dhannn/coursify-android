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
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
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

    val configOpenAI = OpenAIConfig(
        token = apiKey,
        host = OpenAIHost("https://openrouter.ai/api/v1/")
    )

    private val client = OpenAI(configOpenAI)
    private val apiScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val courseSchema = JsonObject(
        mapOf(
            "type" to JsonPrimitive("object"),
            "additionalProperties" to JsonPrimitive(false),
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
                println("=== Starting Course Generation ===")
                println("Request details:")
                println("- Learning Goal: ${request.learningGoal}")
                println("- Weekly Commitment: ${request.weeklyCommitment} hours")
                println("- Course Duration: ${request.courseDuration} weeks")
                println("- Learning Ability: ${request.learningAbility}")
                println("- Target Audience: ${request.targetAudience}")
                println("- Other Comments: ${request.otherComments}")

                val instructions = """
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
            """.trimIndent()

                println("\n=== Assistant Instructions ===")
                println(instructions)

                val assistant = client.assistant(
                    request = AssistantRequest(
                        name = "Course Creation Assistant",
                        instructions = instructions,
                        tools = emptyList(),
                        model = ModelId("claude-3-5-sonnet-20241022"),
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

                println("\n=== Created Assistant ===")
                println("Assistant ID: ${assistant.id}")

                val thread = client.thread()
                println("\n=== Created Thread ===")
                println("Thread ID: ${thread.id}")

                println("\n=== Sending Message ===")
                val message = client.message(
                    threadId = thread.id,
                    request = MessageRequest(
                        role = Role.User,
                        content = "I need a course plan for: ${request.learningGoal}" // Modified this line
                    )
                )
                println("Message sent with ID: ${message.id}")

                val run = client.createRun(
                    thread.id,
                    request = RunRequest(
                        assistantId = assistant.id,
                        instructions = "Please generate a detailed course structure for: ${request.learningGoal}" // Modified this line
                    )
                )
                println("\n=== Started Run ===")
                println("Run ID: ${run.id}")

                var retrievedRun = run
                var attempts = 0
                val maxAttempts = 20

                do {
                    delay(1500)
                    retrievedRun = client.getRun(threadId = thread.id, runId = run.id)
                    println("Run status: ${retrievedRun.status} (Attempt ${attempts + 1})")
                    attempts++
                    if (attempts >= maxAttempts) {
                        throw TimeoutException("Assistant run timed out")
                    }
                } while (retrievedRun.status != Status.Completed)

                val messages = client.messages(thread.id)
                println("\n=== Retrieved Messages ===")
                messages.forEach { msg ->
                    println("Message ${msg.id}:")
                    println("Role: ${msg.role}")
                    println("Content: ${(msg.content.firstOrNull() as? MessageContent.Text)?.text?.value ?: "No content"}")
                    println("---")
                }

                val assistantMessage = messages.find { it.role == Role.Assistant }
                    ?: return@withContext FirebaseResult.Error(Exception("No assistant response found"))

                val content = assistantMessage.content.firstOrNull() as? MessageContent.Text
                    ?: return@withContext FirebaseResult.Error(Exception("No text content found"))

                try {
                    val json = Json { ignoreUnknownKeys = true }
                    val generatedCourse = json.decodeFromString<GeneratedCourse>(content.text.value)
                    println("\n=== Successfully Generated Course ===")
                    println("Title: ${generatedCourse.courseTitle}")
                    println("Weeks: ${generatedCourse.weeks.size}")
                    return@withContext FirebaseResult.Success(generatedCourse)
                } catch (e: Exception) {
                    println("\n=== Error Parsing JSON ===")
                    println("Error: ${e.message}")
                    println("Raw JSON: ${content.text.value}")
                    return@withContext FirebaseResult.Error(e)
                }

            } catch (e: Exception) {
                println("\n=== Fatal Error ===")
                println("Type: ${e.javaClass.simpleName}")
                println("Message: ${e.message}")
                e.printStackTrace()
                FirebaseResult.Error(e)
            }
        }
    }
}