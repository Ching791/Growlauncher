package com.ching791.growlauncher

import com.ching791.growlauncher.viewmodel.AuthViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthValidationTest {
    @Test
    fun validEmail_returnsTrue() {
        assertTrue(AuthViewModel.isValidEmail("user@example.com"))
    }

    @Test
    fun invalidEmail_returnsFalse() {
        assertFalse(AuthViewModel.isValidEmail("invalid-email"))
    }

    @Test
    fun shortPassword_returnsFalse() {
        assertFalse(AuthViewModel.isValidPassword("12345"))
    }

    @Test
    fun minLengthPassword_returnsTrue() {
        assertTrue(AuthViewModel.isValidPassword("123456"))
    }
}
