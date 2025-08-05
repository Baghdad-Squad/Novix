package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class SearchMoviesUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var favoriteGenreRepository: FavoriteGenreRepository
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase

    @BeforeEach
    fun setUp() {
        searchRepository = mockk()
        favoriteGenreRepository = mockk() // مش هيتستخدم، بس موجود عشان ما يحصلش كراش
        searchMoviesUseCase = SearchMoviesUseCase(searchRepository)
    }

    @Test
    fun `searchMoviesUseCase() should return movies as returned by repository`() = runTest {
        val query = "action"
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies

        val result = searchMoviesUseCase(query, 1)

        assertThat(result.data).hasSize(2)
        assertThat(result.data[0].title).isEqualTo("The Dark Knight")
        assertThat(result.data[1].title).isEqualTo("Inception")
    }

    @Test
    fun `searchMoviesUseCase() should preserve pagination keys from repository`() = runTest {
        val query = "action"
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies

        val result = searchMoviesUseCase(query, 1)

        assertThat(result.prevKey).isNull()
        assertThat(result.nextKey).isEqualTo(2)
    }

    @Test
    fun `searchMoviesUseCase() should call repository exactly once`() = runTest {
        val query = "action"
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies

        searchMoviesUseCase(query, 1)

        coVerify(exactly = 1) { searchRepository.searchMoviesByTitle(query, 1) }
    }

    companion object {
        private val sampleMovies = PagedResult(
            prevKey = null, nextKey = 2, data = listOf(
                Movie(
                    id = 1L,
                    title = "The Dark Knight",
                    genres = listOf(Genre(1L, "Action"), Genre(2L, "Crime")),
                    averageRating = 9.0,
                    releaseDate = LocalDate(2008, 7, 18),
                    userRating = 9.0,
                    overview = "Batman raises the stakes...",
                    posterImageURL = "https://image.tmdb.org/t/p/w500/1hRoyzDtpgMU7Dz4JGQEMo0wLv9.jpg",
                    trailerURL = "https://www.youtube.com/watch?v=EXeTwQWrcwY",
                    runtimeMinutes = 152
                ),
                Movie(
                    id = 2L,
                    title = "Inception",
                    genres = listOf(Genre(3L, "Sci-Fi"), Genre(4L, "Action")),
                    averageRating = 8.8,
                    releaseDate = LocalDate(2010, 7, 16),
                    userRating = 8.5,
                    overview = "A thief who steals corporate secrets...",
                    posterImageURL = "https://image.tmdb.org/t/p/w500/8hP9d6b2k1z5a4e7c3f8b2f8b2f8b2f8.jpg",
                    trailerURL = "https://www.youtube.com/watch?v=YoHD9XEInc0",
                    runtimeMinutes = 148
                )
            )
        )
    }
}
