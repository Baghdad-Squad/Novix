//package com.baghdad.domain.usecase.search
//
//import com.baghdad.domain.model.PagedResult
//import com.baghdad.domain.model.search.SearchFilter
//import com.baghdad.domain.repository.FavoriteGenreRepository
//import com.baghdad.domain.repository.SearchRepository
//import com.baghdad.entity.media.Genre
//import com.baghdad.entity.media.Movie
//import com.google.common.truth.Truth.assertThat
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.mockk
//import kotlinx.coroutines.test.runTest
//import kotlinx.datetime.LocalDate
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class SearchMoviesUseCaseTest {
//
//    @BeforeEach
//    fun setUp() {
//        searchRepository = mockk()
//        favoriteGenreRepository = mockk()
//        searchMoviesUseCase =
//            SearchMoviesUseCase(searchRepository, favoriteGenreRepository)
//
//        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns favoriteGenres
//    }
//
//    @Test
//    fun `searchMoviesUseCase() should return filtered and sorted movies`() = runTest {
//        val query = "action"
//        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies
//
//        val result = searchMoviesUseCase(query,1)
//
//        assertThat(result.data).hasSize(2)
//        assertThat(result.data[0].title).isEqualTo("Inception")
//        assertThat(result.data[1].title).isEqualTo("The Dark Knight")
//    }
//
//    @Test
//    fun `searchMoviesUseCase() should maintain pagination keys after filtering`() = runTest {
//        val query = "action"
//        SearchFilter(
//            minimumYear = 2010, minimumRating = 9, maximumYear = 2020, selectedGenres = listOf(
//                Genre(id = 1L, name = "Action")
//            )
//        )
//        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies
//
//        val result = searchMoviesUseCase(query, 1)
//
//        assertThat(result.prevKey).isNull()
//        assertThat(result.nextKey).isEqualTo(2)
//    }
//
//    @Test
//    fun `searchMoviesUseCase() should sort by favorite genre score descending`() = runTest {
//        val query = "action"
//        SearchFilter(
//            minimumYear = 9,
//            minimumRating = 9,
//            maximumYear = 2020,
//            selectedGenres = listOf(
//                Genre(id = 1L, name = "Action")
//            ),
//        )
//        val moviesWithDifferentScores = sampleMovies.copy(
//            data = listOf(
//                sampleMovies.data[0].copy(genres = listOf(Genre(5L, "Drama"))), // Score 1
//                sampleMovies.data[1].copy(genres = listOf(Genre(3L, "Sci-Fi"))) // Score 3
//            )
//        )
//        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns moviesWithDifferentScores
//
//        val result = searchMoviesUseCase(query,1)
//
//        assertThat(result.data[0].genres[0].name).isEqualTo("Sci-Fi")
//        assertThat(result.data[1].genres[0].name).isEqualTo("Drama")
//    }
//
//    @Test
//    fun `searchMoviesUseCase() should make correct repository calls`() = runTest {
//        val query = "action"
//        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies
//
//        searchMoviesUseCase(query, 1)
//
//        coVerify(exactly = 1) { searchRepository.searchMoviesByTitle(query, 1) }
//        coVerify(exactly = 1) { favoriteGenreRepository.getFavoriteGenres() }
//    }
//
//    @Test
//    fun `searchMoviesUseCase() should handle empty favorite genres correctly`() = runTest {
//        val query = "action"
//        SearchFilter(
//            minimumYear = 2010, minimumRating = 9, maximumYear = 2020, selectedGenres = listOf(
//                Genre(id = 1L, name = "Action")
//            )
//        )
//        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleMovies
//        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns emptyMap()
//
//        val result = searchMoviesUseCase(query, 1)
//
//        assertThat(result.data).hasSize(2)
//    }
//
//    companion object {
//        private lateinit var searchRepository: SearchRepository
//        private lateinit var favoriteGenreRepository: FavoriteGenreRepository
//        private lateinit var searchMoviesUseCase: SearchMoviesUseCase
//
//        private val sampleMovies = PagedResult(
//            prevKey = null, nextKey = 2, data = listOf(
//                Movie(
//                    id = 1L,
//                    title = "The Dark Knight",
//                    genres = listOf(Genre(1L, "Action"), Genre(2L, "Crime")),
//                    averageRating = 9.0,
//                    releaseDate = LocalDate(2008, 7, 18),
//                    userRating = 9.0,
//                    overview = "Batman raises the stakes in his war on crime. With the help of Lieutenant Jim Gordon and District Attorney Harvey Dent, Batman has been able to dismantle organized crime in Gotham City. But when a vile young criminal calling himself the Joker suddenly throws Gotham into chaos, the caped crusader begins to tread a fine line between hero and vigilante.",
//                    posterImageURL = "https://image.tmdb.org/t/p/w500/1hRoyzDtpgMU7Dz4JGQEMo0wLv9.jpg",
//                    trailerURL = "https://www.youtube.com/watch?v=EXeTwQWrcwY",
//                    runtimeMinutes = 152
//                ), Movie(
//                    id = 2L,
//                    title = "Inception",
//                    genres = listOf(Genre(3L, "Sci-Fi"), Genre(4L, "Action")),
//                    averageRating = 8.8,
//                    releaseDate = LocalDate(2010, 7, 16),
//                    userRating = 8.5,
//                    overview = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.",
//                    posterImageURL = "https://image.tmdb.org/t/p/w500/8hP9d6b2k1z5a4e7c3f8b2f8b2f8b2f8.jpg",
//                    trailerURL = "https://www.youtube.com/watch?v=YoHD9XEInc0",
//                    runtimeMinutes = 148
//                )
//            )
//        )
//
//        private val favoriteGenres = mapOf(
//            "Action" to 5, "Sci-Fi" to 3, "Drama" to 1
//        )
//    }
//}