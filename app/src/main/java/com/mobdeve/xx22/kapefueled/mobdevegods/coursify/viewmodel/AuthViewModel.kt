package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.AuthRepository
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _authState = MutableStateFlow<FirebaseResult<Unit>?>(null)
    val authState: StateFlow<FirebaseResult<Unit>?> = _authState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = FirebaseResult.Loading
            val result = authRepository.signIn(email, password)
            _authState.value = when (result) {
                is FirebaseResult.Success -> FirebaseResult.Success(Unit)
                is FirebaseResult.Error -> FirebaseResult.Error(result.exception)
                is FirebaseResult.Loading -> FirebaseResult.Loading
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = FirebaseResult.Loading
            val result = authRepository.signUp(email, password)
            _authState.value = when (result) {
                is FirebaseResult.Success -> FirebaseResult.Success(Unit)
                is FirebaseResult.Error -> FirebaseResult.Error(result.exception)
                is FirebaseResult.Loading -> FirebaseResult.Loading
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _authState.value = FirebaseResult.Loading
            _authState.value = authRepository.signOut()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _authState.value = FirebaseResult.Loading
            _authState.value = authRepository.deleteAccount()
        }
    }
}