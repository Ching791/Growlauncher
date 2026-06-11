package com.ching791.growlauncher

import com.ching791.growlauncher.data.preferences.PreferencesManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ScriptEntryCodecTest {
    @Test
    fun encodeDecode_roundTripsScriptEntry() {
        val encoded = PreferencesManager.encodeScriptEntry(
            PreferencesManager.ScriptEntry(name = "Auto Farm", enabled = true)
        )

        val decoded = PreferencesManager.decodeScriptEntry(encoded)

        requireNotNull(decoded)
        assertEquals("Auto Farm", decoded.name)
        assertEquals(true, decoded.enabled)
    }

    @Test
    fun decode_returnsNullForInvalidPayload() {
        assertNull(PreferencesManager.decodeScriptEntry("invalid"))
    }
}
