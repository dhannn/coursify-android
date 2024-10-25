package com.mobdeve.xx22.gilo.joshua.myapplication.savedplans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person

@Composable
fun MyPlansScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Use theme background color
    ) {
        // Top row with "My Plans" and user icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Plans",
                style = MaterialTheme.typography.headlineMedium // Use MaterialTheme typography
            )
            IconButton(onClick = { /* TODO: Handle profile click */ }) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray), // Circle background for user icon
                    contentAlignment = Alignment.Center // Align the icon to the center
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Person",
                        tint = MaterialTheme.colorScheme.onBackground // Use theme color for the icon
                    )
                }
            }
        }

        // Centered content for the Elevated Card with plan
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp), // Adjust top padding for centered card
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Elevated card for MOBDEVE plan
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f)
                    .height(100.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "MOBDEVE",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Introduction to Android Development",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyPlansScreen() {
    MyPlansScreen()
}
