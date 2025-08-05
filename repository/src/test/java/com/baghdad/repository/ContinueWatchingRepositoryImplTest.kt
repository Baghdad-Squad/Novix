package com.baghdad.repository

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.entity.User
import com.baghdad.repository.datasource.local.LocalContinueWatchingDataSource
import com.baghdad.repository.model.ContinueWatchingDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ContinueWatchingRepositoryImplTest {

    private lateinit var localContinueWatchingDataSource: LocalContinueWatchingDataSource
    private lateinit var continueWatchingRepositoryImpl: ContinueWatchingRepositoryImpl
    private lateinit var authenticationRepositoryImpl: AuthenticationRepositoryImpl

    @BeforeEach
    fun setUp() {
        localContinueWatchingDataSource = mockk()
        authenticationRepositoryImpl = mockk()
        continueWatchingRepositoryImpl = ContinueWatchingRepositoryImpl(
            localContinueWatchingDataSource = localContinueWatchingDataSource,
            authenticationRepository = authenticationRepositoryImpl
        )
    }

    @Test
    fun `getContinueWatching should return paged result when data source returns data`() = runTest {
        // Given
        val page = 1
        val pageSize = 10
        val mockContinueWatchingList = listOf(
            createMockContinueWatchingDto(1L, ContinueWatchingDto.ContentType.MOVIE),
            createMockContinueWatchingDto(2L, ContinueWatchingDto.ContentType.TV_SHOW),
            createMockContinueWatchingDto(3L, ContinueWatchingDto.ContentType.MOVIE)
        )
        val expectedResult = PagedResult(
            data = listOf(
                createMockContinueWatching(1L, ContinueWatching.ContentType.MOVIE),
                createMockContinueWatching(2L, ContinueWatching.ContentType.TV_SHOW),
                createMockContinueWatching(3L, ContinueWatching.ContentType.MOVIE)
            ),
            nextKey = null,
            prevKey = null
        )
        coEvery {
            localContinueWatchingDataSource.getContinueWatching(1, pageSize, page)
        } returns mockContinueWatchingList
        coEvery { authenticationRepositoryImpl.getLoggedInUser() } returns User(
            id = 1L,
            userName = "testuser",
            imageUrl = "https://example.com/avatar.jpg"
        )
        // When
        val result = continueWatchingRepositoryImpl.getContinueWatching(page, pageSize)
        // Then
        assertThat(expectedResult == result).isTrue()
        coVerify { localContinueWatchingDataSource.getContinueWatching(1, pageSize, page) }
    }

    @Test
    fun `getContinueWatching should return empty paged result when data source returns empty list`() =
        runTest {
            // Given
            val page = 1
            val pageSize = 10
            val expectedResult = PagedResult<ContinueWatching>(
                data = emptyList(),
                nextKey = null,
                prevKey = null
            )
            coEvery {
                localContinueWatchingDataSource.getContinueWatching(1, pageSize, page)
            } returns emptyList()
            coEvery { authenticationRepositoryImpl.getLoggedInUser() } returns createMockUser()
            // When
            val result = continueWatchingRepositoryImpl.getContinueWatching(page, pageSize)
            // Then
            assertThat(expectedResult == result).isTrue()
            coVerify { localContinueWatchingDataSource.getContinueWatching(1, pageSize, page) }
        }

    @Test
    fun `getContinueWatching should return paged result with next key when full page is returned`() =
        runTest {
            // Given
            val page = 1
            val pageSize = 2
            val mockContinueWatchingList = listOf(
                createMockContinueWatchingDto(1L, ContinueWatchingDto.ContentType.MOVIE),
                createMockContinueWatchingDto(2L, ContinueWatchingDto.ContentType.TV_SHOW)
            )

            val expectedResult = PagedResult(
                data = listOf(
                    createMockContinueWatching(1L, ContinueWatching.ContentType.MOVIE),
                    createMockContinueWatching(2L, ContinueWatching.ContentType.TV_SHOW)
                ),
                nextKey = 2,
                prevKey = null
            )

            coEvery {
                localContinueWatchingDataSource.getContinueWatching(1, pageSize, page)
            } returns mockContinueWatchingList
            coEvery { authenticationRepositoryImpl.getLoggedInUser() } returns createMockUser()

            // When
            val result = continueWatchingRepositoryImpl.getContinueWatching(page, pageSize)

            // Then
            assertThat(expectedResult == result).isTrue()
            coVerify { localContinueWatchingDataSource.getContinueWatching(1, pageSize, page) }
        }

    @Test
    fun `addContinueWatching should call data source when correct parameters for movie provided`() =
        runTest {
            // Given
            val contentId = 123L
            val genreIds = listOf(1L, 2L, 3L)
            val contentImageUrl = "https://example.com/image.jpg"
            val contentType = ContinueWatching.ContentType.MOVIE
            val expectedDto = ContinueWatchingDto(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = ContinueWatchingDto.ContentType.MOVIE,
                userId = 1L
            )

            coEvery { localContinueWatchingDataSource.addContinueWatching(expectedDto) } returns Unit
            coEvery { authenticationRepositoryImpl.getLoggedInUser() } returns createMockUser()
            // When
            continueWatchingRepositoryImpl.addContinueWatching(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = contentType
            )

            // Then
            coVerify { localContinueWatchingDataSource.addContinueWatching(expectedDto) }
        }

    @Test
    fun `addContinueWatching should call data source when correct parameters for tv show provided`() =
        runTest {
            // Given
            val contentId = 456L
            val genreIds = listOf(4L, 5L)
            val contentImageUrl = "https://example.com/tvshow.jpg"
            val contentType = ContinueWatching.ContentType.TV_SHOW
            val expectedDto = ContinueWatchingDto(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = ContinueWatchingDto.ContentType.TV_SHOW,
                userId = 1L
            )

            coEvery { localContinueWatchingDataSource.addContinueWatching(expectedDto) } returns Unit
            coEvery { authenticationRepositoryImpl.getLoggedInUser() } returns createMockUser()
            // When
            continueWatchingRepositoryImpl.addContinueWatching(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = contentType
            )

            // Then
            coVerify { localContinueWatchingDataSource.addContinueWatching(expectedDto) }
        }


    @Test
    fun `addContinueWatching should handle large genre list when large genre is provided`() =
        runTest {
            // Given
            val contentId = 999L
            val genreIds = (1L..20L).toList()
            val contentImageUrl = "https://example.com/large.jpg"
            val contentType = ContinueWatching.ContentType.TV_SHOW
            val expectedDto = ContinueWatchingDto(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = ContinueWatchingDto.ContentType.TV_SHOW,
                userId = 1L
            )

            coEvery { localContinueWatchingDataSource.addContinueWatching(expectedDto) } returns Unit
            coEvery { authenticationRepositoryImpl.getLoggedInUser() } returns createMockUser()
            // When
            continueWatchingRepositoryImpl.addContinueWatching(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = contentType
            )

            // Then
            coVerify { localContinueWatchingDataSource.addContinueWatching(expectedDto) }

        }

    companion object {
        private fun createMockContinueWatchingDto(
            contentId: Long,
            contentType: ContinueWatchingDto.ContentType
        ) = ContinueWatchingDto(
            contentId = contentId,
            genreIds = listOf(1L, 2L),
            contentImageUrl = "https://example.com/image$contentId.jpg",
            contentType = contentType,
            userId = 1L
        )

        private fun createMockUser() = User(
            id = 1L,
            userName = "testuser",
            imageUrl = "https://example.com/avatar.jpg"
        )

        private fun createMockContinueWatching(
            contentId: Long,
            contentType: ContinueWatching.ContentType
        ) = ContinueWatching(
            contentId = contentId,
            genreIds = listOf(1L, 2L),
            contentImageUrl = "https://example.com/image$contentId.jpg",
            contentType = contentType,
            userId = 1L
        )
    }
}