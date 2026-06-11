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
        val normalizedEmail = email.trim().lowercase()
        require(isValidEmail(normalizedEmail)) { "Please enter a valid email address" }
        require(password.isNotBlank()) { "Password must not be empty" }
        require(preferencesManager.validateAccount(normalizedEmail, password)) {
            "Invalid email or password"
        }
        preferencesManager.saveAuthToken(normalizedEmail)
        val user = User(
            email = normalizedEmail,
            displayName = normalizedEmail.substringBefore('@'),
            role = preferencesManager.getRole()
        )
        user
    }

    suspend fun register(email: String, password: String): Result<User> = runCatching {
        val normalizedEmail = email.trim().lowercase()
        require(isValidEmail(normalizedEmail)) { "Please enter a valid email address" }
        require(password.length >= MIN_PASSWORD_LENGTH) { "Password must be at least 6 characters" }
        require(!preferencesManager.hasAccount(normalizedEmail)) { "Account already exists" }
        preferencesManager.saveAccount(normalizedEmail, password)
        preferencesManager.saveAuthToken(normalizedEmail)
        User(
            email = normalizedEmail,
            displayName = normalizedEmail.substringBefore('@'),
            role = preferencesManager.getRole()
        )
    }

    fun currentUser(): User? {
        val email = preferencesManager.getAuthToken()?.takeIf { it.contains("@") } ?: return null
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

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6

        fun isValidEmail(email: String): Boolean =
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
