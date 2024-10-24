package com.mobdeve.xx22.coursify.domain.model

data class LearningPlan(
    val id: String = "",
    val topic: String,
    val weeklyCommitment: Int,
    val totalCommitmentWeeks: Int,
    val advancedSettings: AdvancedSettings? = null,
    val weeklyTasks: List<WeeklyTask> = emptyList(),
    val userId: String? = null
)
