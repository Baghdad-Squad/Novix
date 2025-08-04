package com.baghdad.repository

import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

class SavedListRepositoryImplTest {
    private lateinit var remoteSource: RemoteSavedListDataSource
    private lateinit var repository: SavedListRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteSource = mockk(relaxed = true)
        repository = SavedListRepositoryImpl(remoteSource)
    }
}
