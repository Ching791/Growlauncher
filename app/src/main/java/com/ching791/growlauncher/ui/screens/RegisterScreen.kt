package com.ching791.growlauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    isLoading: Boolean,
    error: String?,
    onRegister: (String, String) -> Unit,
    onSwitchToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }
    val passwordStrength = when {
        password.length >= 10 -> "Strong"
        password.length >= 6 -> "Medium"
        else -> "Weak"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthModeHeader(
            isLoginSelected = false,
            onSelectLogin = onSwitchToLogin,
            onSelectRegister = {}
        )
        Text(
            text = "Create Account",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.White) },
            singleLine = true,
            colors = authTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.White) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = authTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password", color = Color.White) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = authTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
        Text(
            text = "Password strength: $passwordStrength (minimum 6 characters)",
            color = when (passwordStrength) {
                "Strong" -> Color(0xFF4CAF50)
                "Medium" -> Color(0xFFFFC107)
                else -> Color(0xFFFF6B6B)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
        Button(
            onClick = {
                when {
                    password != confirmPassword -> {
                        localError = "Passwords do not match"
                    }
                    else -> {
                        localError = null
                        onRegister(email, password)
                    }
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF9C27B0), Color(0xFF673AB7))
                    )
                )
                .height(50.dp)
        ) {
            Text("Create Account")
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp), color = Color(0xFFB388FF))
        }
        val displayError = localError ?: error
        if (!displayError.isNullOrBlank()) {
            Text(
                text = displayError,
                color = Color(0xFFFF6B6B),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
        Text(
            text = "Already have an account? Login",
            color = Color(0xFFB388FF),
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable(enabled = !isLoading, onClick = onSwitchToLogin)
        )
    }
}
