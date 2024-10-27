package com.mobdeve.xx22.kapefueled.mobdevegods.coursify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.components.MainScaffold
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.learningplan.CourseDetailScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.learningplan.GeneratingScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.learningplan.NewLearningPlanScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.login.LoginScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.signup.SignupScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.onboarding.OnboardingScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.onboarding.OnboardingUtils
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.profile.ProfileScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.home.HomeScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.home.SavedPlansScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.tracking.TrackingScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.ui.theme.MyApplicationTheme
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.utils.navigateToBottomBarRoute

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
                HomeScreen(
                    modifier = paddingModifier,
                    onProfileClick = {
                        navController.navigateToBottomBarRoute(Screen.Profile.route)
                    },
                    onCourseClick = {
                        navController.navigate(Screen.CourseDetail.route)
                    }
                )
            }

            composable(Screen.SavedPlans.route) {
                SavedPlansScreen(
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
