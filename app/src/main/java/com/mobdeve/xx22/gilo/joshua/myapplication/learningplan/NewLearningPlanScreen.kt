package com.mobdeve.xx22.gilo.joshua.myapplication.learningplan

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewLearningPlanScreen(
    onCoursify: () -> Unit,
    onCancel: () -> Unit
) {
    var learningGoal by remember { mutableStateOf("") }
    var weeklyCommitment by remember { mutableStateOf(0f) }
    var courseDuration by remember { mutableStateOf(0f) }
    var isAdvancedOptionsExpanded by remember { mutableStateOf(false) }
    var learningAbility by remember { mutableStateOf("") }
    var targetAudience by remember { mutableStateOf("") }
    var otherComments by remember { mutableStateOf("") }

    val rotationState by animateFloatAsState(
        targetValue = if (isAdvancedOptionsExpanded) 180f else 0f,
        label = "arrow_rotation"
    )

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
                    onValueChange = { learningGoal = it },
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
                    text = "The course is good for...",
                    fontSize = 16.sp,
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
                                            "Other Comments",
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

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFFF8F9FA))
                .padding(horizontal = 30.dp)
                .padding(bottom = 24.dp)
        ) {
            Button(
                onClick = onCoursify,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Coursify!")
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
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Cancel",
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewLearningPlanScreen() {
    MaterialTheme {
        NewLearningPlanScreen(
            onCoursify = {},
            onCancel = {}
        )
    }
}