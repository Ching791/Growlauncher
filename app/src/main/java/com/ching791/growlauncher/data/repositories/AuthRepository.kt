package com.ching791.growlauncher.data.repositories

import com.ching791.growlauncher.data.models.User
import com.ching791.growlauncher.data.preferences.PreferencesManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val preferencesManager: PreferencesManager
) {
    suspend fun login(email: String, password: String): Result<User> = runCatching {
        require(email.isNotBlank()) { "Email must not be empty" }
        require(password.isNotBlank()) { "Password must not be empty" }

        val authResult = firebaseAuth.signInWithEmailAndPassword(email.trim(), password).await()
        val token = authResult.user?.uid ?: error("Authentication failed")
        preferencesManager.saveAuthToken(token)
        val user = User(
            email = authResult.user?.email ?: email.trim(),
            displayName = authResult.user?.displayName ?: email.substringBefore('@'),
            role = preferencesManager.getRole()
        )
        user
    }

    fun currentUser(): User? {
        val user = firebaseAuth.currentUser ?: return null
        return User(
            email = user.email ?: "user@growlauncher.app",
            displayName = user.displayName ?: (user.email ?: "User").substringBefore('@'),
            role = preferencesManager.getRole()
        )
    }

    fun isAuthenticated(): Boolean = !preferencesManager.getAuthToken().isNullOrBlank()

    fun logout() {
        firebaseAuth.signOut()
        preferencesManager.clearAuthToken()
    }
}
