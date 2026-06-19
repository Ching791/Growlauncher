package com.ching791.growlauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
fun LoginScreen(
    isLoading: Boolean,
    error: String?,
    onLogin: (String, String) -> Unit,
    onSwitchToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthModeHeader(isLoginSelected = true, onSelectLogin = {}, onSelectRegister = onSwitchToRegister)
        Text(
            text = "Growlauncher Login",
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
            colors = authTextFieldColors(),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
        Button(
            onClick = { onLogin(email, password) },
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
            Text("Login")
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp), color = Color(0xFFB388FF))
        }
        if (!error.isNullOrBlank()) {
            Text(
                text = error,
                color = Color(0xFFFF6B6B),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
        Text(
            text = "Don't have an account? Register",
            color = Color(0xFFB388FF),
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable(enabled = !isLoading, onClick = onSwitchToRegister)
        )
    }
}

@Composable
fun AuthModeHeader(
    isLoginSelected: Boolean,
    onSelectLogin: () -> Unit,
    onSelectRegister: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AuthModeButton(
            text = "Login",
            selected = isLoginSelected,
            onClick = onSelectLogin,
            modifier = Modifier.weight(1f)
        )
        AuthModeButton(
            text = "Register",
            selected = !isLoginSelected,
            onClick = onSelectRegister,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AuthModeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) Color(0xFFB388FF) else Color(0xFF555555)
    Text(
        text = text,
        color = Color.White,
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
    )
}

@Composable
fun authTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFFB388FF),
    unfocusedBorderColor = Color(0xFF777777),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color(0xFFB388FF)
)
