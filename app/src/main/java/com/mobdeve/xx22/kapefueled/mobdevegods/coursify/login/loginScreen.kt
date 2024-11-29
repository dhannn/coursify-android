package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.R
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.ui.theme.*
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseResult
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf<String?>(null) }

    // Collect auth state
    val authState by authViewModel.authState.collectAsState()

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (authState) {
            is FirebaseResult.Success -> {
                onLoginSuccess()
            }
            is FirebaseResult.Error -> {
                showError = (authState as FirebaseResult.Error).exception.message
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier.size(217.dp)
            )

            Text(
                text = "Welcome Back!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(217.dp)
                    .height(50.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Show error if exists
            showError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    showError = null
                },
                label = { Text("Email") },
                modifier = Modifier
                    .width(304.dp)
                    .height(60.3.dp)
                    .scale(scaleY = 0.9F, scaleX = 1F),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(13.dp),
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Email") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SecondaryColor,
                    unfocusedContainerColor = SecondaryColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = PrimaryColor,
                    focusedTextColor = PrimaryColor,
                    unfocusedTextColor = PrimaryColor
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    showError = null
                },
                label = { Text("Password") },
                modifier = Modifier
                    .width(304.dp)
                    .height(60.3.dp)
                    .scale(scaleY = 0.9F, scaleX = 1F),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(13.dp),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SecondaryColor,
                    unfocusedContainerColor = SecondaryColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = PrimaryColor,
                    focusedTextColor = PrimaryColor,
                    unfocusedTextColor = PrimaryColor
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Create an Account",
                color = AccentColor1,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable(onClick = onCreateAccountClick)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        authViewModel.signIn(email, password)
                    } else {
                        showError = "Please fill in all fields"
                    }
                },
                modifier = Modifier
                    .width(304.dp)
                    .height(43.3.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(PrimaryColor),
                enabled = authState !is FirebaseResult.Loading
            ) {
                if (authState is FirebaseResult.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Login", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            onLoginSuccess = {},
            onCreateAccountClick = {}
        )
    }
}