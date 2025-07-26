package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.util.SearchFilterHelper
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

    @BeforeEach
    fun setUp() {
        searchRepository = mockk()
        favoriteGenreRepository = mockk()
        filterHelper = mockk()
        searchMoviesUseCase =
            SearchMoviesUseCase(searchRepository, favoriteGenreRepository, filterHelper)

        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns favoriteGenres
        coEvery { filterHelper.matchesRatingFilter(any(), any()) } returns true
        coEvery { filterHelper.matchesYearFilter(any(), any(), any()) } returns true
        coEvery { filterHelper.matchesGenreFilter(any(), any()) } returns true
    }

    @Test
    fun `searchMoviesUseCase returns filtered and sorted movies`() = runTest {
        // Given
        val query = "action"
        val filter = SearchFilter(
            minimumYear = 9, maximumYear = 2020, minimumRating = 9, selectedGenres = listOf(
                Genre(id = 1L, name = "Action"), Genre(id = 2L, name = "Crime")
            )
        )
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies

        // When
        val result = searchMoviesUseCase(query, filter, 1)

        // Then
        assertThat(result.data).hasSize(2)
        assertThat(result.data[0].title).isEqualTo("Inception")
        assertThat(result.data[1].title).isEqualTo("The Dark Knight")
    }

    @Test
    fun `searchMoviesUseCase applies rating filter correctly`() = runTest {
        // Given
        val query = "action"
        val filter = SearchFilter(
            minimumRating = 9,
            minimumYear = 2010,
            maximumYear = 2020,
            selectedGenres = listOf(Genre(id = 1L, "Crime"), Genre(id = 1L, "Action"))
        )
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies
        coEvery { filterHelper.matchesRatingFilter(9.0, 9) } returns true
        coEvery { filterHelper.matchesRatingFilter(8.8, 9) } returns false

        // When
        val result = searchMoviesUseCase(query, filter, 1)

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].title).isEqualTo("The Dark Knight")
    }

    @Test
    fun `searchMoviesUseCase applies year filter correctly`() = runTest {
        // Given
        val query = "action"
        val filter = SearchFilter(
            minimumYear = 2010, maximumYear = 2020, minimumRating = 9, selectedGenres = emptyList()
        )

        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies
        coEvery { filterHelper.matchesYearFilter(2008, 2010, 2020) } returns false
        coEvery { filterHelper.matchesYearFilter(2010, 2010, 2020) } returns true
        coEvery { filterHelper.matchesRatingFilter(any(), any()) } returns true
        coEvery { filterHelper.matchesGenreFilter(any(), any()) } returns true

        // When
        val result = searchMoviesUseCase(query, filter, 1)

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].title).isEqualTo("Inception")
    }

    @Test
    fun `searchMoviesUseCase applies genre filter correctly`() = runTest {
        // Given
        val query = "action"
        val filter = SearchFilter(
            selectedGenres = listOf(Genre(id = 1L, name = "Crime")),
            minimumYear = 2010,
            minimumRating = 9,
            maximumYear = 2020,
        )
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies
        coEvery {
            filterHelper.matchesGenreFilter(
                listOf(Genre(1L, "Action"), Genre(2L, "Crime")), listOf(
                    Genre(id = 1L, name = "Crime")
                )
            )
        } returns true
        coEvery {
            filterHelper.matchesGenreFilter(
                listOf(Genre(3L, "Sci-Fi"), Genre(4L, "Action")), listOf(
                    Genre(id = 1L, name = "Crime")
                )
            )
        } returns false

        // When
        val result = searchMoviesUseCase(query, filter, 1)

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].title).isEqualTo("The Dark Knight")
    }

    @Test
    fun `searchMoviesUseCase returns empty list when no matches after filtering`() = runTest {
        // Given
        val query = "action"
        val filter = SearchFilter(
            minimumRating = 9,
            minimumYear = 2010,
            maximumYear = 2020,
            selectedGenres = listOf(
                Genre(id = 1L, name = "Comedy")
            ),
        )
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies
        coEvery { filterHelper.matchesRatingFilter(any(), 9) } returns false

        // When
        val result = searchMoviesUseCase(query, filter, 1)

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `searchMoviesUseCase maintains pagination keys after filtering`() = runTest {
        // Given
        val query = "action"
        val filter = SearchFilter(
            minimumYear = 2010, minimumRating = 9, maximumYear = 2020, selectedGenres = listOf(
                Genre(id = 1L, name = "Action")
            )
        )
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies

        // When
        val result = searchMoviesUseCase(query, filter, 1)

        // Then
        assertThat(result.prevKey).isNull()
        assertThat(result.nextKey).isEqualTo(2)
    }

    @Test
    fun `searchMoviesUseCase sorts by favorite genre score descending`() = runTest {
        // Given
        val query = "action"
        val filter = SearchFilter(
            minimumYear = 9,
            minimumRating = 9,
            maximumYear = 2020,
            selectedGenres = listOf(
                Genre(id = 1L, name = "Action")
            ),
        )
        val moviesWithDifferentScores = sampleMovies.copy(
            data = listOf(
                sampleMovies.data[0].copy(genres = listOf(Genre(5L, "Drama"))), // Score 1
                sampleMovies.data[1].copy(genres = listOf(Genre(3L, "Sci-Fi"))) // Score 3
            )
        )
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns moviesWithDifferentScores

        // When
        val result = searchMoviesUseCase(query, filter, 1)

        // Then
        assertThat(result.data[0].genres[0].name).isEqualTo("Sci-Fi")
        assertThat(result.data[1].genres[0].name).isEqualTo("Drama")
    }

    @Test
    fun `searchMoviesUseCase makes correct repository calls`() = runTest {
        // Given
        val query = "action"
        val filter = SearchFilter(
            minimumYear = 2010, minimumRating = 9, maximumYear = 2020, selectedGenres = listOf(
                Genre(id = 1L, name = "Action")
            )
        )
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies

        // When
        searchMoviesUseCase(query, filter, 1)

        // Then
        coVerify(exactly = 1) { searchRepository.searchMoviesByTitle(query, 1) }
        coVerify(exactly = 1) { favoriteGenreRepository.getFavoriteGenres() }
    }

    @Test
    fun `searchMoviesUseCase handles empty favorite genres correctly`() = runTest {
        // Given
        val query = "action"
        val filter = SearchFilter(
            minimumYear = 2010, minimumRating = 9, maximumYear = 2020, selectedGenres = listOf(
                Genre(id = 1L, name = "Action")
            )
        )
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies
        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns emptyMap()

        // When
        val result = searchMoviesUseCase(query, filter, 1)

        // Then
        assertThat(result.data).hasSize(2)
    }

    companion object {
        private lateinit var searchRepository: SearchRepository
        private lateinit var favoriteGenreRepository: FavoriteGenreRepository
        private lateinit var filterHelper: SearchFilterHelper
        private lateinit var searchMoviesUseCase: SearchMoviesUseCase

        private val sampleMovies = PagedResult(
            prevKey = null, nextKey = 2, data = listOf(
                Movie(
                    id = 1L,
                    title = "The Dark Knight",
                    genres = listOf(Genre(1L, "Action"), Genre(2L, "Crime")),
                    averageRating = 9.0,
                    releaseDate = LocalDate(2008, 7, 18),
                    userRating = 9.0,
                    overview = "Batman raises the stakes in his war on crime. With the help of Lieutenant Jim Gordon and District Attorney Harvey Dent, Batman has been able to dismantle organized crime in Gotham City. But when a vile young criminal calling himself the Joker suddenly throws Gotham into chaos, the caped crusader begins to tread a fine line between hero and vigilante.",
                    posterImageURL = "https://image.tmdb.org/t/p/w500/1hRoyzDtpgMU7Dz4JGQEMo0wLv9.jpg",
                    trailerURL = "https://www.youtube.com/watch?v=EXeTwQWrcwY",
                    runtimeMinutes = 152
                ), Movie(
                    id = 2L,
                    title = "Inception",
                    genres = listOf(Genre(3L, "Sci-Fi"), Genre(4L, "Action")),
                    averageRating = 8.8,
                    releaseDate = LocalDate(2010, 7, 16),
                    userRating = 8.5,
                    overview = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.",
                    posterImageURL = "https://image.tmdb.org/t/p/w500/8hP9d6b2k1z5a4e7c3f8b2f8b2f8b2f8.jpg",
                    trailerURL = "https://www.youtube.com/watch?v=YoHD9XEInc0",
                    runtimeMinutes = 148
                )
            )
        )

        private val favoriteGenres = mapOf(
            "Action" to 5, "Sci-Fi" to 3, "Drama" to 1
        )

    }
}