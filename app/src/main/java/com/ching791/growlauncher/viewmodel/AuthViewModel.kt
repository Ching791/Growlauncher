package com.ching791.growlauncher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ching791.growlauncher.data.preferences.PreferencesManager
import com.ching791.growlauncher.data.repositories.AuthRepository
import com.ching791.growlauncher.ui.theme.ThemeAccent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val isLoginMode: Boolean = true,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null
)

data class ThemeState(
    val darkTheme: Boolean = true,
    val accent: ThemeAccent = ThemeAccent.PURPLE
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    preferencesManager: PreferencesManager
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthUiState(isAuthenticated = authRepository.isAuthenticated()))
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    private val _themeState = MutableStateFlow(
        ThemeState(
            darkTheme = preferencesManager.isDarkTheme(),
            accent = preferencesManager.getAccentTheme()
        )
    )
    val themeState: StateFlow<ThemeState> = _themeState.asStateFlow()

    fun toggleAuthMode() {
        _authState.update {
            it.copy(
                isLoginMode = !it.isLoginMode,
                errorMessage = null,
                successMessage = null,
                password = "",
                confirmPassword = ""
            )
        }
    }

    fun updateEmail(value: String) {
        _authState.update { it.copy(email = value, errorMessage = null, successMessage = null) }
    }

    fun updatePassword(value: String) {
        _authState.update { it.copy(password = value, errorMessage = null, successMessage = null) }
    }

    fun updateConfirmPassword(value: String) {
        _authState.update { it.copy(confirmPassword = value, errorMessage = null, successMessage = null) }
    }

    fun validateEmail(email: String): Boolean = isValidEmail(email)

    fun validatePassword(password: String): Boolean = isValidPassword(password)

    fun login() {
        val email = authState.value.email.trim()
        val password = authState.value.password

        if (!validateEmail(email)) {
            _authState.update { it.copy(errorMessage = "Please enter a valid email address") }
            return
        }
        if (!validatePassword(password)) {
            _authState.update { it.copy(errorMessage = "Password must be at least 6 characters") }
            return
        }

        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            delay(300)
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                _authState.update {
                    it.copy(
                        isAuthenticated = true,
                        isLoading = false,
                        successMessage = "Login successful"
                    )
                }
            } else {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Login failed"
                    )
                }
            }
        }
    }

    fun register() {
        val email = authState.value.email.trim()
        val password = authState.value.password
        val confirmPassword = authState.value.confirmPassword

        if (!validateEmail(email)) {
            _authState.update { it.copy(errorMessage = "Please enter a valid email address") }
            return
        }
        if (!validatePassword(password)) {
            _authState.update { it.copy(errorMessage = "Password must be at least 6 characters") }
            return
        }
        if (password != confirmPassword) {
            _authState.update { it.copy(errorMessage = "Passwords do not match") }
            return
        }

        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            delay(300)
            val result = authRepository.register(email, password)
            if (result.isSuccess) {
                _authState.update {
                    it.copy(
                        isAuthenticated = true,
                        isLoading = false,
                        successMessage = "Account created successfully"
                    )
                }
            } else {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Registration failed"
                    )
                }
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _authState.value = AuthUiState(isAuthenticated = false)
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
        private val EMAIL_REGEX =
            Regex(pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

        fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email.trim())

        fun isValidPassword(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH
    }
}
