package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.learningplan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.R
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.Screen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseResult
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.viewmodel.LearningPlanViewModel
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.LearningPlan

@Composable
fun GeneratingScreen(
    navController: NavController,
    planId: String,
) {
    val context = LocalContext.current
    val learningPlanViewModel: LearningPlanViewModel = viewModel(
        factory = remember { LearningPlanViewModel.Factory(context) }
    )

    var currentStep by remember { mutableStateOf(0) }
    var showError by remember { mutableStateOf<String?>(null) }
    val planState by learningPlanViewModel.currentPlan.collectAsState()

    val steps = listOf(
        "Analyzing your learning preferences...",
        "Creating course structure...",
        "Generating weekly content...",
        "Finalizing your learning plan..."
    )

    LaunchedEffect(planId) {
        while (true) {
            learningPlanViewModel.loadPlan(planId)
            when (planState) {
                is FirebaseResult.Success -> {
                    val plan = (planState as FirebaseResult.Success<LearningPlan>).data

                    // Update step based on plan status
                    currentStep = when {
                        plan.status == "active" -> {
                            // Plan is ready, navigate to detail screen
                            navController.navigate(Screen.CourseDetail.createRoute(planId)) {
                                popUpTo(Screen.NewLearningPlan.route) { inclusive = true }
                            }
                            3 // Set to final step before navigation
                        }
                        plan.weeks.isEmpty() -> 0
                        plan.weeks.any { it.subtopics.isEmpty() } -> 1
                        plan.weeks.any { it.tasks.isEmpty() } -> 2
                        else -> 3
                    }

                    if (plan.status == "active") break
                }
                is FirebaseResult.Error -> {
                    showError = (planState as FirebaseResult.Error).exception.message
                    break
                }
                else -> { /* Keep polling */ }
            }
            delay(2000) // Poll every 2 seconds
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(200.dp))

        Image(
            painterResource(R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier.size(217.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (showError != null) {
            Text(
                text = showError!!,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(16.dp)
                    .width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Go Back", color = Color.White)
            }
        } else {
            Text(
                text = steps[currentStep],
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(
                color = Color.Black,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            LinearProgressIndicator(
                progress = (currentStep + 1).toFloat() / steps.size,
                modifier = Modifier
                    .width(200.dp)
                    .height(4.dp),
                color = Color.Black,
                trackColor = Color.LightGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGeneratingScreen() {
    MaterialTheme {
        GeneratingScreen(
            navController = rememberNavController(),
            planId = "preview-plan-id"
        )
    }
}