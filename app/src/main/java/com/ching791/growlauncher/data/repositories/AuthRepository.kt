package com.ching791.growlauncher.data.repositories

import com.ching791.growlauncher.data.models.User
import com.ching791.growlauncher.data.preferences.PreferencesManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    suspend fun login(email: String, password: String): Result<User> = runCatching {
        // TODO: Restore Firebase sign-in when google-services.json is configured.
        val normalizedEmail = email.trim().lowercase()
        require(normalizedEmail.isNotBlank()) { "Email must not be empty" }
        require(password.isNotBlank()) { "Password must not be empty" }
        require(preferencesManager.userExists(normalizedEmail)) { "Account does not exist" }
        require(preferencesManager.getUserPassword(normalizedEmail) == password) { "Incorrect password" }

        preferencesManager.saveAuthToken(normalizedEmail)
        User(
            email = normalizedEmail,
            displayName = normalizedEmail.substringBefore('@'),
            role = preferencesManager.getRole()
        )
    }

    suspend fun register(email: String, password: String): Result<User> = runCatching {
        // TODO: Replace local account storage with Firebase Auth once enabled.
        val normalizedEmail = email.trim().lowercase()
        require(normalizedEmail.isNotBlank()) { "Email must not be empty" }
        require(password.isNotBlank()) { "Password must not be empty" }
        require(!preferencesManager.userExists(normalizedEmail)) { "Account already exists" }

        preferencesManager.saveUser(normalizedEmail, password)
        preferencesManager.saveAuthToken(normalizedEmail)
        User(
            email = normalizedEmail,
            displayName = normalizedEmail.substringBefore('@'),
            role = preferencesManager.getRole()
        )
    }

    fun currentUser(): User? {
        val email = preferencesManager.getAuthToken() ?: return null
        return User(
            email = email,
            displayName = email.substringBefore('@'),
            role = preferencesManager.getRole()
        )
    }

    fun isAuthenticated(): Boolean = !preferencesManager.getAuthToken().isNullOrBlank()
    fun logout() {
        preferencesManager.clearAuthToken()
    }
}
