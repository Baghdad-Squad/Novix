package com.baghdad.repository

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.repository.datasource.local.LocalFavoriteGenreDataSource
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.RecentlyViewedDto
import com.baghdad.repository.model.TvShowDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RecentlyViewedRepositoryImplTest {

    private lateinit var localRecentlyViewedDataSource: LocalRecentlyViewedDataSource
    private lateinit var localFavoriteGenreDataSource: LocalFavoriteGenreDataSource
    private lateinit var localMovieDataSource: LocalMovieDataSource
    private lateinit var localTvShowDataSource: LocalTvShowDataSource
    private lateinit var recentlyViewedRepositoryImpl: RecentlyViewedRepositoryImpl

    @BeforeEach
    fun setUp() {
        localRecentlyViewedDataSource = mockk()
        localFavoriteGenreDataSource = mockk()
        localMovieDataSource = mockk()
        localTvShowDataSource = mockk()
        recentlyViewedRepositoryImpl = RecentlyViewedRepositoryImpl(
            localRecentlyViewedDataSource = localRecentlyViewedDataSource,
            localFavoriteGenreDataSource = localFavoriteGenreDataSource,
            localMovieDataSource = localMovieDataSource,
            localTvShowDataSource = localTvShowDataSource
        )
    }

    @Test
    fun `getAllRecentlyViewed should return flow of recently viewed items when data source succeeds`() = runTest {
        // Given
        val mockRecentlyViewedDtos = listOf(
            createMockRecentlyViewedDto(1L, "Movie Title", RecentlyViewedDto.ContentType.MOVIE),
            createMockRecentlyViewedDto(2L, "TV Show Title", RecentlyViewedDto.ContentType.TV_SHOW)
        )
        val expectedRecentlyViewed = listOf(
            createMockRecentlyViewed(1L, RecentlyViewed.ContentType.MOVIE),
            createMockRecentlyViewed(2L, RecentlyViewed.ContentType.TV_SHOW)
        )

        coEvery { localRecentlyViewedDataSource.getAllRecentlyViewed() } returns flowOf(mockRecentlyViewedDtos)

        val result = recentlyViewedRepositoryImpl.getAllRecentlyViewed().toList()

        assertEquals(1, result.size)
        val recentlyViewedList = result[0]
        assertEquals(expectedRecentlyViewed.size, recentlyViewedList.size)
        assertEquals(expectedRecentlyViewed[0].contentId, recentlyViewedList[0].contentId)
        assertEquals(expectedRecentlyViewed[0].contentImageUrl, recentlyViewedList[0].contentImageUrl)
        assertEquals(expectedRecentlyViewed[0].contentType, recentlyViewedList[0].contentType)
        assertEquals(expectedRecentlyViewed[1].contentId, recentlyViewedList[1].contentId)
        assertEquals(expectedRecentlyViewed[1].contentImageUrl, recentlyViewedList[1].contentImageUrl)
        assertEquals(expectedRecentlyViewed[1].contentType, recentlyViewedList[1].contentType)
        coVerify { localRecentlyViewedDataSource.getAllRecentlyViewed() }
    }

    @Test
    fun `getAllRecentlyViewed should return empty flow when no recently viewed items found`() = runTest {

        coEvery { localRecentlyViewedDataSource.getAllRecentlyViewed() } returns flowOf(emptyList())

        val result = recentlyViewedRepositoryImpl.getAllRecentlyViewed().toList()

        assertEquals(1, result.size)
        assertEquals(emptyList<RecentlyViewed>(), result[0])
        coVerify { localRecentlyViewedDataSource.getAllRecentlyViewed() }
    }

    @Test
    fun `deleteAllRecentlyViewed should call data source delete method`() = runTest {

        coEvery { localRecentlyViewedDataSource.deleteAllRecentlyViewed() } returns Unit

        recentlyViewedRepositoryImpl.deleteAllRecentlyViewed()

        coVerify { localRecentlyViewedDataSource.deleteAllRecentlyViewed() }
    }

    @Test
    fun `deleteAllRecentlyViewed should handle exception when data source fails`() = runTest {

        val exception = RuntimeException("Database error")
        coEvery { localRecentlyViewedDataSource.deleteAllRecentlyViewed() } throws exception

        assertThrows<Exception> {
            recentlyViewedRepositoryImpl.deleteAllRecentlyViewed()
        }
    }

    @Test
    fun `addRecentlyViewed should add movie and update favorite genres when content type is movie`() = runTest {

        val movieId = 123L
        val recentlyViewed = createMockRecentlyViewed(movieId, RecentlyViewed.ContentType.MOVIE)
        val mockMovieDto = createMockMovieDto(movieId)
        val mockGenres = listOf(
            GenreDto(1L, "Action", GenreDto.GenreType.MOVIE),
            GenreDto(2L, "Drama", GenreDto.GenreType.MOVIE)
        )
        val movieDtoWithGenres = mockMovieDto.copy(genres = mockGenres)

        coEvery { localMovieDataSource.getMovieById(movieId) } returns movieDtoWithGenres
        coEvery { localFavoriteGenreDataSource.updateFavoriteGenreCount(any(), any()) } returns Unit
        coEvery { localRecentlyViewedDataSource.addMediaToRecentlyViewed(any()) } returns Unit

        recentlyViewedRepositoryImpl.addRecentlyViewed(recentlyViewed)

        coVerify { localMovieDataSource.getMovieById(movieId) }
        coVerify { localFavoriteGenreDataSource.updateFavoriteGenreCount(1L, "Action") }
        coVerify { localFavoriteGenreDataSource.updateFavoriteGenreCount(2L, "Drama") }
        coVerify { localRecentlyViewedDataSource.addMediaToRecentlyViewed(any()) }
    }

    @Test
    fun `addRecentlyViewed should add tv show and update favorite genres when content type is tv show`() = runTest {

        val tvShowId = 456L
        val recentlyViewed = createMockRecentlyViewed(tvShowId, RecentlyViewed.ContentType.TV_SHOW)
        val mockTvShowDto = createMockTvShowDto(tvShowId)
        val mockGenres = listOf(
            GenreDto(3L, "Comedy", GenreDto.GenreType.TV_SHOW),
            GenreDto(4L, "Romance", GenreDto.GenreType.TV_SHOW)
        )
        val tvShowDtoWithGenres = mockTvShowDto.copy(genres = mockGenres)

        coEvery { localTvShowDataSource.getTvShowById(tvShowId) } returns tvShowDtoWithGenres
        coEvery { localFavoriteGenreDataSource.updateFavoriteGenreCount(any(), any()) } returns Unit
        coEvery { localRecentlyViewedDataSource.addMediaToRecentlyViewed(any()) } returns Unit

        recentlyViewedRepositoryImpl.addRecentlyViewed(recentlyViewed)

        coVerify { localTvShowDataSource.getTvShowById(tvShowId) }
        coVerify { localFavoriteGenreDataSource.updateFavoriteGenreCount(3L, "Comedy") }
        coVerify { localFavoriteGenreDataSource.updateFavoriteGenreCount(4L, "Romance") }
        coVerify { localRecentlyViewedDataSource.addMediaToRecentlyViewed(any()) }
    }

    @Test
    fun `addRecentlyViewed should not update favorite genres when media has no genres`() = runTest {

        val movieId = 789L
        val recentlyViewed = createMockRecentlyViewed(movieId, RecentlyViewed.ContentType.MOVIE)
        val mockMovieDto = createMockMovieDto(movieId).copy(genres = emptyList())

        coEvery { localMovieDataSource.getMovieById(movieId) } returns mockMovieDto
        coEvery { localRecentlyViewedDataSource.addMediaToRecentlyViewed(any()) } returns Unit

        recentlyViewedRepositoryImpl.addRecentlyViewed(recentlyViewed)

        coVerify { localMovieDataSource.getMovieById(movieId) }
        coVerify(exactly = 0) { localFavoriteGenreDataSource.updateFavoriteGenreCount(any(), any()) }
        coVerify { localRecentlyViewedDataSource.addMediaToRecentlyViewed(any()) }
    }

    @Test
    fun `addRecentlyViewed should throw exception when data source fails`() = runTest {

        val movieId = 123L
        val recentlyViewed = createMockRecentlyViewed(movieId, RecentlyViewed.ContentType.MOVIE)
        val exception = RuntimeException("Database error")

        coEvery { localMovieDataSource.getMovieById(movieId) } throws exception

        assertThrows<Exception> {
            recentlyViewedRepositoryImpl.addRecentlyViewed(recentlyViewed)
        }
    }

    companion object {

        private fun createMockRecentlyViewedDto(
            contentId: Long,
            title: String,
            contentType: RecentlyViewedDto.ContentType
        ) = RecentlyViewedDto(
            contentId = contentId,
            contentImageUrl = "/image_$contentId.jpg",
            contentType = contentType,
            viewedAtEpochMillis = 1672574400000L
        )

        private fun createMockMovieDto(
            id: Long,
            title: String = "Test Movie"
        ) = MovieDto(
            id = id,
            title = title,
            genres = emptyList(),
            imdbRating = 8.0,
            userRating = 7.5,
            releaseDate = "2023-01-01",
            overview = "Test movie overview",
            posterPictureURL = "/movie_poster.jpg",
            runtimeMinutes = 120,
            trailerURL = " "
        )

        private fun createMockTvShowDto(
            id: Long,
            title: String = "Test TV Show"
        ) = TvShowDto(
            id = id,
            title = title,
            genres = emptyList(),
            imdbRating = 8.5,
            userRating = 8.0,
            releaseDate = "2023-01-01",
            overview = "Test TV show overview",
            posterPictureURL = "/tv_show_poster.jpg",
            numberOfSeasons = 3,
            trailerURL = " ",
            headerImagesURLs = emptyList(),
        )

        private fun createMockRecentlyViewed(
            contentId: Long,
            contentType: RecentlyViewed.ContentType
        ) = RecentlyViewed(
            contentId = contentId,
            contentImageUrl = "/image_$contentId.jpg",
            contentType = contentType,
            viewedAt = LocalDateTime.parse("2023-01-01T12:00:00")
        )
    }
}