package com.dreamlab

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IkilemApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        val auth = Firebase.auth
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnSuccessListener { /* ready */ }
                .addOnFailureListener { it.printStackTrace() }
        }
    }
}