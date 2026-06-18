package com.ching791.growlauncher.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ching791.growlauncher.ui.screens.MainScreen
import com.ching791.growlauncher.ui.screens.ScriptScreen
import com.ching791.growlauncher.ui.screens.SettingsScreen
import com.ching791.growlauncher.ui.screens.ThemeScreen
import com.ching791.growlauncher.viewmodel.MainViewModel
import com.ching791.growlauncher.viewmodel.ScriptViewModel
import com.ching791.growlauncher.viewmodel.SettingsViewModel

@Composable
fun GrowlauncherApp(
    settingsViewModel: SettingsViewModel,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val scriptViewModel: ScriptViewModel = hiltViewModel()

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = "main") {
            composable("main") {
                MainScreen(
                    viewModel = mainViewModel,
                    onOpenScripts = { navController.navigate("scripts") },
                    onOpenSettings = { navController.navigate("settings") },
                    onOpenTheme = { navController.navigate("theme") }
                )
            }
            composable("scripts") {
                ScriptScreen(viewModel = scriptViewModel, onBack = { navController.popBackStack() })
            }
            composable("settings") {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onBack = { navController.popBackStack() },
                    onLogout = onLogout
                )
            }
            composable("theme") {
                ThemeScreen(viewModel = settingsViewModel, onBack = { navController.popBackStack() })
            }
        }
    }
}
