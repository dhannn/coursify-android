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