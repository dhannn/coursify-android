package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.learningplan

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseResult
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.viewmodel.NewLearningPlanViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.Screen
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.LearningPlanRepository
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.service.ChatGPTService
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.utils.PreferencesManager

class NewLearningPlanViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewLearningPlanViewModel::class.java)) {
            val preferencesManager = PreferencesManager(context)
            val apiKey = preferencesManager.getOpenAIKey() ?: PreferencesManager.KEY_OPENAI_API
            val chatGPTService = ChatGPTService(apiKey)
            val repository = LearningPlanRepository(chatGPTService)
            return NewLearningPlanViewModel(chatGPTService, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewLearningPlanScreen(
    navController: NavHostController,
    onCoursify: (String) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = viewModel<NewLearningPlanViewModel>(
        factory = remember { NewLearningPlanViewModelFactory(context) }
    )

    var learningGoal by remember { mutableStateOf("") }
    var weeklyCommitment by remember { mutableStateOf(0f) }
    var courseDuration by remember { mutableStateOf(0f) }
    var isAdvancedOptionsExpanded by remember { mutableStateOf(false) }
    var learningAbility by remember { mutableStateOf("") }
    var targetAudience by remember { mutableStateOf("") }
    var otherComments by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf<String?>(null) }

    val rotationState by animateFloatAsState(
        targetValue = if (isAdvancedOptionsExpanded) 180f else 0f,
        label = "arrow_rotation"
    )

    val generationState by viewModel.generationState.collectAsState()

    LaunchedEffect(generationState) {
        when (generationState) {
            is FirebaseResult.Success -> {
                val planId = (generationState as FirebaseResult.Success<String>).data
                onCoursify(planId)
            }
            is FirebaseResult.Error -> {
                showError = (generationState as FirebaseResult.Error).exception.message
            }
            is FirebaseResult.Loading -> {
                // Show loading state
            }
            null -> {
                // Initial state, do nothing
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .padding(bottom = 140.dp)
        ) {
            item {
                Text(
                    text = "New Learning Plan",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Text(
                    text = "I want to learn....",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = learningGoal,
                    onValueChange = {
                        learningGoal = it
                        showError = null
                    },
                    placeholder = {
                        Text(
                            "e.g. prompt engineering, songwriting",
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Text(
                    text = "I'm willing to commit weekly...",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "${(weeklyCommitment * 20).toInt()} hours per week",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Slider(
                    value = weeklyCommitment,
                    onValueChange = { weeklyCommitment = it },
                    modifier = Modifier.padding(bottom = 24.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = Color.Black,
                        inactiveTrackColor = Color.LightGray
                    )
                )

                Text(
                    text = "The course duration should be...",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "${(courseDuration * 12).toInt()} weeks",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Slider(
                    value = courseDuration,
                    onValueChange = { courseDuration = it },
                    modifier = Modifier.padding(bottom = 24.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = Color.Black,
                        inactiveTrackColor = Color.LightGray
                    )
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isAdvancedOptionsExpanded = !isAdvancedOptionsExpanded },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Advanced Options",
                                fontSize = 16.sp
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle advanced options",
                                modifier = Modifier.rotate(rotationState)
                            )
                        }

                        if (isAdvancedOptionsExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 16.dp)
                            ) {
                                Text(
                                    text = "I want to be able to...",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = learningAbility,
                                    onValueChange = { learningAbility = it },
                                    placeholder = {
                                        Text(
                                            "e.g. leverage AI tools for programming",
                                            color = Color.Gray
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.LightGray,
                                        focusedBorderColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                Text(
                                    text = "This learning plan is for...",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = targetAudience,
                                    onValueChange = { targetAudience = it },
                                    placeholder = {
                                        Text(
                                            "e.g. CS students who want to speed up",
                                            color = Color.Gray
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.LightGray,
                                        focusedBorderColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                Text(
                                    text = "Other Comments",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = otherComments,
                                    onValueChange = { otherComments = it },
                                    placeholder = {
                                        Text(
                                            "Any other requirements or preferences",
                                            color = Color.Gray
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.LightGray,
                                        focusedBorderColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Buttons
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFFF8F9FA))
                .padding(horizontal = 30.dp)
                .padding(bottom = 24.dp)
        ) {
            if (showError != null) {
                Text(
                    text = showError!!,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (learningGoal.isBlank()) {
                        showError = "Please enter what you want to learn"
                    } else {
                        viewModel.generatePlan(
                            learningGoal = learningGoal,
                            weeklyCommitment = weeklyCommitment,
                            courseDuration = courseDuration,
                            learningAbility = learningAbility,
                            targetAudience = targetAudience,
                            otherComments = otherComments
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Coursify!", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = generationState !is FirebaseResult.Loading
            ) {
                Text(
                    "Cancel",
                    color = Color.Black
                )
            }
        }
    }
}