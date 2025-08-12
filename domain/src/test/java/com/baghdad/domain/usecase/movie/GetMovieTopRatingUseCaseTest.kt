//package com.baghdad.domain.usecase.movie
//
//import com.baghdad.domain.model.pagination.PagedResult
//import com.baghdad.domain.repository.MovieRepository
//import com.baghdad.domain.testHelper.getSampleMovie
//import com.baghdad.domain.testHelper.getSampleSavedMovie
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
//
//class GetMovieTopRatingUseCaseTest {
//
//    private lateinit var movieRepository: MovieRepository
//    private lateinit var useCase: GetMovieTopRatingUseCase
//
//    @BeforeEach
//    fun setUp() {
//        movieRepository = mockk()
//        useCase = GetMovieTopRatingUseCase(movieRepository)
//    }
//
//    @Test
//    fun `getTopRatedMovies() should return all movies when genreId is null`() = runTest {
//        // Given
//        val page = 1
//        val movies = listOf(
//            createMovie(1, listOf(1)),
//            createMovie(2, listOf(2))
//        )
//        val pagedResult = PagedResult(data = movies, nextKey = 2, prevKey = null)
//
//        coEvery { movieRepository.getTopRatedMovies(page) } returns pagedResult
//
//        // When
//        val result = useCase(page, genreId = null)
//
//        // Then
//        assertThat(result.data).containsExactlyElementsIn(movies)
//        coVerify(exactly = 1) { movieRepository.getTopRatedMovies(page) }
//    }
//
//    @Test
//    fun `getTopRatedMovies() should return filtered movies when genreId is provided`() = runTest {
//        // Given
//        val page = 1
//        val genreId = 2L
//        val movies = listOf(
//            createMovie(1, listOf(1)),
//            createMovie(2, listOf(2)),
//            createMovie(3, listOf(2, 3))
//        )
//        val expected = listOf(movies[1], movies[2])
//        val pagedResult = PagedResult(data = movies, nextKey = 2, prevKey = null)
//
//        coEvery { movieRepository.getTopRatedMovies(page) } returns pagedResult
//
//        // When
//        val result = useCase(page, genreId)
//
//        // Then
//        assertThat(result.data).containsExactlyElementsIn(expected)
//        coVerify(exactly = 1) { movieRepository.getTopRatedMovies(page) }
//    }
//
//    @Test
//    fun `getTopRatedMovies() should return empty list when no movies match genreId`() = runTest {
//
//
//        coEvery { movieRepository.getTopRatedMovies(PAGE) } returns sampleSavedMovies
//
//        // When
//        val result = useCase(PAGE, GENRE_ID)
//
//        // Then
//        assertThat(result.data).isEmpty()
//        coVerify(exactly = 1) { movieRepository.getTopRatedMovies(PAGE) }
//    }
//
//    @Test
//    fun `getTopRatedMovies() should preserve pagination keys when filtering by genreId`() = runTest {
//        // Given
//
//
//        coEvery { movieRepository.getTopRatedMovies(PAGE) } returns sampleSavedMovies
//
//        // When
//        val result = useCase(PAGE, GENRE_ID)
//
//        // Then
//        assertThat(result.nextKey).isEqualTo(3)
//        assertThat(result.prevKey).isEqualTo(1)
//        assertThat(result.data).hasSize(1)
//    }
//
//    companion object{
//        private const val PAGE = 2
//        private const val PAGE_SIZE = 20
//        private const val GENRE_ID = 5L
//        private val sampleSavedMovies = PagedResult(
//            listOf(
//                getSampleSavedMovie(),
//                getSampleSavedMovie(
//                    movie = getSampleMovie(
//                        id = 2L,
//                        genres = listOf(Genre(1L, "Action"), Genre(2L, "Drama")),
//                    )
//                ),
//                getSampleSavedMovie(
//                    movie = getSampleMovie(
//                        id = 3L,
//                        genres = listOf(Genre(3L, "Adventure"), Genre(5L, "Drama")),
//                    )
//                )
//            ),
//            nextKey = 2,
//            prevKey = null
//        )
//    }
//}