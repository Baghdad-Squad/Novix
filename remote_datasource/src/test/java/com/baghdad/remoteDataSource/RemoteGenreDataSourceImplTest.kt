package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.GenreApiService
import com.baghdad.remoteDataSource.response.genre.GenreListResponse
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteGenreDataSourceImplTest {
    private val genreApiService = mockk<GenreApiService>()
    private val logger = mockk<Logger>(relaxed = true)
    private val dataSource = RemoteGenreDataSourceImpl(genreApiService, logger)

    @Test
    fun `should return movie genre dto list when getMovieGenre is called with valid language`() = runTest {
        val successResponse = Response.success(genreListResponse)
        coEvery { genreApiService.getMovieGenre() } returns successResponse

        val result = dataSource.getMovieGenre(LANGUAGE)

        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(expectedMovieGenreDto1)
        assertThat(result[1]).isEqualTo(expectedMovieGenreDto2)
        coVerify(exactly = 1) { genreApiService.getMovieGenre() }
    }

    @Test
    fun `should return tv show genre dto list when getTvShowGenre is called with valid language`() = runTest {
        val successResponse = Response.success(genreListResponse)
        coEvery { genreApiService.getTvShowGenre() } returns successResponse

        val result = dataSource.getTvShowGenre(LANGUAGE)

        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(expectedTvShowGenreDto1)
        assertThat(result[1]).isEqualTo(expectedTvShowGenreDto2)
        coVerify(exactly = 1) { genreApiService.getTvShowGenre() }
    }

    @Test
    fun `should return empty list when getMovieGenre receives empty genres`() = runTest {
        val successResponse = Response.success(genreListResponseEmpty)
        coEvery { genreApiService.getMovieGenre() } returns successResponse

        val result = dataSource.getMovieGenre(LANGUAGE)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when getTvShowGenre receives empty genres`() = runTest {
        val successResponse = Response.success(genreListResponseEmpty)
        coEvery { genreApiService.getTvShowGenre() } returns successResponse

        val result = dataSource.getTvShowGenre(LANGUAGE)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should set correct genre type for movie genres`() = runTest {
        val successResponse = Response.success(genreListResponse)
        coEvery { genreApiService.getMovieGenre() } returns successResponse

        val result = dataSource.getMovieGenre(LANGUAGE)

        assertThat(result).hasSize(2)
        assertThat(result[0].type).isEqualTo(GenreDto.GenreType.MOVIE)
        assertThat(result[1].type).isEqualTo(GenreDto.GenreType.MOVIE)
    }

    @Test
    fun `should set correct genre type for tv show genres`() = runTest {
        val successResponse = Response.success(genreListResponse)
        coEvery { genreApiService.getTvShowGenre() } returns successResponse

        val result = dataSource.getTvShowGenre(LANGUAGE)

        assertThat(result).hasSize(2)
        assertThat(result[0].type).isEqualTo(GenreDto.GenreType.TV_SHOW)
        assertThat(result[1].type).isEqualTo(GenreDto.GenreType.TV_SHOW)
    }

    companion object {
        const val LANGUAGE = "en-US"
        const val GENRE_ID_1 = 28L
        const val GENRE_ID_2 = 12L
        const val GENRE_NAME_1 = "Action"
        const val GENRE_NAME_2 = "Adventure"

        val genre1 = GenreListResponse.GenreItemDto(
            id = GENRE_ID_1,
            name = GENRE_NAME_1
        )

        val genre2 = GenreListResponse.GenreItemDto(
            id = GENRE_ID_2,
            name = GENRE_NAME_2
        )

        val genreWithNullId = GenreListResponse.GenreItemDto(
            id = null,
            name = GENRE_NAME_1
        )

        val genreWithNullName = GenreListResponse.GenreItemDto(
            id = GENRE_ID_1,
            name = ""
        )

        val genreListResponse = GenreListResponse(
            genres = listOf(genre1, genre2)
        )

        val genreListResponseWithNulls = GenreListResponse(
            genres = listOf(genreWithNullId, genreWithNullName)
        )

        val genreListResponseEmpty = GenreListResponse(
            genres = emptyList()
        )

        val expectedMovieGenreDto1 = GenreDto(
            id = GENRE_ID_1,
            name = GENRE_NAME_1,
            type = GenreDto.GenreType.MOVIE
        )

        val expectedMovieGenreDto2 = GenreDto(
            id = GENRE_ID_2,
            name = GENRE_NAME_2,
            type = GenreDto.GenreType.MOVIE
        )

        val expectedTvShowGenreDto1 = GenreDto(
            id = GENRE_ID_1,
            name = GENRE_NAME_1,
            type = GenreDto.GenreType.TV_SHOW
        )

        val expectedTvShowGenreDto2 = GenreDto(
            id = GENRE_ID_2,
            name = GENRE_NAME_2,
            type = GenreDto.GenreType.TV_SHOW
        )

        val expectedMovieGenreWithNullId = GenreDto(
            id = 0L,
            name = GENRE_NAME_1,
            type = GenreDto.GenreType.MOVIE
        )

        val expectedMovieGenreWithNullName = GenreDto(
            id = GENRE_ID_1,
            name = "",
            type = GenreDto.GenreType.MOVIE
        )

        val expectedTvShowGenreWithNullId = GenreDto(
            id = 0L,
            name = GENRE_NAME_1,
            type = GenreDto.GenreType.TV_SHOW
        )

        val expectedTvShowGenreWithNullName = GenreDto(
            id = GENRE_ID_1,
            name = "",
            type = GenreDto.GenreType.TV_SHOW
        )
    }

}