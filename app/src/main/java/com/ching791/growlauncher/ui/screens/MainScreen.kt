package com.ching791.growlauncher.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ching791.growlauncher.ui.components.AppTopBar
import com.ching791.growlauncher.ui.components.LaunchButton
import com.ching791.growlauncher.ui.components.MenuCard
import com.ching791.growlauncher.ui.components.UserInfoCard
import com.ching791.growlauncher.utils.AppLauncher
import com.ching791.growlauncher.utils.Constants
import com.ching791.growlauncher.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onOpenScripts: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenTheme: () -> Unit
) {
    val state = viewModel.state
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { AppTopBar(title = "Growlauncher") },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UserInfoCard(user = state.user)
            Text("Growtopia Version: ${state.config.growtopiaVersion}")
            Text("App Version: ${state.config.appVersion}")

            LaunchButton {
                val result = AppLauncher.launchGrowtopia(context)
                scope.launch {
                    val message = when (result) {
                        AppLauncher.LaunchResult.Launched -> "Launching Growtopia..."
                        AppLauncher.LaunchResult.NotInstalled -> "Growtopia is not installed."
                        is AppLauncher.LaunchResult.LaunchFailed -> result.message
                    }
                    snackbarHostState.showSnackbar(message)
                }
            }

            MenuCard(title = "Script Hub", onClick = onOpenScripts)
            Text("My Scripts")
            MenuCard(title = "Settings", onClick = onOpenSettings)
            MenuCard(title = "Theme Picker", onClick = onOpenTheme)
            MenuCard(title = "Discord") {
                runCatching {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.DISCORD_URL)))
                }.onFailure {
                    scope.launch { snackbarHostState.showSnackbar("Unable to open Discord link.") }
                }
            }
        }
    }
}
