package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseResult
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.LearningPlanRequest
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.LearningPlanRepository
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.LearningPlan
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service.ChatGPTService
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service.ClaudeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewLearningPlanViewModel(
    private val claudeService: ClaudeService,
    private val chatGPTService: ChatGPTService,
    private val repository: LearningPlanRepository
) : ViewModel() {

    private val _generationState = MutableStateFlow<FirebaseResult<String>?>(null)
    val generationState: StateFlow<FirebaseResult<String>?> = _generationState

    private val generationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    fun generatePlan(
        learningGoal: String,
        weeklyCommitment: Float,
        courseDuration: Float,
        learningAbility: String = "",
        targetAudience: String = "",
        otherComments: String = ""
    ) {
        viewModelScope.launch {
            _generationState.value = FirebaseResult.Loading

            val initialPlan = LearningPlan(
                title = "Generating...",
                status = "pending",
                learningGoal = learningGoal,
                weeklyCommitment = weeklyCommitment,
                courseDuration = courseDuration,
                learningAbility = learningAbility,
                targetAudience = targetAudience,
                otherComments = otherComments,
                weeks = emptyList()
            )

            when (val createResult = repository.createPlan(initialPlan)) {
                is FirebaseResult.Success -> {
                    val planId = createResult.data
                    _generationState.value = FirebaseResult.Success(planId)

                    generationScope.launch {
                        repository.generateAndSavePlan(
                            LearningPlanRequest(
                                id = planId,
                                learningGoal = learningGoal,
                                weeklyCommitment = weeklyCommitment,
                                courseDuration = courseDuration,
                                learningAbility = learningAbility,
                                targetAudience = targetAudience,
                                otherComments = otherComments
                            )
                        )
                    }
                }
                is FirebaseResult.Error -> {
                    _generationState.value = createResult
                }
                else -> {
                    _generationState.value = FirebaseResult.Error(Exception("Unexpected state"))
                }
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        // Optional: Cancel generation scope when ViewModel is cleared
        // generationScope.cancel()
    }
}