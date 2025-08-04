package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.logger.Logger
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

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
}
