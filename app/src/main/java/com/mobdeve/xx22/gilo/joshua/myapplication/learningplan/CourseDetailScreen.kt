package com.mobdeve.xx22.gilo.joshua.myapplication.learningplan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = { /* TODO: Handle navigation */ }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            actions = {
                Row {
                    IconButton(onClick = { /* TODO: Handle refresh */ }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.Black
                        )
                    }
                    IconButton(onClick = { /* TODO: Handle bookmark */ }) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = Color.Black
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                // Title
                Text(
                    text = "Introduction to\nAndroid Development",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                )

                // Description
                Text(
                    text = "This course covers Android development basics using Java or Kotlin, guiding learners to build a functional app.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Week 1
                Text(
                    text = "Week 1",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Main Topic
                Text(
                    text = "Main Topic: Android Studio & Basics of Android Development",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Subtopics
                Text(
                    text = "Subtopics:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                    Text("• Installing and configuring Android Studio")
                    Text("• Java/Kotlin programming review")
                }

                // Tasks
                Text(
                    text = "Tasks",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Column(modifier = Modifier.padding(start = 16.dp, bottom = 24.dp)) {
                    Text("• Set up Android Studio and create a simple project.")
                    Text("• Review Java/Kotlin basics.")
                    Text("• Watch an introductory video on Android Studio navigation.")
                }

                // Week 2
                Text(
                    text = "Week 2",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Main Topic
                Text(
                    text = "Main Topic: Activity Lifecycle and UI Components",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Subtopics
                Text(
                    text = "Subtopics:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                    Text("• Activity lifecycle overview")
                    Text("• Layouts and Views")
                }

                // Tasks
                Text(
                    text = "Tasks",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Column(modifier = Modifier.padding(start = 16.dp, bottom = 24.dp)) {
                    Text("• Create a simple app with TextView and Button.")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCourseDetailScreen() {
    MaterialTheme {
        CourseDetailScreen()
    }
}