package com.ching791.growlauncher.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ching791.growlauncher.ui.components.AppTopBar
import com.ching791.growlauncher.ui.theme.ThemeAccent
import com.ching791.growlauncher.viewmodel.SettingsViewModel

@Composable
fun ThemeScreen(viewModel: SettingsViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(topBar = { AppTopBar(title = "Theme Picker", onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Dark Mode")
            Switch(checked = state.isDarkTheme, onCheckedChange = viewModel::setDarkTheme)
            Text("Accent Color")
            ThemeAccent.entries.forEach { accent ->
                FilterChip(
                    selected = state.accent == accent,
                    onClick = { viewModel.setAccent(accent) },
                    label = { Text(accent.name.lowercase().replaceFirstChar { it.uppercase() }) }
                )
            }
        }
    }
}
