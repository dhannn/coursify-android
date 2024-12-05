package com.mobdeve.xx22.kapefueled.mobdevegods.coursify

import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.LearningPlanRequest
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service.ClaudeService
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun test_Claude_connection() = runBlocking {
        val service = ClaudeService("sk-")
        val request = LearningPlanRequest(
            learningGoal = "Prompt Engineering for CS Students",
            weeklyCommitment = 4f,
            courseDuration = 8f
        )


        val result = service.generateCourse(request)
        println(result)
    }
}