package com.ching791.growlauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

private val PurpleAccent = Color(0xFF9C4DFF)
private val ErrorRed = Color(0xFFFF6B6B)
private val SuccessGreen = Color(0xFF4CAF50)

@Composable
fun RegisterScreen(
    isLoginMode: Boolean,
    email: String,
    password: String,
    confirmPassword: String,
    isLoading: Boolean,
    error: String?,
    successMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegister: () -> Unit,
    onSwitchToLogin: () -> Unit,
    onToggleTab: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterModeTabs(isLoginMode = isLoginMode, onToggleTab = onToggleTab)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Create Account", color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email", color = Color.White) },
            singleLine = true,
            colors = fieldColors()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password", color = Color.White) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            colors = fieldColors()
        )
        Text(
            text = if (password.length >= 6) "Password strength: Good" else "Password strength: Minimum 6 characters",
            color = if (password.length >= 6) SuccessGreen else Color.White.copy(alpha = 0.8f),
            modifier = Modifier.align(Alignment.Start).padding(top = 6.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Confirm Password", color = Color.White) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            colors = fieldColors()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onRegister,
            enabled = !isLoading,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = PurpleAccent,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }
        if (isLoading) {
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator(color = PurpleAccent)
        }
        if (!error.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = error, color = ErrorRed)
        }
        if (!successMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = successMessage, color = SuccessGreen)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Already have an account? Login",
            color = PurpleAccent,
            modifier = Modifier.clickable(onClick = onSwitchToLogin)
        )
    }
}

@Composable
private fun RegisterModeTabs(
    isLoginMode: Boolean,
    onToggleTab: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = { onToggleTab(true) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isLoginMode) PurpleAccent else Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text("Login")
        }
        OutlinedButton(
            onClick = { onToggleTab(false) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (!isLoginMode) PurpleAccent else Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text("Register")
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PurpleAccent,
    unfocusedBorderColor = PurpleAccent.copy(alpha = 0.7f),
    cursorColor = PurpleAccent,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White
)
