package com.ching791.growlauncher

import com.ching791.growlauncher.viewmodel.AuthUiState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthUiStateTest {
    @Test
    fun defaultAuthState_isUnauthenticatedAndNotLoading() {
        val state = AuthUiState()
        assertFalse(state.isAuthenticated)
        assertFalse(state.isLoading)
    }

    @Test
    fun authenticatedState_setsAuthenticated() {
        val state = AuthUiState(isAuthenticated = true)
        assertTrue(state.isAuthenticated)
    }
}
