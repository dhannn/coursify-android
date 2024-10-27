package com.mobdeve.xx22.gilo.joshua.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobdeve.xx22.gilo.joshua.myapplication.components.MainScaffold
import com.mobdeve.xx22.gilo.joshua.myapplication.learningplan.CourseDetailScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.learningplan.GeneratingScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.learningplan.NewLearningPlanScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.login.LoginScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.signup.SignupScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.onboarding.OnboardingScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.onboarding.OnboardingUtils
import com.mobdeve.xx22.gilo.joshua.myapplication.profile.ProfileScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.savedplans.MyPlansScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.tracking.TrackingScreen
import com.mobdeve.xx22.gilo.joshua.myapplication.ui.theme.MyApplicationTheme
import com.mobdeve.xx22.gilo.joshua.myapplication.utils.navigateToBottomBarRoute

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object SavedPlans : Screen("saved_plans")
    object Tracking : Screen("tracking")
    object Profile : Screen("profile")
    object NewLearningPlan : Screen("new_learning_plan")
    object Generating : Screen("generating")
    object CourseDetail : Screen("course_detail")
}

class MainActivity : ComponentActivity() {
    private val onboardingUtils by lazy { OnboardingUtils(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    MainScaffold(navController = navController) { paddingModifier ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = paddingModifier
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
                LoginScreen(
                    onLoginClick = { email, password ->
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onCreateAccountClick = {
                        navController.navigate(Screen.Signup.route)
                    }
                )
            }

            composable(Screen.Signup.route) {
                SignupScreen(
                    onSignupComplete = { email, password ->
                        navController.navigate(Screen.Login.route)
                    }
                )
            }

            composable(Screen.Home.route) {
                MyPlansScreen(
                    modifier = paddingModifier,
                    onProfileClick = {
                        navController.navigateToBottomBarRoute(Screen.Profile.route)
                    },
                    onCourseClick = {
                        navController.navigate(Screen.CourseDetail.route)
                    }
                )
            }

            composable(Screen.Tracking.route) {
                TrackingScreen(
                    modifier = paddingModifier,
                    onProfileClick = {
                        navController.navigateToBottomBarRoute(Screen.Profile.route)
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    modifier = paddingModifier,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            // Other screens without bottom bar
            composable(Screen.NewLearningPlan.route) {
                NewLearningPlanScreen(
                    onCoursify = {
                        navController.navigate(Screen.Generating.route)
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Generating.route) {
                GeneratingScreen(navController = navController)
            }

            composable(Screen.CourseDetail.route) {
                CourseDetailScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}