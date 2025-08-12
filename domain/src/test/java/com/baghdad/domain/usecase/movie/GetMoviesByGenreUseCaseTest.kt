//package com.baghdad.domain.usecase.movie
//
//import com.baghdad.domain.model.pagination.PagedResult
//import com.baghdad.domain.repository.MovieRepository
//import com.baghdad.domain.testHelper.getSampleMovie
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
//import org.junit.jupiter.api.assertThrows
//
//class GetMoviesByGenreUseCaseTest {
//
//    private lateinit var movieRepository: MovieRepository
//    private lateinit var getMoviesByGenreUseCase: GetMoviesByGenreUseCase
//
//    private val sampleMovies = PagedResult(
//        prevKey = null,
//        nextKey = 2,
//        data = listOf(getSampleMovie())
//    )
//
//    @BeforeEach
//    fun setUp() {
//        movieRepository = mockk()
//        getMoviesByGenreUseCase = GetMoviesByGenreUseCase(movieRepository)
//    }
//
//    @Test
//    fun `getMoviesByGenre() should return paged result with correct data when called with valid genre and page`() = runTest {
//        val genreId = 1L
//        val page = 1
//        coEvery {
//            movieRepository.getMoviesByGenre(genreId, page, GetMoviesByGenreUseCase.PAGE_SIZE)
//        } returns sampleMovies
//
//        val result = getMoviesByGenreUseCase(genreId, page)
//
//        assertThat(result.data).hasSize(2)
//        assertThat(result.data[0].title).isEqualTo("The Dark Knight")
//        assertThat(result.data[1].title).isEqualTo("Mad Max: Fury Road")
//        assertThat(result.prevKey).isNull()
//        assertThat(result.nextKey).isEqualTo(2)
//    }
//
//    @Test
//    fun `getMoviesByGenre() should call repository with correct parameters when invoked`() = runTest {
//        val genreId = 1L
//        val page = 1
//        coEvery {
//            movieRepository.getMoviesByGenre(any(), any(), any())
//        } returns sampleMovies
//
//        getMoviesByGenreUseCase(genreId, PAGE_SIZE)
//
//        coVerify(exactly = 1) {
//            movieRepository.getMoviesByGenre(
//                genreId,
//                page,
//                PAGE_SIZE
//            )
//        }
//    }
//
//    @Test
//    fun `getMoviesByGenre() should return different results for different genre IDs`() = runTest {
//        val actionGenreId = 1L
//        val comedyGenreId = 2L
//        val page = 1
//        val comedyMovies = sampleMovies.copy(
//            data = listOf(
//                Movie(
//                    id = 3L,
//                    title = "Superbad",
//                    genres = listOf(Genre(2L, "Comedy")),
//                    averageRating = 7.6,
//                    releaseDate = LocalDate(2007, 8, 17),
//                    userRating = 7.8,
//                    overview = "Two co-dependent high school seniors...",
//                    posterImageURL = "https://image.tmdb.org/t/p/w500/superbad.jpg",
//                    trailerURL = "https://www.youtube.com/watch?v=superbad",
//                    runtimeMinutes = 113
//                )
//            )
//        )
//
//        coEvery {
//            movieRepository.getMoviesByGenre(actionGenreId, page, any())
//        } returns sampleMovies
//        coEvery {
//            movieRepository.getMoviesByGenre(comedyGenreId, page, any())
//        } returns comedyMovies
//
//        val actionResult = getMoviesByGenreUseCase(actionGenreId, page, PAGE_SIZE)
//        val comedyResult = getMoviesByGenreUseCase(comedyGenreId, page, PAGE_SIZE)
//
//        assertThat(actionResult.data).isNotEqualTo(comedyResult.data)
//        assertThat(actionResult.data[0].genres.map { it.id }).contains(actionGenreId)
//        assertThat(comedyResult.data[0].genres.map { it.id }).contains(comedyGenreId)
//    }
//
//    @Test
//    fun `getMoviesByGenre() should return different results for different page numbers`() = runTest {
//        val genreId = 1L
//        val page1Movies = sampleMovies
//        val page2Movies = sampleMovies.copy(
//            data = listOf(
//                Movie(
//                    id = 4L,
//                    title = "John Wick",
//                    genres = listOf(Genre(1L, "Action")),
//                    averageRating = 7.4,
//                    releaseDate = LocalDate(2014, 10, 24),
//                    userRating = 7.6,
//                    overview = "An ex-hit-man comes out of retirement...",
//                    posterImageURL = "https://image.tmdb.org/t/p/w500/johnwick.jpg",
//                    trailerURL = "https://www.youtube.com/watch?v=johnwick",
//                    runtimeMinutes = 101
//                )
//            ),
//            prevKey = 1,
//            nextKey = null
//        )
//
//        coEvery {
//            movieRepository.getMoviesByGenre(genreId, 1, any())
//        } returns page1Movies
//        coEvery {
//            movieRepository.getMoviesByGenre(genreId, 2, any())
//        } returns page2Movies
//
//        val page1Result = getMoviesByGenreUseCase(genreId, 1, PAGE_SIZE)
//        val page2Result = getMoviesByGenreUseCase(genreId, 2, PAGE_SIZE)
//
//        assertThat(page1Result.data).hasSize(2)
//        assertThat(page2Result.data).hasSize(1)
//        assertThat(page1Result.nextKey).isEqualTo(2)
//        assertThat(page2Result.prevKey).isEqualTo(1)
//    }
//
//    @Test
//    fun `getMoviesByGenre() should propagate exception when repository throws`() = runTest {
//        val genreId = 1L
//        val page = 1
//        val expectedException = RuntimeException("Network error")
//
//        coEvery {
//            movieRepository.getMoviesByGenre(genreId, page, any())
//        } throws expectedException
//
//        assertThrows<RuntimeException> {
//            getMoviesByGenreUseCase(genreId, page, PAGE_SIZE)
//        }.apply {
//            assertThat(message).isEqualTo("Network error")
//        }
//    }
//
//    companion object{
//        private const val PAGE_SIZE = 20
//    }
//}