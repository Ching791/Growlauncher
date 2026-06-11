package com.ching791.growlauncher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ching791.growlauncher.data.repositories.AuthRepository
import com.ching791.growlauncher.data.preferences.PreferencesManager
import com.ching791.growlauncher.ui.theme.ThemeAccent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false
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

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) }
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                _authState.update { AuthUiState(isAuthenticated = true, isLoading = false) }
                _errorMessage.value = null
            } else {
                _authState.update { it.copy(isLoading = false) }
                _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Login failed"
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _authState.value = AuthUiState(isAuthenticated = false)
    }
}
