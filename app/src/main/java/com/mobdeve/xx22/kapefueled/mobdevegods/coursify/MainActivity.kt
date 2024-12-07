package com.mobdeve.xx22.kapefueled.mobdevegods.coursify

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.components.MainScaffold
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseManager
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.LearningPlanRepository
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service.ChatGPTService
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service.ClaudeService
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.learningplan.CourseDetailScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.learningplan.GeneratingScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.learningplan.NewLearningPlanScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.login.LoginScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.signup.SignupScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.onboarding.OnboardingScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.onboarding.OnboardingUtils
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.profile.ProfileScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.home.HomeScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.savedplans.SavedPlansScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.tracking.TrackingScreen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.ui.theme.MyApplicationTheme
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.utils.PreferencesManager
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.utils.navigateToBottomBarRoute
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.viewmodel.AuthViewModel
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.viewmodel.NewLearningPlanViewModel

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object SavedPlans : Screen("saved_plans")
    object Tracking : Screen("tracking")
    object Profile : Screen("profile")
    object NewLearningPlan : Screen("new_learning_plan")
    object CourseDetail : Screen("course_detail/{planId}") {
        fun createRoute(planId: String) = "course_detail/$planId"
    }
    object Generating : Screen("generating/{planId}") {
        fun createRoute(planId: String) = "generating/$planId"
    }
}

private class NewLearningPlanViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewLearningPlanViewModel::class.java)) {
            val preferencesManager = PreferencesManager(context)
            val apiKey = preferencesManager.getOpenAIKey() ?: throw IllegalStateException("API key not found")
            val chatGPTService = ChatGPTService(apiKey)
            val claudeService = ClaudeService(apiKey)
            val repository = LearningPlanRepository(claudeService, chatGPTService)
            return NewLearningPlanViewModel(claudeService, chatGPTService, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivity : ComponentActivity() {
    private val onboardingUtils by lazy { OnboardingUtils(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseManager.initialize(this)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val authState by authViewModel.authState.collectAsState()

                    val startDestination = if (!onboardingUtils.isOnboardingCompleted()) {
                        Screen.Onboarding.route
                    } else if (!FirebaseManager.isUserSignedIn) {
                        Screen.Login.route
                    } else {
                        Screen.Home.route
                    }

                    AppNavigation(
                        navController = navController,
                        startDestination = startDestination,
                        onboardingUtils = onboardingUtils,
                        authViewModel = authViewModel,
                        context = this
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
    onboardingUtils: OnboardingUtils,
    authViewModel: AuthViewModel,
    context: Context  // Add context parameter
) {
    val newLearningPlanViewModelFactory = remember {
        NewLearningPlanViewModelFactory(context)
    }

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
                    onLoginSuccess = {
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
                    onSignupSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        navController.navigateUp()
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    modifier = paddingModifier,
                    onProfileClick = {
                        navController.navigateToBottomBarRoute(Screen.Profile.route)
                    },
                    onCourseClick = { planId ->
                        navController.navigate(Screen.CourseDetail.createRoute(planId))  // Or handle this differently
                    }
                )
            }

            composable(Screen.SavedPlans.route) {
                SavedPlansScreen(
                    modifier = paddingModifier,
                    onProfileClick = {
                        navController.navigateToBottomBarRoute(Screen.Profile.route)
                    },
                    onCourseClick = {  // Remove the planId parameter if SavedPlansScreen doesn't use it
                        navController.navigate(Screen.CourseDetail.createRoute("default-id"))  // Or handle this differently
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
                        authViewModel.signOut()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.NewLearningPlan.route) {
                val viewModel: NewLearningPlanViewModel = viewModel(factory = newLearningPlanViewModelFactory)
                NewLearningPlanScreen(
                    navController = navController,
                    onCoursify = { planId ->
                        navController.navigate(Screen.Generating.createRoute(planId)) {
                            popUpTo(Screen.NewLearningPlan.route) { inclusive = true }
                        }
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.Generating.route,
                arguments = listOf(navArgument("planId") { type = NavType.StringType })
            ) { backStackEntry ->
                val planId = backStackEntry.arguments?.getString("planId") ?: return@composable
                GeneratingScreen(
                    navController = navController,
                    planId = planId
                )
            }

            composable(
                route = Screen.CourseDetail.route,
                arguments = listOf(navArgument("planId") { type = NavType.StringType })
            ) { backStackEntry ->
                val planId = backStackEntry.arguments?.getString("planId") ?: return@composable
                Log.d("CourseDetail", "Plan ID: $planId")
                CourseDetailScreen(
                    planId = planId,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
