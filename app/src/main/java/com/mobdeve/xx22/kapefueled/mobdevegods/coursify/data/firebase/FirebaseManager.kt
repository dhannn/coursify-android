package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase

import android.content.Context;
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

object FirebaseManager {
lateinit var auth: FirebaseAuth
private set

lateinit var firestore: FirebaseFirestore
private set

lateinit var storage: FirebaseStorage
private set

fun initialize(context: Context) {
    FirebaseApp.initializeApp(context)
    auth = Firebase.auth
    firestore = Firebase.firestore
    storage = Firebase.storage
}

val isUserSignedIn: Boolean
get() = auth.currentUser != null

val currentUserId: String?
get() = auth.currentUser?.uid
}