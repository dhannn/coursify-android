package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service

import android.util.Log
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseResult
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.GeneratedCourse
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.LearningPlanRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import kotlin.math.ceil
import kotlin.math.floor

class ClaudeService(private val apiKey: String) {
    private val client = HttpClient() {
        install(HttpTimeout) {
            requestTimeoutMillis = 50_000
        }
    }
    private val apiScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private fun buildPrompt(request: LearningPlanRequest): String {
        return """
                Create a structured learning plan with the following parameters:
                - Weekly commitment: ${floor(request.weeklyCommitment * 20)} hours
                - Course duration: ${floor(request.courseDuration * 12)} weeks
                - Learning goal: ${request.learningGoal}
                ${if (request.learningAbility.isNotBlank()) "- Desired learning outcome: ${request.learningAbility}" else ""}
                ${if (request.targetAudience.isNotBlank()) "- Target audience: ${request.targetAudience}" else ""}
                ${if (request.otherComments.isNotBlank()) "- Additional requirements: ${request.otherComments}" else ""}
                Create a detailed course structure with:
                1. A clear progression of topics
                2. 2-4 practical tasks per week
                3. Relevant subtopics for each main topic
                4. Content appropriate for the specified time commitment
                5. Return only the JSON object in the following format:
                
                {
                    \"course_title\": <a catchy title for the course>,
                    \"course_description\": <a catchy description for the course>,
                    \"weeks\": [
                        {
                            \"week_number\": <week number>,
                            \"main_topic\": <main topic>,
                            \"subtopics\": [
                                <subtopic 1>,
                                ...
                                <subtopic n>
                            ],
                            \"tasks\": [
                                <task 1>,
                                ...
                                <task n>
                            ]
                       }
                    ]
                }
        """.replace("\n", "\\n").replace("    ", "")
    }

    suspend fun generateCourse(request: LearningPlanRequest): FirebaseResult<GeneratedCourse> {
        return withContext(apiScope.coroutineContext) {
            val body =
                """
                        {
                            "model": "claude-3-5-sonnet-20241022",
                            "max_tokens": 1024,
                            "system": "You are an expert course designer.",
                            "messages": [
                                {"role": "user", "content": "${buildPrompt(request)}"}
                            ]
                        }
                    """.trimIndent()

            Log.d("ClaudeService", "Request body: $body")
            try {
                val response: HttpResponse =
                    client.request("https://api.anthropic.com/v1/messages") {
                        method = HttpMethod.Post
                        header("x-api-key", apiKey)
                        header("anthropic-version", "2023-06-01")
                        header("content-type", "application/json")
                        setBody(
                            """
                        {
                            "model": "claude-3-5-sonnet-20241022",
                            "max_tokens": 2048,
                            "system": "You are an expert course designer.",
                            "messages": [
                                {"role": "user", "content": "${buildPrompt(request)}"}
                            ]
                        }
                    """.trimIndent()
                        )
                    }

                val jsonObj = JSONObject(response.bodyAsText())
                val generatedString = jsonObj
                    .getJSONArray("content")
                    .getJSONObject(0)
                    .getString("text")

                try {
                    val json = Json { ignoreUnknownKeys = true }
                    val generatedCourse = json.decodeFromString<GeneratedCourse>(generatedString)
                    println("\n=== Successfully Generated Course ===")
                    println("Title: ${generatedCourse.courseTitle}")
                    println("Weeks: ${generatedCourse.weeks.size}")
                    return@withContext FirebaseResult.Success(generatedCourse)
                } catch (e: Exception) {
                    println("\n=== Error Parsing JSON ===")
                    println("Error: ${e.message}")
                    println("Raw JSON: ${response.bodyAsText()}")
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