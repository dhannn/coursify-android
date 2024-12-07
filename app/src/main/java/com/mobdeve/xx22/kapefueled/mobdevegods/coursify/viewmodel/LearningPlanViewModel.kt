package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseResult
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.LearningPlan
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.Task
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.Week
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.LearningPlanRepository
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.LearningPlanRequest
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service.ChatGPTService
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service.ClaudeService
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LearningPlanViewModel(
    private val context: Context
) : ViewModel() {
    private val preferencesManager = PreferencesManager(context)
    private val chatGPTService = ChatGPTService(
        preferencesManager.getOpenAIKey() ?: PreferencesManager.KEY_OPENAI_API
    )
    private val claudeService = ClaudeService(
        preferencesManager.getOpenAIKey() ?: PreferencesManager.KEY_OPENAI_API
    )
    private val repository = LearningPlanRepository(claudeService, chatGPTService)

    // State flows
    private val _planState = MutableStateFlow<FirebaseResult<String>?>(null)
    val planState: StateFlow<FirebaseResult<String>?> = _planState

    private val _currentPlan = MutableStateFlow<FirebaseResult<LearningPlan>?>(null)
    val currentPlan: StateFlow<FirebaseResult<LearningPlan>?> = _currentPlan

    private val _userPlans = MutableStateFlow<FirebaseResult<List<LearningPlan>>?>(null)
    val userPlans: StateFlow<FirebaseResult<List<LearningPlan>>?> = _userPlans

    private val _bookmarkedPlans = MutableStateFlow<FirebaseResult<List<LearningPlan>>?>(null)
    val bookmarkedPlans: StateFlow<FirebaseResult<List<LearningPlan>>?> = _bookmarkedPlans

    fun createPlan(
        learningGoal: String,
        weeklyCommitment: Float,
        courseDuration: Float,
        learningAbility: String = "",
        targetAudience: String = "",
        otherComments: String = ""
    ) {
        viewModelScope.launch {
            _planState.value = FirebaseResult.Loading

            val plan = LearningPlan(
                title = learningGoal,
                learningGoal = learningGoal,
                weeklyCommitment = weeklyCommitment,
                courseDuration = courseDuration,
                learningAbility = learningAbility,
                targetAudience = targetAudience,
                otherComments = otherComments
            )

            _planState.value = repository.createPlan(plan)
        }
    }

    fun loadPlan(planId: String) {
        viewModelScope.launch {
            _currentPlan.value = FirebaseResult.Loading
            _currentPlan.value = repository.getPlan(planId)
            Log.d("loadPlan", planId)
        }
    }

    fun loadUserPlans() {
        viewModelScope.launch {
            repository.getUserPlans().collect { result ->
                _userPlans.value = result
                Log.d("loadUserPlans", result.toString())
                Log.d("loadUserPlans", _userPlans.value.toString())
            }
        }
    }

    fun loadBookmarkedPlans() {
        viewModelScope.launch {
            repository.getBookmarkedPlans().collect { result ->
                _bookmarkedPlans.value = result
            }
        }
    }

    fun toggleBookmark(planId: String) {
        viewModelScope.launch {
            when (val result = repository.toggleBookmark(planId)) {
                is FirebaseResult.Success -> {
                    // Refresh current plan and lists
                    Log.d("toggleBookmark", (currentPlan.value as FirebaseResult.Success<LearningPlan>).data.bookmarked.toString())
                    loadPlan(planId)
                    loadUserPlans()
                    loadBookmarkedPlans()
                }
                is FirebaseResult.Error -> {
                    _currentPlan.value = FirebaseResult.Error(result.exception)
                }
                else -> {}
            }
        }
    }

    fun updateTaskCompletion(planId: String, weekNumber: Int, task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            when (val result = repository.updateTaskCompletion(planId, weekNumber, task, isCompleted)) {
                is FirebaseResult.Loading -> {
                    _currentPlan.value = FirebaseResult.Loading
                }

                is FirebaseResult.Success -> {
                    _currentPlan.value = FirebaseResult.Success((currentPlan.value as FirebaseResult.Success<LearningPlan>).data)
                }
                is FirebaseResult.Error -> {
                    _currentPlan.value = FirebaseResult.Error(result.exception)
                }
                else -> {}
            }
        }
    }

    fun updatePlanStatus(planId: String, status: String) {
        viewModelScope.launch {
            when (val result = repository.updatePlanStatus(planId, status)) {
                is FirebaseResult.Success -> {
                    loadPlan(planId)
                    loadUserPlans()
                }
                is FirebaseResult.Error -> {
                    _currentPlan.value = FirebaseResult.Error(result.exception)
                }
                else -> {}
            }
        }
    }

    fun saveCourseContent(planId: String, weeks: List<Week>) {
        viewModelScope.launch {
            when (val result = repository.saveCourseContent(planId, weeks)) {
                is FirebaseResult.Success -> {
                    loadPlan(planId)
                }
                is FirebaseResult.Error -> {
                    _currentPlan.value = FirebaseResult.Error(result.exception)
                }
                else -> {}
            }
        }
    }

    fun deletePlan(planId: String) {
        viewModelScope.launch {
            when (val result = repository.deletePlan(planId)) {
                is FirebaseResult.Success -> {
                    if (_currentPlan.value is FirebaseResult.Success &&
                        (_currentPlan.value as FirebaseResult.Success<LearningPlan>).data.planId == planId
                    ) {
                        _currentPlan.value = null
                    }
                    loadUserPlans()
                    loadBookmarkedPlans()
                }
                is FirebaseResult.Error -> {
                    _currentPlan.value = FirebaseResult.Error(result.exception)
                }
                else -> {}
            }
        }
    }

    fun clearStates() {
        _planState.value = null
        _currentPlan.value = null
        _userPlans.value = null
        _bookmarkedPlans.value = null
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LearningPlanViewModel::class.java)) {
                return LearningPlanViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    fun regeneratePlan(planId: String, oldPlan: LearningPlan) {
        viewModelScope.launch {
            try {
                _currentPlan.value = FirebaseResult.Loading
                when (val result = repository.regeneratePlan(planId, oldPlan)) {
                    is FirebaseResult.Success -> {
                        loadPlan(planId)
                        loadUserPlans()
                    }
                    is FirebaseResult.Error -> {
                        _currentPlan.value = FirebaseResult.Error(result.exception)
                    }
                    is FirebaseResult.Loading -> {
                    }
                }
            } catch (e: Exception) {
                _currentPlan.value = FirebaseResult.Error(e)
            }
        }
    }
}
