package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository {

    suspend fun signIn(email: String, password: String): FirebaseResult<FirebaseUser> {
        return try {
            val result = FirebaseManager.auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                FirebaseResult.Success(it)
            } ?: FirebaseResult.Error(Exception("Authentication failed"))
        } catch (e: Exception) {
            e.toFirebaseResult()
        }
    }

    suspend fun signUp(email: String, password: String): FirebaseResult<FirebaseUser> {
        return try {
            val result = FirebaseManager.auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let {
                FirebaseResult.Success(it)
            } ?: FirebaseResult.Error(Exception("Sign up failed"))
        } catch (e: Exception) {
            e.toFirebaseResult()
        }
    }

    suspend fun signOut(): FirebaseResult<Unit> {
        return try {
            FirebaseManager.auth.signOut()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            e.toFirebaseResult()
        }
    }

    fun getAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }

        FirebaseManager.auth.addAuthStateListener(authStateListener)

        awaitClose {
            FirebaseManager.auth.removeAuthStateListener(authStateListener)
        }
    }

    suspend fun deleteAccount(): FirebaseResult<Unit> {
        return try {
            FirebaseManager.auth.currentUser?.delete()?.await()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            e.toFirebaseResult()
        }
    }
}