package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.utils

import android.content.Context

class PreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        "coursify_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        const val KEY_OPENAI_API = "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"
    }
    fun saveOpenAIKey(apiKey: String) {
        sharedPreferences.edit()
            .putString(KEY_OPENAI_API, apiKey)
            .apply()
    }

    fun getOpenAIKey(): String {
        return sharedPreferences.getString(KEY_OPENAI_API, null)
            ?: KEY_OPENAI_API
    }
}