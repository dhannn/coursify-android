package com.mobdeve.xx22.coursify.domain.model

data class WeeklyTask (
    val weekNumber: Int,
    val tasks: List<Task>
)