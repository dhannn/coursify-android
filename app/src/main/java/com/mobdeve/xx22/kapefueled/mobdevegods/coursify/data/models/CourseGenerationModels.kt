package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeneratedCourse(
    val weeks: List<GeneratedWeek>,
    @SerialName("course_title")
    val courseTitle: String,
    @SerialName("course_description")
    val courseDescription: String
)

@Serializable
data class GeneratedWeek(
    @SerialName("week_number")
    val weekNumber: Int,
    @SerialName("main_topic")
    val mainTopic: String,
    val subtopics: List<String>,
    val tasks: List<String>
)

@Serializable
data class CourseGenerationPrompt(
    @SerialName("learning_goal")
    val learningGoal: String,
    @SerialName("weekly_hours")
    val weeklyHours: Int,
    @SerialName("total_weeks")
    val totalWeeks: Int,
    @SerialName("learning_ability")
    val learningAbility: String?,
    @SerialName("target_audience")
    val targetAudience: String?,
    @SerialName("additional_requirements")
    val additionalRequirements: String?
)