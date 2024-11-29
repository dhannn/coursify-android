package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class LearningPlan(
    @DocumentId
    val planId: String = "",
    val userId: String = "",
    val title: String = "",
    val createdAt: Timestamp? = Timestamp.now(),
    val lastModified: Timestamp? = Timestamp.now(),
    val learningGoal: String = "",
    val weeklyCommitment: Float = 0f,
    val courseDuration: Float = 0f,
    val status: String = "active",
    val isBookmarked: Boolean = false,

    val learningAbility: String = "",
    val targetAudience: String = "",
    val otherComments: String = "",

    val weeks: List<Week> = emptyList()
)

data class Week(
    val weekNumber: Int = 0,
    val title: String = "",
    val mainTopic: String = "",
    val subtopics: List<String> = emptyList(),
    val tasks: List<Task> = emptyList()
)

data class Task(
    val description: String = "",
    val isCompleted: Boolean = false,
    val completedAt: Timestamp? = null
)