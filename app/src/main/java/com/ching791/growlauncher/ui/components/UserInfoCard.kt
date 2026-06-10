package com.ching791.growlauncher.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ching791.growlauncher.data.models.User

@Composable
fun UserInfoCard(user: User) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Welcome, ${user.displayName}")
            Text(text = "Email: ${user.email}")
            Text(text = "Role: ${user.role}")
        }
    }
}
