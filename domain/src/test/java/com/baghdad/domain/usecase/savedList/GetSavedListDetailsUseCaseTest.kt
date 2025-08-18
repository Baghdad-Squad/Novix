package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.exception.UnKnownNetworkException
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.domain.testHelper.getSampleSavedMovie
import com.baghdad.entity.savedList.SavedList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetSavedListDetailsUseCaseTest {
    private lateinit var savedListRepository: SavedListRepository
    private lateinit var getSavedListDetailsUseCase: GetSavedListDetailsUseCase
    private val page = 1
    private val pageSize = 20
    private val sampleSavedMovie = getSampleSavedMovie()

    @BeforeEach
    fun setUp() {
        savedListRepository = mockk(relaxed = true)
        getSavedListDetailsUseCase = GetSavedListDetailsUseCase(savedListRepository)
    }

    @Test
    fun `should return SavedListDetails when repository returns data successfully`() =
        runTest {
            // Given
            val listId = 1L
            val expected =
                SavedListDetails(
                    savedList = SavedList(1L, "My List", 2),
                    pagedItems =
                        PagedResult(
                            data =
                                listOf(
                                    sampleSavedMovie
                                ),
                            nextPage = null,
                            prevPage = 1,
                        ),
                )
            coEvery {
                savedListRepository.getSavedListDetails(
                    listId,
                    page,
                    pageSize,
                )
            } returns expected

            // When
            val result = getSavedListDetailsUseCase(listId, page, pageSize)

            // Then
            assertThat(result).isEqualTo(expected)
            coVerify(exactly = 1) {
                savedListRepository.getSavedListDetails(
                    listId,
                    page,
                    pageSize
                )
            }
        }

    @Test
    fun `should propagate exception when repository throws an exception`() =
        runTest {
            // Given
            val listId = 42L
            val exception = UnKnownNetworkException()
            coEvery {
                savedListRepository.getSavedListDetails(
                    listId,
                    page,
                    pageSize,
                )
            } throws exception

            // When
            val thrown =
                runCatching { getSavedListDetailsUseCase(listId, page, pageSize) }.exceptionOrNull()

            // Then
            assertThat(thrown).isInstanceOf(UnKnownNetworkException::class.java)
            coVerify(exactly = 1) {
                savedListRepository.getSavedListDetails(
                    listId,
                    page,
                    pageSize
                )
            }
        }
}
