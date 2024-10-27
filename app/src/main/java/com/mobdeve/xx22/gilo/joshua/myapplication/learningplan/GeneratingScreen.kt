package com.mobdeve.xx22.gilo.joshua.myapplication.learningplan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.mobdeve.xx22.gilo.joshua.myapplication.R
import com.mobdeve.xx22.gilo.joshua.myapplication.Screen
import kotlinx.coroutines.delay

@Composable
fun GeneratingScreen(
    navController: NavController
) {
    LaunchedEffect(Unit) {
        delay(2000) // Simulate generation
        navController.navigate(Screen.CourseDetail.route) {
            popUpTo(Screen.NewLearningPlan.route) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(250.dp))

        Image(
            painterResource(R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier.size(217.dp)
        )

        Text(
            text = "Generating...",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGeneratingScreen() {
    MaterialTheme {
        GeneratingScreen(
            navController = NavController(LocalContext.current)
        )
    }
}
