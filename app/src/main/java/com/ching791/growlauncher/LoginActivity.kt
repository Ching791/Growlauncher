package com.ching791.growlauncher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
                if (authState.isLoginMode) {
                    LoginScreen(
                        isLoginMode = authState.isLoginMode,
                        email = authState.email,
                        password = authState.password,
                        isLoading = authState.isLoading,
                        error = authState.errorMessage,
                        successMessage = authState.successMessage,
                        onEmailChange = authViewModel::updateEmail,
                        onPasswordChange = authViewModel::updatePassword,
                        onLogin = authViewModel::login,
                        onSwitchToRegister = authViewModel::toggleAuthMode,
                        onToggleTab = { isLogin ->
                            if (!isLogin) authViewModel.toggleAuthMode()
                        }
                    )
                } else {
                    RegisterScreen(
                        isLoginMode = authState.isLoginMode,
                        email = authState.email,
                        password = authState.password,
                        confirmPassword = authState.confirmPassword,
                        isLoading = authState.isLoading,
                        error = authState.errorMessage,
                        successMessage = authState.successMessage,
                        onEmailChange = authViewModel::updateEmail,
                        onPasswordChange = authViewModel::updatePassword,
                        onConfirmPasswordChange = authViewModel::updateConfirmPassword,
                        onRegister = authViewModel::register,
                        onSwitchToLogin = authViewModel::toggleAuthMode,
                        onToggleTab = { isLogin ->
                            if (isLogin) authViewModel.toggleAuthMode()
                        }
                    )
                }
            }
        }
    }
}
