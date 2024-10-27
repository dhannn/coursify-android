package com.mobdeve.xx22.gilo.joshua.myapplication.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(150.dp))

        // Profile Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(60.dp),
                tint = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Name
        Text(
            text = "Name",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Delete Account Button
        Button(
            onClick = { /* TODO: Handle delete account */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Delete Account",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        Button(
            onClick = { /* TODO: Handle logout */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Logout",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    MaterialTheme {
        ProfileScreen()
    }
}