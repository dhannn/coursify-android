package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.learningplan

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseResult
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.viewmodel.LearningPlanViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.models.LearningPlan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    planId: String,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel: LearningPlanViewModel = viewModel(
        factory = LearningPlanViewModel.Factory(context)
    )
    var showError by remember { mutableStateOf<String?>(null) }
    val planState by viewModel.currentPlan.collectAsState()


    LaunchedEffect(planId) {
        Log.d("CourseDetailScreen", "Loading plan: $planId")
        viewModel.loadPlan(planId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(30.dp)
    ) {
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            actions = {
                Row {
                    // Only show actions when plan is loaded
                    if (planState is FirebaseResult.Success) {
                        val plan = (planState as FirebaseResult.Success<LearningPlan>).data
                        IconButton(
                            onClick = { viewModel.toggleBookmark(planId) }
                        ) {
                            Log.d("CourseDetailScreen::Bookmark", plan.bookmarked.toString())
                            Icon(
                                imageVector = if (plan.bookmarked)
                                    Icons.Default.Bookmark
                                else Icons.Default.BookmarkBorder,
                                contentDescription = if (plan.bookmarked)
                                    "Remove Bookmark"
                                else "Add Bookmark",
                                tint = Color.Black
                            )
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        when (planState) {
            is FirebaseResult.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            is FirebaseResult.Success -> {
                val plan = (planState as FirebaseResult.Success<LearningPlan>).data

                // Check if plan is still generating
                if (plan.status == "pending") {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = Color.Black)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Generating your learning plan...",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Button(
                                onClick = onBackClick,
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .width(200.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                            ) {
                                Text("Go Back", color = Color.White)
                            }
                        }
                    }
                    return
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Text(
                            text = plan.title,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 34.sp,
                            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                        )

                        Text(
                            text = plan.learningGoal,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            lineHeight = 24.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Show commitment info
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF8F9FA)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Course Details",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text("Weekly Commitment: ${(plan.weeklyCommitment * 10).toInt()} hours")
                                Text("Duration: ${(plan.courseDuration * 12).toInt()} weeks")
                                if (plan.learningAbility.isNotBlank()) {
                                    Text("Goal: ${plan.learningAbility}")
                                }
                            }
                        }

                        // Display weeks
                        plan.weeks.forEach { week ->
                            Text(
                                text = "Week ${week.weekNumber}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Text(
                                text = "Main Topic: ${week.mainTopic}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            if (week.subtopics.isNotEmpty()) {
                                Text(
                                    text = "Subtopics:",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFF8F9FA)
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        week.subtopics.forEach { subtopic ->
                                            Text(
                                                text = "• $subtopic",
                                                modifier = Modifier.padding(bottom = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            if (week.tasks.isNotEmpty()) {
                                Text(
                                    text = "Tasks:",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 24.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFF8F9FA)
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        week.tasks.forEach { task ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            ) {
                                                Checkbox(
                                                    checked = task.isCompleted,
                                                    onCheckedChange = { isChecked ->
                                                        viewModel.updateTaskCompletion(
                                                            planId,
                                                            week.weekNumber,
                                                            task,
                                                            isChecked
                                                        )
                                                    },
                                                    colors = CheckboxDefaults.colors(
                                                        checkedColor = Color.Black,
                                                        uncheckedColor = Color.Gray
                                                    )
                                                )
                                                Text(
                                                    text = task.description,
                                                    style = if (task.isCompleted) {
                                                        MaterialTheme.typography.bodyMedium.copy(
                                                            color = Color.Gray,
                                                            textDecoration = TextDecoration.LineThrough
                                                        )
                                                    } else {
                                                        MaterialTheme.typography.bodyMedium
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is FirebaseResult.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (planState as FirebaseResult.Error).exception.message
                                ?: "Error loading course",
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = onBackClick,
                            modifier = Modifier.width(200.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Go Back", color = Color.White)
                        }
                    }
                }
            }

            null -> {
                // Initial state, show loading
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCourseDetailScreen() {
    MaterialTheme {
        CourseDetailScreen(
            planId = "preview-plan-id",
            onBackClick = {}
        )
    }
}