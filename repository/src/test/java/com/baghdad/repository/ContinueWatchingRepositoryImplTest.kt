package com.baghdad.repository

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.entity.user.User
import com.baghdad.repository.datasource.local.LocalUserWatchedMediaDataSource
import com.baghdad.repository.model.UserWatchedMediaDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ContinueWatchingRepositoryImplTest {

    private lateinit var localUserWatchedMediaDataSource: LocalUserWatchedMediaDataSource
    private lateinit var continueWatchingRepositoryImpl: UserWatchedMediaRepositoryImpl
    private lateinit var authenticationRepositoryImpl: AuthenticationRepositoryImpl

    @BeforeEach
    fun setUp() {
        localUserWatchedMediaDataSource = mockk()
        authenticationRepositoryImpl = mockk()
        continueWatchingRepositoryImpl = UserWatchedMediaRepositoryImpl(
            localUserWatchedMediaDataSource = localUserWatchedMediaDataSource,
            authenticationRepository = authenticationRepositoryImpl
        )
    }

    @Test
    fun `getContinueWatching should return paged result when data source returns data`() = runTest {
        // Given
        val page = 1
        val pageSize = 10
        val mockContinueWatchingList = listOf(
            createMockContinueWatchingDto(1L, UserWatchedMediaDto.ContentType.MOVIE),
            createMockContinueWatchingDto(2L, UserWatchedMediaDto.ContentType.TV_SHOW),
            createMockContinueWatchingDto(3L, UserWatchedMediaDto.ContentType.MOVIE)
        )
        val expectedResult = PagedResult(
            data = listOf(
                createMockContinueWatching(1L, UserWatchedMedia.ContentType.MOVIE),
                createMockContinueWatching(2L, UserWatchedMedia.ContentType.TV_SHOW),
                createMockContinueWatching(3L, UserWatchedMedia.ContentType.MOVIE)
            ),
            nextKey = null,
            prevKey = null
        )
        coEvery {
            localUserWatchedMediaDataSource.getContinueWatching(1, pageSize, page)
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
        coVerify { localUserWatchedMediaDataSource.getContinueWatching(1, pageSize, page) }
    }

    @Test
    fun `getContinueWatching should return empty paged result when data source returns empty list`() =
        runTest {
            // Given
            val page = 1
            val pageSize = 10
            val expectedResult = PagedResult<UserWatchedMedia>(
                data = emptyList(),
                nextKey = null,
                prevKey = null
            )
            coEvery {
                localUserWatchedMediaDataSource.getContinueWatching(1, pageSize, page)
            } returns emptyList()
            coEvery { authenticationRepositoryImpl.getLoggedInUser() } returns createMockUser()
            // When
            val result = continueWatchingRepositoryImpl.getContinueWatching(page, pageSize)
            // Then
            assertThat(expectedResult == result).isTrue()
            coVerify { localUserWatchedMediaDataSource.getContinueWatching(1, pageSize, page) }
        }

    @Test
    fun `getContinueWatching should return paged result with next key when full page is returned`() =
        runTest {
            // Given
            val page = 1
            val pageSize = 2
            val mockContinueWatchingList = listOf(
                createMockContinueWatchingDto(1L, UserWatchedMediaDto.ContentType.MOVIE),
                createMockContinueWatchingDto(2L, UserWatchedMediaDto.ContentType.TV_SHOW)
            )

            val expectedResult = PagedResult(
                data = listOf(
                    createMockContinueWatching(1L, UserWatchedMedia.ContentType.MOVIE),
                    createMockContinueWatching(2L, UserWatchedMedia.ContentType.TV_SHOW)
                ),
                nextKey = 2,
                prevKey = null
            )

            coEvery {
                localUserWatchedMediaDataSource.getContinueWatching(1, pageSize, page)
            } returns mockContinueWatchingList
            coEvery { authenticationRepositoryImpl.getLoggedInUser() } returns createMockUser()

            // When
            val result = continueWatchingRepositoryImpl.getContinueWatching(page, pageSize)

            // Then
            assertThat(expectedResult == result).isTrue()
            coVerify { localUserWatchedMediaDataSource.getContinueWatching(1, pageSize, page) }
        }

    @Test
    fun `addContinueWatching should call data source when correct parameters for movie provided`() =
        runTest {
            // Given
            val contentId = 123L
            val genreIds = listOf(1L, 2L, 3L)
            val contentImageUrl = "https://example.com/image.jpg"
            val contentType = UserWatchedMedia.ContentType.MOVIE
            val expectedDto = UserWatchedMediaDto(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = UserWatchedMediaDto.ContentType.MOVIE,
                userId = 1L
            )

            coEvery { localUserWatchedMediaDataSource.addUserWatchedMedia(expectedDto) } returns Unit
            coEvery { authenticationRepositoryImpl.getLoggedInUser() } returns createMockUser()
            // When
            continueWatchingRepositoryImpl.addUserWatchedMedia(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = contentType
            )

            // Then
            coVerify { localUserWatchedMediaDataSource.addUserWatchedMedia(expectedDto) }
        }

    @Test
    fun `addContinueWatching should call data source when correct parameters for tv show provided`() =
        runTest {
            // Given
            val contentId = 456L
            val genreIds = listOf(4L, 5L)
            val contentImageUrl = "https://example.com/tvshow.jpg"
            val contentType = UserWatchedMedia.ContentType.TV_SHOW
            val expectedDto = UserWatchedMediaDto(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = UserWatchedMediaDto.ContentType.TV_SHOW,
                userId = 1L
            )

            coEvery { localUserWatchedMediaDataSource.addUserWatchedMedia(expectedDto) } returns Unit
            coEvery { authenticationRepositoryImpl.getLoggedInUser() } returns createMockUser()
            // When
            continueWatchingRepositoryImpl.addUserWatchedMedia(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = contentType
            )

            // Then
            coVerify { localUserWatchedMediaDataSource.addUserWatchedMedia(expectedDto) }
        }


    @Test
    fun `addContinueWatching should handle large genre list when large genre is provided`() =
        runTest {
            // Given
            val contentId = 999L
            val genreIds = (1L..20L).toList()
            val contentImageUrl = "https://example.com/large.jpg"
            val contentType = UserWatchedMedia.ContentType.TV_SHOW
            val expectedDto = UserWatchedMediaDto(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = UserWatchedMediaDto.ContentType.TV_SHOW,
                userId = 1L
            )

            coEvery { localUserWatchedMediaDataSource.addUserWatchedMedia(expectedDto) } returns Unit
            coEvery { authenticationRepositoryImpl.getLoggedInUser() } returns createMockUser()
            // When
            continueWatchingRepositoryImpl.addUserWatchedMedia(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = contentType
            )

            // Then
            coVerify { localUserWatchedMediaDataSource.addUserWatchedMedia(expectedDto) }

        }

    companion object {
        private fun createMockContinueWatchingDto(
            contentId: Long,
            contentType: UserWatchedMediaDto.ContentType
        ) = UserWatchedMediaDto(
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
            contentType: UserWatchedMedia.ContentType
        ) = UserWatchedMedia(
            contentId = contentId,
            genreIds = listOf(1L, 2L),
            contentImageUrl = "https://example.com/image$contentId.jpg",
            contentType = contentType,
            userId = 1L
        )
    }
}