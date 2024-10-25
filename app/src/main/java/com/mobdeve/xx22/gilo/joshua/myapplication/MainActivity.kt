package com.mobdeve.xx22.gilo.joshua.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.mobdeve.xx22.gilo.joshua.myapplication.onboarding.OnboardingScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.onboarding.OnboardingUtils
import com.mobdeve.xx22.gilo.joshua.myapplication.ui.theme.MyApplicationTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val onboardingUtils by lazy { OnboardingUtils(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()

        setContent {
            MyApplicationTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    if (onboardingUtils.isOnboardingCompleted()) {
                        ShowHomeScreen()
                    } else {
                        ShowOnboardingScreen()

                    }
                }
            }
        }


    }


    @Composable
    private fun ShowHomeScreen() {

        Column {
            Text(text = "Home Screen", style = MaterialTheme.typography.headlineLarge)
        }


    }

    @Composable
    private fun ShowOnboardingScreen() {
        val scope = rememberCoroutineScope()
        OnboardingScreen {
            onboardingUtils.setOnboardingCompleted()
            scope.launch {
                setContent {
                    ShowHomeScreen()
                }
            }
        }


    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MyApplicationTheme {
            ShowHomeScreen()
        }
    }
}