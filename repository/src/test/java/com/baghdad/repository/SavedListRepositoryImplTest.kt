package com.baghdad.repository

import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SavedListRepositoryImplTest {
    private lateinit var remoteSource: RemoteSavedListDataSource
    private lateinit var repository: SavedListRepositoryImpl
    private lateinit var localSessionDataStore: LocalSessionDataStore

    @BeforeEach
    fun setUp() {
        remoteSource = mockk(relaxed = true)
        localSessionDataStore = mockk(relaxed = true)
        repository = SavedListRepositoryImpl(
            remoteSavedListSource = remoteSource,
            localSessionDataStore = localSessionDataStore
        )
    }

    val title = "Favorite"
    val sessionId = "123"

    @Test
    fun `should createSavedList call remote source with correct session ID`() = runTest {
        // Given
        coEvery { localSessionDataStore.getSessionId() } returns sessionId
        coEvery { remoteSource.createSavedList(title, sessionId) } returns Unit

        // When
        repository.createSavedList(title)

        // Then
        coVerify(exactly = 1) { localSessionDataStore.getSessionId() }
        coVerify(exactly = 1) { remoteSource.createSavedList(title, sessionId) }
    }

    @Test
    fun `should createSavedList not crash when session ID is null`() = runBlocking {
        // Given
        val title = "Empty Session Test"
        coEvery { localSessionDataStore.getSessionId() } returns null
        coEvery { remoteSource.createSavedList(title, any()) } returns Unit

        // When
        repository.createSavedList(title)

        // Then
        coVerify { localSessionDataStore.getSessionId() }
    }

    @Test
    fun `createSavedList() should propagate exception when remote source fails`() = runTest {
        // Given
        val exception = RuntimeException("Network failure")

        coEvery { localSessionDataStore.getSessionId() } returns sessionId
        coEvery { remoteSource.createSavedList(title, sessionId) } throws exception

        // When
        val resultException = runCatching { repository.createSavedList(title) }.exceptionOrNull()

        // Then
        coVerify(exactly = 1) { localSessionDataStore.getSessionId() }
        assertThat(resultException).isNotNull()
    }



}
