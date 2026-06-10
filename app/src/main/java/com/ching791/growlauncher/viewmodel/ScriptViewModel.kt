package com.ching791.growlauncher.viewmodel

import androidx.lifecycle.ViewModel
import com.ching791.growlauncher.data.models.Script
import com.ching791.growlauncher.data.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ScriptViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _scripts = MutableStateFlow(loadScripts())
    val scripts: StateFlow<List<Script>> = _scripts.asStateFlow()

    fun addScript(name: String) {
        val normalizedName = name.trim()
        if (normalizedName.isBlank()) return
        _scripts.update { current ->
            if (current.any { it.name.equals(normalizedName, ignoreCase = true) }) {
                current
            } else {
                (current + Script(id = normalizedName, name = normalizedName)).also { persist(it) }
            }
        }
    }

    fun toggleScript(scriptId: String) {
        _scripts.update { current ->
            current.map { if (it.id == scriptId) it.copy(enabled = !it.enabled) else it }.also { persist(it) }
        }
    }

    fun deleteScript(scriptId: String) {
        _scripts.update { current -> current.filterNot { it.id == scriptId }.also { persist(it) } }
    }

    private fun loadScripts(): List<Script> =
        preferencesManager.getScriptEntries().map {
            Script(id = it.name, name = it.name, enabled = it.enabled)
        }

    private fun persist(scripts: List<Script>) {
        preferencesManager.saveScriptEntries(
            scripts.map { PreferencesManager.ScriptEntry(name = it.name, enabled = it.enabled) }.toSet()
        )
    }
}
