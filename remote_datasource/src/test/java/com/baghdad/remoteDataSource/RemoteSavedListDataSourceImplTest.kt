package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.logger.Logger
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoteSavedListDataSourceImplTest {
    private lateinit var savedListApiService: SavedListApiService
    private lateinit var logger: Logger
    private lateinit var remoteSource: RemoteSavedListDataSource

    @BeforeEach
    fun setUp() {
        savedListApiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        remoteSource = RemoteSavedListDataSourceImpl(savedListApiService, logger)
    }

    @Test
    fun `should add movie to saved list when addMovieToSavedList is called`() = runTest {
        // Given
        val listId = 123L
        val movieId = 2222002L
        val sessionId = "session_id"
        coEvery { logger.logException(any()) } returns Unit


        // When
        remoteSource.addMovieToSavedList(listId, movieId, sessionId)

        // Then
        coVerify { savedListApiService.addItemToSavedList(listId, any(), sessionId) }
    }

    @Test
    fun `should add tv show to saved list when addTvShowToSavedList is called`() = runTest {
        // Given
        val listId = 123L
        val tvShowId = 2222002L
        val sessionId = "session_id"
        coEvery {
            savedListApiService.addItemToSavedList(listId, any(), sessionId)
        }

        // When
        remoteSource.addTvShowToSavedList(listId, tvShowId, sessionId)

        // Then
        coVerify { savedListApiService.addItemToSavedList(listId, any(), sessionId) }
    }

}
