package com.mobdeve.xx22.gilo.joshua.myapplication.onboarding

import android.content.Context

class OnboardingUtils(private val context: Context) {

    fun isOnboardingCompleted(): Boolean {
        return context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            .getBoolean("completed", false)
    }

    fun setOnboardingCompleted() {
        context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("completed", false)
            // .putBoolean("completed", true) // once finished change.
            .apply()
    }
}