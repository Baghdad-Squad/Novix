package com.baghdad.repository

import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource

class SavedListRepositoryImpl(
    private val remoteSavedListSource: RemoteSavedListDataSource,
) : SavedListRepository
