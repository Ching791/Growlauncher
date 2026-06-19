package com.ching791.growlauncher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ching791.growlauncher.ui.screens.LoginScreen
import com.ching791.growlauncher.ui.screens.RegisterScreen
import com.ching791.growlauncher.ui.theme.GrowlauncherTheme
import com.ching791.growlauncher.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeState by authViewModel.themeState.collectAsStateWithLifecycle()
            val authState by authViewModel.authState.collectAsStateWithLifecycle()
            val error by authViewModel.errorMessage.observeAsState()

            LaunchedEffect(authState.isAuthenticated) {
                if (authState.isAuthenticated) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }

            GrowlauncherTheme(
                darkTheme = themeState.darkTheme,
                accent = themeState.accent
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    var isLoginMode by rememberSaveable { mutableStateOf(true) }
                    if (isLoginMode) {
                        LoginScreen(
                            isLoading = authState.isLoading,
                            error = error,
                            onLogin = authViewModel::login,
                            onSwitchToRegister = {
                                authViewModel.clearError()
                                isLoginMode = false
                            }
                        )
                    } else {
                        RegisterScreen(
                            isLoading = authState.isLoading,
                            error = error,
                            onRegister = authViewModel::register,
                            onSwitchToLogin = {
                                authViewModel.clearError()
                                isLoginMode = true
                            }
                        )
                    }
                }
            }
        }
    }
}
