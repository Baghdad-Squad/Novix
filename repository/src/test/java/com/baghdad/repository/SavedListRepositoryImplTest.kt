package com.baghdad.repository

import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SavedListRepositoryImplTest {
    private lateinit var remoteSource: RemoteSavedListDataSource
    private lateinit var repository: SavedListRepositoryImpl
    private lateinit var localSessionDataStore: LocalSessionDataStore

    val listId = 22L
    val movieId = 22002L
    val sessionId = "session_id"

    @BeforeEach
    fun setUp() {
        remoteSource = mockk(relaxed = true)
        localSessionDataStore = mockk(relaxed = true)
        repository = SavedListRepositoryImpl(remoteSource, localSessionDataStore)
    }

    @Test
    fun `should return success response when adding a movie to saved list`() = runTest {
        // Given
        coEvery { localSessionDataStore.getSessionId() } returns sessionId
        coEvery { remoteSource.addMovieToSavedList(listId, movieId, sessionId) } just Runs

        // When
        repository.addMovieToSavedList(listId, movieId)

        // Then
        coVerify { remoteSource.addMovieToSavedList(listId, movieId, sessionId) }
    }

    @Test
    fun `should return success response when adding a tv show to saved list`() = runTest {
        // Given
        coEvery { localSessionDataStore.getSessionId() } returns sessionId
        coEvery { remoteSource.addTvShowToSavedList(listId, movieId, sessionId) } just Runs

        // When
        repository.addTvShowToSavedList(listId, movieId)

        // Then
        coVerify { remoteSource.addTvShowToSavedList(listId, movieId, sessionId) }
    }

    @Test
    fun `should throw exception when api returns error while adding a movie to saved list`() =
        runTest {
            // Given
            coEvery { localSessionDataStore.getSessionId() } returns sessionId
            coEvery {
                remoteSource.addMovieToSavedList(listId, movieId, sessionId)
            } throws Exception()

            // When & Then
            assertThrows<Exception> { repository.addMovieToSavedList(listId, movieId) }

            coVerify { remoteSource.addMovieToSavedList(listId, movieId, sessionId) }
        }

    @Test
    fun `should throw exception when api returns error while adding a tv show to saved list`() =
        runTest {
            // Given
            coEvery { localSessionDataStore.getSessionId() } returns sessionId
            coEvery {
                remoteSource.addTvShowToSavedList(listId, movieId, sessionId)
            } throws Exception()

            // When & Then
            assertThrows<Exception> { repository.addTvShowToSavedList(listId, movieId) }

            coVerify { remoteSource.addTvShowToSavedList(listId, movieId, sessionId) }
        }
}
