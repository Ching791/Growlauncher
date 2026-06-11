package com.ching791.growlauncher

import com.ching791.growlauncher.data.preferences.PreferencesManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AccountEntryCodecTest {
    @Test
    fun encodeDecode_roundTripsAccountEntry() {
        val encoded = PreferencesManager.encodeAccountEntry(
            PreferencesManager.AccountEntry(email = "user@example.com", password = "secret123")
        )

        val decoded = PreferencesManager.decodeAccountEntry(encoded)

        requireNotNull(decoded)
        assertEquals("user@example.com", decoded.email)
        assertEquals("secret123", decoded.password)
    }

    @Test
    fun decode_returnsNullForBlankEmail() {
        assertNull(PreferencesManager.decodeAccountEntry("||password"))
    }
}
