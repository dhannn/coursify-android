package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.R
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.ui.theme.PrimaryColor
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.ui.theme.SecondaryColor
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.ui.theme.BackgroundColor
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.data.firebase.FirebaseResult
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.viewmodel.AuthViewModel

@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf<String?>(null) }

    // Collect auth state
    val authState by authViewModel.authState.collectAsState()

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (authState) {
            is FirebaseResult.Success -> {
                onSignupSuccess()
            }
            is FirebaseResult.Error -> {
                showError = (authState as FirebaseResult.Error).exception.message
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
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
            text = "Create an Account",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
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

        TextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                showError = null
            },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .width(304.dp)
                .height(60.3.dp)
                .scale(scaleY = 0.9F, scaleX = 1F),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(13.dp),
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Confirm Password") },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
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

        Button(
            onClick = {
                when {
                    email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                        showError = "Please fill in all fields"
                    }
                    password != confirmPassword -> {
                        showError = "Passwords do not match"
                    }
                    password.length < 6 -> {
                        showError = "Password must be at least 6 characters"
                    }
                    else -> {
                        authViewModel.signUp(email, password)
                    }
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
                Text("Create Account", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Already have an account? Login", color = PrimaryColor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    MaterialTheme {
        SignupScreen(
            onSignupSuccess = {},
            onBackToLogin = {}
        )
    }
}