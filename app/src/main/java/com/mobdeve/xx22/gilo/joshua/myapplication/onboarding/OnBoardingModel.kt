package com.mobdeve.xx22.gilo.joshua.myapplication.onboarding

import androidx.annotation.DrawableRes
import com.mobdeve.xx22.gilo.joshua.myapplication.R

sealed class OnboardingModel(
    @DrawableRes val image: Int,
    val title: String,
    val description: String,
) {

    data object FirstPages : OnboardingModel(
        image = R.drawable.o1, // Replace with appropriate drawable resource
        title = "Create Your Learning Plan",
        description = "Tell us what you want to learn, and we'll generate a learning plan designed for your goals. Your path to mastery starts here!"
    )

    data object SecondPages : OnboardingModel(
        image = R.drawable.o2, // Replace with appropriate drawable resource
        title = "Regenerate Your Plan Anytime",
        description = "Not happy with your plan? No worries! You can regenerate it with just one click to better fit your needs."
    )

    data object ThirdPages : OnboardingModel(
        image = R.drawable.o3, // Replace with appropriate drawable resource
        title = "Save and View Plans Anytime",
        description = "Register and log in to save your learning plans. Access them anytime and keep improving at your own pace."
    )
}
