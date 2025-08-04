package com.baghdad.repository

import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.exception.UnknownNetworkException
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.savedList.SavedListDetailsDto
import com.baghdad.repository.model.savedList.SavedListItemDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SavedListRepositoryImplTest {
    private lateinit var remoteSource: RemoteSavedListDataSource
    private lateinit var repository: SavedListRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteSource = mockk(relaxed = true)
        repository = SavedListRepositoryImpl(remoteSource)
    }

    @Test
    fun `getSavedListDetails should return mapped SavedListDetails from remote source when data is retrieved successfully`() =
        runTest {
            // Given
            val listId = 1L
            val dto =
                SavedListDetailsDto(
                    savedList = SavedListDto(1, "My Watchlist", 2),
                    listItems =
                        listOf(
                            SavedListItemDto(100, SavedListItemDto.Type.MOVIE, "Oppenheimer", "poster1"),
                            SavedListItemDto(101, SavedListItemDto.Type.TV_SHOW, "Dark", "poster2"),
                        ),
                )
            val expectedEntity = dto.toEntity()

            coEvery { remoteSource.getSavedListDetails(listId) } returns dto

            // When
            val result = repository.getSavedListDetails(listId)

            // Then
            assertThat(result).isEqualTo(expectedEntity)
            coVerify(exactly = 1) { remoteSource.getSavedListDetails(listId) }
        }

    @Test
    fun `getSavedListDetails throws when remote source throws`() =
        runTest {
            // Given
            val listId = 999L
            val exception = UnknownNetworkException()
            coEvery { remoteSource.getSavedListDetails(listId) } throws exception

            // When
            val thrown = runCatching { repository.getSavedListDetails(listId) }.exceptionOrNull()

            // Then
            assertThat(thrown).isInstanceOf(UnknownNetworkException::class.java)
        coVerify(exactly = 1) { remoteSource.getSavedListDetails(listId) }
    }
}
