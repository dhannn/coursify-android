package com.mobdeve.xx22.gilo.joshua.myapplication.signup
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.mobdeve.xx22.gilo.joshua.myapplication.R
import com.mobdeve.xx22.gilo.joshua.myapplication.ui.theme.Smoke


@Composable
fun SignupScreen(
    onSignupComplete: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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
            text = "Create an Account",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))  // Changed from 20.dp to 16.dp to match login

        // Email field
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .width(304.dp)
                .height(60.3.dp)
                .scale(scaleY = 0.9F, scaleX = 1F),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(13.dp),
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "E-mail") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Smoke,
                unfocusedContainerColor = Smoke,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(20.dp))  // Already matches login

        // Password field
        TextField(
            value = password,
            onValueChange = { password = it },
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
                focusedContainerColor = Smoke,
                unfocusedContainerColor = Smoke,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(20.dp))  // Already matches login

        Button(
            onClick = { onSignupComplete(email, password) },
            modifier = Modifier
                .width(304.dp)
                .height(43.3.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text("Create Account")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    MaterialTheme {
        SignupScreen(
            onSignupComplete = { _, _ -> /* Do nothing for preview */ }
        )
    }
}
