package com.ching791.growlauncher.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.ching791.growlauncher.ui.theme.ThemeAccent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val authPrefs: SharedPreferences = context.getSharedPreferences("auth_pref", Context.MODE_PRIVATE)
    private val userPrefs: SharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
    private val scriptPrefs: SharedPreferences = context.getSharedPreferences("script_pref", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        authPrefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    fun getAuthToken(): String? = authPrefs.getString(KEY_AUTH_TOKEN, null)

    fun clearAuthToken() {
        authPrefs.edit().remove(KEY_AUTH_TOKEN).apply()
    }

    fun saveAccount(email: String, password: String) {
        val account = AccountEntry(email = email.trim().lowercase(), password = password)
        val updatedAccounts = getStoredAccounts().toMutableSet().apply {
            removeAll { decodeAccountEntry(it)?.email == account.email }
            add(encodeAccountEntry(account))
        }
        userPrefs.edit().putStringSet(KEY_ACCOUNTS, updatedAccounts).apply()
    }

    fun hasAccount(email: String): Boolean =
        getStoredAccounts().any { decodeAccountEntry(it)?.email == email.trim().lowercase() }

    fun validateAccount(email: String, password: String): Boolean =
        getStoredAccounts().any {
            val account = decodeAccountEntry(it)
            account?.email == email.trim().lowercase() && account.password == password
        }

    fun saveDarkTheme(enabled: Boolean) {
        userPrefs.edit().putBoolean(KEY_DARK_THEME, enabled).apply()
    }

    fun isDarkTheme(): Boolean = userPrefs.getBoolean(KEY_DARK_THEME, true)

    fun saveAccentTheme(accent: ThemeAccent) {
        userPrefs.edit().putString(KEY_ACCENT_THEME, accent.name).apply()
    }

    fun getAccentTheme(): ThemeAccent {
        val name = userPrefs.getString(KEY_ACCENT_THEME, ThemeAccent.PURPLE.name) ?: ThemeAccent.PURPLE.name
        return ThemeAccent.entries.firstOrNull { it.name == name } ?: ThemeAccent.PURPLE
    }

    fun saveRole(role: String) {
        userPrefs.edit().putString(KEY_ROLE, role).apply()
    }

    fun getRole(): String = userPrefs.getString(KEY_ROLE, "Member") ?: "Member"

    fun saveScripts(scripts: Set<String>) {
        scriptPrefs.edit().putStringSet(KEY_SCRIPTS, scripts).apply()
    }

    fun getScripts(): Set<String> = scriptPrefs.getStringSet(KEY_SCRIPTS, emptySet()) ?: emptySet()

    fun saveScriptEntries(entries: Set<ScriptEntry>) {
        saveScripts(entries.map(::encodeScriptEntry).toSet())
    }

    fun getScriptEntries(): List<ScriptEntry> =
        getScripts().mapNotNull(::decodeScriptEntry).sortedBy { it.name }

    data class ScriptEntry(
        val name: String,
        val enabled: Boolean
    )

    data class AccountEntry(
        val email: String,
        val password: String
    )

    companion object {
        private const val SCRIPT_SEPARATOR = "||"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_ACCENT_THEME = "accent_theme"
        private const val KEY_ROLE = "user_role"
        private const val KEY_SCRIPTS = "scripts"
        private const val KEY_ACCOUNTS = "accounts"

        fun encodeScriptEntry(entry: ScriptEntry): String =
            "${entry.name}$SCRIPT_SEPARATOR${entry.enabled}"

        fun decodeScriptEntry(value: String): ScriptEntry? {
            val parts = value.split(SCRIPT_SEPARATOR, limit = 2)
            if (parts.size != 2) return null
            return ScriptEntry(
                name = parts[0],
                enabled = parts[1].toBooleanStrictOrNull() ?: false
            )
        }

        fun encodeAccountEntry(entry: AccountEntry): String =
            "${entry.email}$SCRIPT_SEPARATOR${entry.password}"

        fun decodeAccountEntry(value: String): AccountEntry? {
            val parts = value.split(SCRIPT_SEPARATOR, limit = 2)
            if (parts.size != 2 || parts[0].isBlank()) return null
            return AccountEntry(
                email = parts[0],
                password = parts[1]
            )
        }
    }

    private fun getStoredAccounts(): Set<String> =
        userPrefs.getStringSet(KEY_ACCOUNTS, emptySet()) ?: emptySet()
}
