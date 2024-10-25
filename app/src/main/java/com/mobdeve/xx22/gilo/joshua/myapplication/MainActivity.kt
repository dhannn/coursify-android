package com.mobdeve.xx22.gilo.joshua.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobdeve.xx22.gilo.joshua.myapplication.login.LoginScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.onboarding.OnboardingScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.onboarding.OnboardingUtils
import com.mobdeve.xx22.gilo.joshua.myapplication.ui.theme.MyApplicationTheme

// Define sealed class for type-safe navigation
sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Home : Screen("home")
}

class MainActivity : ComponentActivity() {
    private val onboardingUtils by lazy { OnboardingUtils(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val startDestination = if (onboardingUtils.isOnboardingCompleted()) {
                        Screen.Login.route
                    } else {
                        Screen.Onboarding.route
                    }

                    AppNavigation(
                        navController = navController,
                        startDestination = startDestination,
                        onboardingUtils = onboardingUtils
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    onboardingUtils: OnboardingUtils
) {
    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Onboarding.route) {
                OnboardingScreen {
                    onboardingUtils.setOnboardingCompleted()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            }

            composable(Screen.Login.route) {
                LoginScreen { email, password ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
            composable(Screen.Home.route) {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
}