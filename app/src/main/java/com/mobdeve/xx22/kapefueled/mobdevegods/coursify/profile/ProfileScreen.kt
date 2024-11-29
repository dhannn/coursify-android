package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.viewmodel.AuthViewModel
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseResult
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseManager

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf<String?>(null) }

    val authState by authViewModel.authState.collectAsState()

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (authState) {
            is FirebaseResult.Success -> {
                onLogout()
            }
            is FirebaseResult.Error -> {
                showError = (authState as FirebaseResult.Error).exception.message
            }
            else -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(150.dp))

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

        Text(
            text = FirebaseManager.auth.currentUser?.email ?: "User",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Show error if exists
        showError?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
            )
        }

        Button(
            onClick = { showDeleteConfirmDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = authState !is FirebaseResult.Loading
        ) {
            Text(
                text = "Delete Account",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { authViewModel.signOut() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = authState !is FirebaseResult.Loading
        ) {
            if (authState is FirebaseResult.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmDialog = false
                        authViewModel.deleteAccount()
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    MaterialTheme {
        ProfileScreen(
            onLogout = {}
        )
    }
}