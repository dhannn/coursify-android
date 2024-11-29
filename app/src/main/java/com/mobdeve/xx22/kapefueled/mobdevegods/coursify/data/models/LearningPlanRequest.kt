package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LearningPlanRequest(
    val id: String = "",
    val learningGoal: String,
    val weeklyCommitment: Float,
    val courseDuration: Float,
    val learningAbility: String = "",
    val targetAudience: String = "",
    val otherComments: String = ""
)