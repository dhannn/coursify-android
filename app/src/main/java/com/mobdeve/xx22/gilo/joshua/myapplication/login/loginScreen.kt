package com.mobdeve.xx22.gilo.joshua.myapplication.login
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.res.painterResource
import com.mobdeve.xx22.gilo.joshua.myapplication.R


@Composable
fun LoginScreen(onLoginClick: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(R.drawable.logo), contentDescription = "logo")
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .width(304.dp)
                .height(43.3.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(13.dp),
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "E-mail")}
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .width(304.dp)
                .height(43.3.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(13.dp),
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") },
            trailingIcon = { Icon(Icons.Filled.Visibility, contentDescription = null)},

        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "No account yet? Click here.",
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onLoginClick(email, password) },
            modifier = Modifier.width(304.dp).height(43.3.dp),
            shape = RoundedCornerShape(13.dp)
        ) {
            Text("Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen { email, password ->
    }
}


