package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.logger.Logger

class RemoteSavedListDataSourceImpl(
    private val savedListApiService: SavedListApiService,
    private val logger: Logger,
) : RemoteSavedListDataSource
