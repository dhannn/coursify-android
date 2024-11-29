package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase

sealed class FirebaseResult<out T> {
    data class Success<T>(val data: T) : FirebaseResult<T>()
    data class Error(val exception: Exception) : FirebaseResult<Nothing>()
    object Loading : FirebaseResult<Nothing>()
}

fun Exception.toFirebaseResult() = FirebaseResult.Error(this)