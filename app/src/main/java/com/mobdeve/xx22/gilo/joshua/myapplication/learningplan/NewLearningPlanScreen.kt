package com.mobdeve.xx22.gilo.joshua.myapplication.learningplan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewLearningPlanScreen() {
    var learningGoal by remember { mutableStateOf("") }
    var weeklyCommitment by remember { mutableStateOf(0f) }
    var courseDuration by remember { mutableStateOf(0f) }
    var isAdvancedOptionsExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(30.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Title
            Text(
                text = "New Learning Plan",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Learning Goal Input
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

            // Weekly Commitment Slider
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

            // Course Duration Slider
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

            // Advanced Options
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
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
                        contentDescription = "Expand advanced options",
                        tint = Color.Black
                    )
                }
            }
        }

        // Bottom Buttons
        Column(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Button(
                onClick = { /* TODO: Handle coursify action */ },
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
                onClick = { /* TODO: Handle cancel action */ },
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
        NewLearningPlanScreen()
    }
}