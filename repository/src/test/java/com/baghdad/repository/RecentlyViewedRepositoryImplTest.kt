package com.baghdad.repository

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.repository.datasource.local.RecentlyViewedDataSource
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.model.RecentlyViewedDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RecentlyViewedRepositoryImplTest {
    private val recentlyViewedDataSource: RecentlyViewedDataSource = mockk()
    private val savableMovieDataSource: SavableMovieDataSource = mockk()
    private val recentlyViewedRepositoryImpl: RecentlyViewedRepositoryImpl =
        RecentlyViewedRepositoryImpl(
            recentlyViewedDataSource = recentlyViewedDataSource,
            savableMovieDataSource = savableMovieDataSource
        )

    @Test
    fun `getAllRecentlyViewed should return flow of recently viewed items when data source succeeds`() =
        runTest {
            val mockRecentlyViewedDtos = listOf(
                createMockRecentlyViewedDto(1L, RecentlyViewedDto.ContentType.MOVIE),
                createMockRecentlyViewedDto(2L, RecentlyViewedDto.ContentType.TV_SHOW)
            )
            val expectedRecentlyViewed = listOf(
                createMockRecentlyViewed(1L, RecentlyViewed.ContentType.MOVIE),
                createMockRecentlyViewed(2L, RecentlyViewed.ContentType.TV_SHOW)
            )

            coEvery { recentlyViewedDataSource.getAllRecentlyViewed() } returns flowOf(
                mockRecentlyViewedDtos
            )
            coEvery { savableMovieDataSource.getSavedMovies() } returns emptyMap()

            val result = recentlyViewedRepositoryImpl.getAllRecentlyViewed().toList()

            assertThat(1 == result.size).isTrue()
            assertThat(result.first()).isEqualTo(expectedRecentlyViewed)
            coVerify { recentlyViewedDataSource.getAllRecentlyViewed() }
        }

    @Test
    fun `getAllRecentlyViewed should return empty flow when no recently viewed items found`() =
        runTest {
            coEvery { recentlyViewedDataSource.getAllRecentlyViewed() } returns flowOf(emptyList())
            coEvery { savableMovieDataSource.getSavedMovies() } returns emptyMap()

            val result = recentlyViewedRepositoryImpl.getAllRecentlyViewed().toList()

            assertThat(1 == result.size).isTrue()
            assertThat(emptyList<RecentlyViewed>() == result[0]).isTrue()
            coVerify { recentlyViewedDataSource.getAllRecentlyViewed() }
        }

    @Test
    fun `deleteAllRecentlyViewed should call data source delete method when it is requested`() =
        runTest {
            coEvery { recentlyViewedDataSource.deleteAllRecentlyViewed() } returns Unit
            coEvery { savableMovieDataSource.getSavedMovies() } returns emptyMap()

            recentlyViewedRepositoryImpl.deleteAllRecentlyViewed()

            coVerify { recentlyViewedDataSource.deleteAllRecentlyViewed() }
        }

    @Test
    fun `addRecentlyViewed should add movie and update favorite genres when content type is movie`() =
        runTest {
            val movieId = 123L
            val recentlyViewed = createMockRecentlyViewed(movieId, RecentlyViewed.ContentType.MOVIE)

            coEvery { savableMovieDataSource.getSavedMovies() } returns emptyMap()
            coEvery { recentlyViewedDataSource.addMediaToRecentlyViewed(any()) } returns Unit

            recentlyViewedRepositoryImpl.addRecentlyViewed(recentlyViewed)

            coVerify { recentlyViewedDataSource.addMediaToRecentlyViewed(any()) }
        }

    @Test
    fun `addRecentlyViewed should add tv show and update favorite genres when content type is tv show`() =
        runTest {
            val tvShowId = 456L
            val recentlyViewed =
                createMockRecentlyViewed(tvShowId, RecentlyViewed.ContentType.TV_SHOW)
            coEvery { recentlyViewedDataSource.addMediaToRecentlyViewed(any()) } returns Unit

            recentlyViewedRepositoryImpl.addRecentlyViewed(recentlyViewed)

            coVerify { recentlyViewedDataSource.addMediaToRecentlyViewed(any()) }
        }

    @Test
    fun `addRecentlyViewed should throw exception when data source fails`() = runTest {
        val movieId = 123L
        val recentlyViewed = createMockRecentlyViewed(movieId, RecentlyViewed.ContentType.MOVIE)
        val exception = RuntimeException("Database error")

        coEvery { recentlyViewedDataSource.addMediaToRecentlyViewed(any()) } throws exception

        assertThrows<Exception> {
            recentlyViewedRepositoryImpl.addRecentlyViewed(recentlyViewed)
        }
    }

    companion object {

        private fun createMockRecentlyViewedDto(
            contentId: Long,
            contentType: RecentlyViewedDto.ContentType
        ) = RecentlyViewedDto(
            contentId = contentId,
            contentImageUrl = "/image_$contentId.jpg",
            contentType = contentType,
            viewedAtEpochMillis = 1672574400000L
        )

        private fun createMockRecentlyViewed(
            contentId: Long,
            contentType: RecentlyViewed.ContentType
        ) = RecentlyViewed(
            contentId = contentId,
            contentImageUrl = "/image_$contentId.jpg",
            contentType = contentType,
            viewedAt = LocalDateTime.parse("2023-01-01T04:00"),
            isSaved = false,
            listId = null
        )
    }
}