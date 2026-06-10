package com.ching791.growlauncher.viewmodel

import androidx.lifecycle.ViewModel
import com.ching791.growlauncher.BuildConfig
import com.ching791.growlauncher.data.preferences.PreferencesManager
import com.ching791.growlauncher.ui.theme.ThemeAccent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SettingsUiState(
    val isDarkTheme: Boolean,
    val accent: ThemeAccent,
    val appVersion: String = "1.0.0"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    private val _state = MutableStateFlow(
        SettingsUiState(
            isDarkTheme = preferencesManager.isDarkTheme(),
            accent = preferencesManager.getAccentTheme(),
            appVersion = BuildConfig.VERSION_NAME
        )
    )
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    fun setDarkTheme(enabled: Boolean) {
        preferencesManager.saveDarkTheme(enabled)
        _state.update { it.copy(isDarkTheme = enabled) }
    }

    fun setAccent(accent: ThemeAccent) {
        preferencesManager.saveAccentTheme(accent)
        _state.update { it.copy(accent = accent) }
    }

    fun clearCache() {
        preferencesManager.saveScripts(emptySet())
    }
}
