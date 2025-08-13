package com.baghdad.localDatasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.baghdad.repository.exception.DatabaseException
import com.baghdad.repository.logger.Logger
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class SessionDataSourceImplTest {
    private val mockDataStore: DataStore<Preferences> = mockk()
    private val mockLogger: Logger = mockk(relaxed = true)
    private val mockPreferences = mockk<Preferences>()
    private var dataStoreImpl: SessionDataSourceImpl =
        SessionDataSourceImpl(mockDataStore, mockLogger)

    @Test
    fun `saveSessionId should successfully store sessionId into DataStore when invoked`() =
        runTest {
            val sessionId = "abc123"
            coEvery {
                mockDataStore.updateData(any<suspend (Preferences) -> Preferences>())
            } returns mockPreferences

            dataStoreImpl.saveSessionId(sessionId)

            coVerify {
                mockDataStore.updateData(any<suspend (Preferences) -> Preferences>())
            }
        }

    @Test
    fun `getSessionId should retrieves existing session value from DataStore when it invoked`() =
        runTest {
            val expectedSessionId = "e343o53k52h"
            val mockPrefs: Preferences = mockk {
                every { get(stringPreferencesKey("sessionId")) } returns expectedSessionId
            }
            coEvery { mockDataStore.data } returns flowOf(mockPrefs)

            val result = dataStoreImpl.getSessionId()

            assertThat(result).isEqualTo(expectedSessionId)
        }

    @Test
    fun `getSessionId should returns null when sessionId is not set in DataStore`() = runTest {
        val mockPrefs: Preferences = mockk {
            every { get(stringPreferencesKey("sessionId")) } returns null
        }
        coEvery { mockDataStore.data } returns flowOf(mockPrefs)

        val result = dataStoreImpl.getSessionId()

        assertThat(result).isNull()
    }

    @Test
    fun `getSessionId should logs error when reading from DataStore fails`() = runTest {
        val expectedException = IOException("Read failed")
        coEvery { mockDataStore.data } returns flow { throw expectedException }

        assertThrows<Exception> {
            dataStoreImpl.getSessionId()
        }

        verify { mockLogger.logException(expectedException) }
    }

    @Test
    fun `deleteSessionId should throws DatabaseException when deletion fails`() = runTest {
        val deletionException = DatabaseException("Deletion failed")

        coEvery {
            mockDataStore.updateData(any<suspend (Preferences) -> Preferences>())
        } throws deletionException


        assertThrows<DatabaseException> {
            dataStoreImpl.deleteSessionId()
        }
    }

    @Test
    fun `deleteSessionId should successfully delete sessionId from DataStore when it invoked`() =
        runTest {
            coEvery {
                mockDataStore.updateData(any<suspend (Preferences) -> Preferences>())
            } returns mockPreferences

            dataStoreImpl.deleteSessionId()

            coVerify { mockDataStore.updateData(any<suspend (Preferences) -> Preferences>()) }
        }
}