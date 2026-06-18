package com.ching791.growlauncher.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ching791.growlauncher.BuildConfig
import com.ching791.growlauncher.data.models.Config
import com.ching791.growlauncher.data.models.User
import com.ching791.growlauncher.data.preferences.PreferencesManager
import com.ching791.growlauncher.data.repositories.AuthRepository
import com.ching791.growlauncher.utils.getPackageVersion
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class MainUiState(
    val user: User = User("user@growlauncher.app"),
    val config: Config
)

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository,
    preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val state = MainUiState(
        user = authRepository.currentUser() ?: User("user@growlauncher.app", role = preferencesManager.getRole()),
        config = Config(
            appVersion = BuildConfig.VERSION_NAME,
            growtopiaVersion = context.getPackageVersion("com.rtsoft.growtopia")
        )
    )
}
