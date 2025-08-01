package com.baghdad.local_datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.baghdad.local_datasource.dataStore.session.LocalSessionDataStoreImpl
import com.baghdad.repository.logger.Logger
import io.mockk.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class LocalSessionDataStoreImplTest {

    private lateinit var dataStoreImpl: LocalSessionDataStoreImpl
    private val mockDataStore: DataStore<Preferences> = mockk()
    private val mockLogger: Logger = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        dataStoreImpl = LocalSessionDataStoreImpl(mockDataStore, mockLogger)
    }

    @Test
    fun `saveSessionId throws exception when storage fails`() = runTest {
        // Given
        val sessionId = "test-session-123"

        // When & Then
        assertThrows<Exception> {
            dataStoreImpl.saveSessionId(sessionId)
        }
    }

    @Test
    fun `getSessionId retrieves existing session value from DataStore`() = runTest {
        // Given
        val expectedSessionId = "test-session-456"
        val mockPrefs: Preferences = mockk {
            every { get(stringPreferencesKey("sessionId")) } returns expectedSessionId
        }
        // When
        coEvery { mockDataStore.data } returns flowOf(mockPrefs)
    }

    @Test
    fun `getSessionId returns null when sessionId is not set in DataStore`() = runTest {
        // Given
        val mockPrefs: Preferences = mockk {
            every { get(stringPreferencesKey("sessionId")) } returns null
        }
        // When
        coEvery { mockDataStore.data } returns flowOf(mockPrefs)
    }

    @Test
    fun `getSessionId logs error when reading from DataStore fails`() = runTest {
        // Given
        val expectedException = IOException("Read failed")
        coEvery { mockDataStore.data } returns flow { throw expectedException }

        // When & Then
        assertThrows<Exception> {
            dataStoreImpl.getSessionId()
        }
        verify { mockLogger.logException(expectedException) }
    }

    @Test
    fun `deleteSessionId throws exception when deletion fails`() = runTest {
        // When & Then
        assertThrows<Exception> {
            dataStoreImpl.deleteSessionId()
        }
    }
}