package com.ching791.growlauncher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ching791.growlauncher.ui.GrowlauncherApp
import com.ching791.growlauncher.ui.theme.GrowlauncherTheme
import com.ching791.growlauncher.viewmodel.AuthViewModel
import com.ching791.growlauncher.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authToken = getSharedPreferences("auth_pref", MODE_PRIVATE).getString("auth_token", null)
        if (authToken.isNullOrBlank()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        setContent {
            val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()
            GrowlauncherTheme(darkTheme = settingsState.isDarkTheme, accent = settingsState.accent) {
                GrowlauncherApp(settingsViewModel = settingsViewModel, onLogout = {
                    authViewModel.logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                })
            }
        }
    }
}
