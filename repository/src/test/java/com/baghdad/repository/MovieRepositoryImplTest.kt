package com.baghdad.repository

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.Review
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.DummyDataFactory.createMockCastMember
import com.baghdad.repository.DummyDataFactory.createMockCastMemberDto
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Locale

class MovieRepositoryImplTest {

    private lateinit var remoteGenreDataSource: RemoteGenreDataSource
    private lateinit var remoteMovieDataSource: RemoteMovieDataSource
    private lateinit var movieRepositoryImpl: MovieRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteGenreDataSource = mockk()
        remoteMovieDataSource = mockk()
        movieRepositoryImpl = MovieRepositoryImpl(
            remoteGenreDataSource = remoteGenreDataSource,
            remoteMovieDataSource = remoteMovieDataSource,
            localGenreDataSource = mockk()
        )
    }

    @Test
    fun `getGenres should return list of genres when remote call succeeds`() = runTest {

        val mockGenreDtos = listOf(
            createMockGenreDto(1, "Action"),
            createMockGenreDto(2, "Comedy")
        )
        val expectedGenres = listOf(
            createMockGenre(1, "Action"),
            createMockGenre(2, "Comedy")
        )

        coEvery { remoteGenreDataSource.getMovieGenre(language = any()) } returns mockGenreDtos

        val result = movieRepositoryImpl.getGenres()

        assertEquals(expectedGenres.size, result.size)
        assertEquals(expectedGenres[0].id, result[0].id)
        assertEquals(expectedGenres[0].name, result[0].name)
        assertEquals(expectedGenres[1].id, result[1].id)
        assertEquals(expectedGenres[1].name, result[1].name)
        coVerify { remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language) }
    }

    @Test
    fun `getGenres should return empty list when no genres found`() = runTest {

        coEvery { remoteGenreDataSource.getMovieGenre(language = any()) } returns emptyList()

        val result = movieRepositoryImpl.getGenres()

        assertEquals(emptyList<Genre>(), result)
        coVerify { remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language) }
    }

    @Test
    fun `getGenres should throw exception when remote call fails`() = runTest {

        val exception = RuntimeException("Network error")
        coEvery { remoteGenreDataSource.getMovieGenre(language = any()) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getGenres()
        }
    }

    @Test
    fun `getSimilarMovies should return list of movies when remote call succeeds`() = runTest {

        val movieId = 123L
        val mockMovieDtos = listOf(createMockMovieDto())
        val expectedMovies = listOf(createMockMovie())

        coEvery { remoteMovieDataSource.getSimilarMovies(movieId) } returns mockMovieDtos

        val result = movieRepositoryImpl.getSimilarMovies(movieId)

        assertEquals(expectedMovies.size, result.size)
        val movie = result[0]
        assertEquals(456L, movie.id)
        assertEquals("Test Movie", movie.title)
        assertEquals(1, movie.genres.size)
        assertEquals(8.0, movie.averageRating)
        assertEquals(7.5, movie.userRating)
        assertEquals(LocalDate.parse("2023-01-01"), movie.releaseDate)
        assertEquals("Test movie overview", movie.overview)
        assertEquals("/movie_poster.jpg", movie.posterImageURL)
        assertEquals(120, movie.runtimeMinutes)
        coVerify { remoteMovieDataSource.getSimilarMovies(movieId) }
    }

    @Test
    fun `getSimilarMovies should return empty list when no similar movies found`() = runTest {

        val movieId = 123L
        coEvery { remoteMovieDataSource.getSimilarMovies(movieId) } returns emptyList()

        val result = movieRepositoryImpl.getSimilarMovies(movieId)

        assertEquals(emptyList<Movie>(), result)
        coVerify { remoteMovieDataSource.getSimilarMovies(movieId) }
    }

    @Test
    fun `getSimilarMovies should throw exception when remote call fails`() = runTest {

        val movieId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getSimilarMovies(movieId) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getSimilarMovies(movieId)
        }
    }

    @Test
    fun `getMovieDetails should throw exception when remote call fails`() = runTest {

        val movieId = 456L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getMovieDetails(movieId) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getMovieDetails(movieId)
        }
    }

    @Test
    fun `getMovieCastMembers should return list of cast members when remote call succeeds`() =
        runTest {

            val movieId = 456L
            val mockCastMemberDtos = listOf(
                createMockCastMemberDto(),
                createMockCastMemberDto()
            )
            val expectedCastMembers = listOf(
                createMockCastMember(),
                createMockCastMember()
            )

            coEvery { remoteMovieDataSource.getMovieCastMembers(movieId) } returns mockCastMemberDtos

            val result = movieRepositoryImpl.getMovieCastMembers(movieId)

            assertEquals(expectedCastMembers.size, result.size)
            assertEquals(expectedCastMembers[0].actor.id, result[0].actor.id)
            assertEquals(expectedCastMembers[0].actor.name, result[0].actor.name)
            assertEquals(expectedCastMembers[0].characterName, result[0].characterName)

            assertEquals(expectedCastMembers[1].actor.id, result[1].actor.id)
            assertEquals(expectedCastMembers[1].actor.name, result[1].actor.name)
            assertEquals(expectedCastMembers[1].characterName, result[1].characterName)

            coVerify { remoteMovieDataSource.getMovieCastMembers(movieId) }
        }

    @Test
    fun `getMovieCastMembers should return empty list when no cast members found`() = runTest {

        val movieId = 456L
        coEvery { remoteMovieDataSource.getMovieCastMembers(movieId) } returns emptyList()

        val result = movieRepositoryImpl.getMovieCastMembers(movieId)

        assertEquals(emptyList<CastMember>(), result)
        coVerify { remoteMovieDataSource.getMovieCastMembers(movieId) }
    }

    @Test
    fun `getMovieCastMembers should throw exception when remote call fails`() = runTest {

        val movieId = 456L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getMovieCastMembers(movieId) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getMovieCastMembers(movieId)
        }
    }

    @Test
    fun `getMovieDetails should return movie with trailer when remote call succeeds`() = runTest {
        // Given
        val movieId = 123L
        val mockMovieDto = createMockMovieDto()
        val mockTrailerUrl = "https://youtube.com/watch?v=trailer123"
        val expectedMovie = createMockMovie().copy(trailerURL = mockTrailerUrl)

        coEvery { remoteMovieDataSource.getMovieDetails(movieId) } returns mockMovieDto
        coEvery { remoteMovieDataSource.getMovieTrailer(movieId) } returns mockTrailerUrl

        val result = movieRepositoryImpl.getMovieDetails(movieId)

        assertEquals(expectedMovie.id, result.id)
        assertEquals(expectedMovie.title, result.title)
        assertEquals(expectedMovie.trailerURL, result.trailerURL)
        coVerify { remoteMovieDataSource.getMovieDetails(movieId) }
        coVerify { remoteMovieDataSource.getMovieTrailer(movieId) }
    }

    @Test
    fun `getMoviesByGenre should return paged result when remote call succeeds`() = runTest {
        // Given
        val genreId = 28L
        val page = 1
        val pageSize = 20
        val mockMovieDtos = listOf(createMockMovieDto())
        val mockPagedResult = PagedResultDto(mockMovieDtos, nextKey = 2, prevKey = null)

        coEvery { remoteMovieDataSource.getMoviesByGenre(genreId, page) } returns mockPagedResult

        val result = movieRepositoryImpl.getMoviesByGenre(genreId, page, pageSize)

        assertEquals(1, result.data.size)
        assertEquals(456L, result.data[0].id)
        assertEquals("Test Movie", result.data[0].title)
        assertEquals(2, result.nextKey)
        assertEquals(null, result.prevKey)
        coVerify { remoteMovieDataSource.getMoviesByGenre(genreId, page) }
    }

    @Test
    fun `getMoviesByGenre should return empty paged result when no movies found`() = runTest {
        // Given
        val genreId = 28L
        val page = 1
        val pageSize = 20
        val emptyPagedResult = PagedResultDto<MovieDto>(emptyList(), nextKey = null, prevKey = null)

        coEvery { remoteMovieDataSource.getMoviesByGenre(genreId, page) } returns emptyPagedResult

        val result = movieRepositoryImpl.getMoviesByGenre(genreId, page, pageSize)

        assertEquals(0, result.data.size)
        assertEquals(null, result.nextKey)
        assertEquals(null, result.prevKey)
        coVerify { remoteMovieDataSource.getMoviesByGenre(genreId, page) }
    }

    @Test
    fun `getMoviesByGenre should throw exception when remote call fails`() = runTest {
        // Given
        val genreId = 28L
        val page = 1
        val pageSize = 20
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getMoviesByGenre(genreId, page) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getMoviesByGenre(genreId, page, pageSize)
        }
    }

    @Test
    fun `getMovieReviews should return list of reviews when remote call succeeds`() = runTest {
        // Given
        val movieId = 123L
        val mockReviewDtos = listOf(createMockReviewDto())
        val expectedReviews = listOf(createMockReview())

        coEvery { remoteMovieDataSource.getMovieReviews(movieId) } returns mockReviewDtos

        val result = movieRepositoryImpl.getMovieReviews(movieId)

        assertEquals(expectedReviews.size, result.size)
        assertEquals(expectedReviews[0].id, result[0].id)
        assertEquals(expectedReviews[0].contentTitle, result[0].contentTitle)
        assertEquals(expectedReviews[0].authorName, result[0].authorName)
        coVerify { remoteMovieDataSource.getMovieReviews(movieId) }
    }

    @Test
    fun `getMovieReviews should return empty list when no reviews found`() = runTest {
        // Given
        val movieId = 123L
        coEvery { remoteMovieDataSource.getMovieReviews(movieId) } returns emptyList()

        val result = movieRepositoryImpl.getMovieReviews(movieId)

        assertEquals(emptyList<Review>(), result)
        coVerify { remoteMovieDataSource.getMovieReviews(movieId) }
    }

    @Test
    fun `getMovieImages should return list of image urls when remote call succeeds`() = runTest {
        // Given
        val movieId = 123L
        val mockImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg")

        coEvery { remoteMovieDataSource.getMovieImages(movieId) } returns mockImages

        val result = movieRepositoryImpl.getMovieImages(movieId)

        assertEquals(mockImages, result)
        coVerify { remoteMovieDataSource.getMovieImages(movieId) }
    }

    @Test
    fun `getMovieImages should return empty list when no images found`() = runTest {
        // Given
        val movieId = 123L
        coEvery { remoteMovieDataSource.getMovieImages(movieId) } returns emptyList()

        val result = movieRepositoryImpl.getMovieImages(movieId)

        assertEquals(emptyList<String>(), result)
        coVerify { remoteMovieDataSource.getMovieImages(movieId) }
    }

    @Test
    fun `getTopRatedMovies should return paged result when remote call succeeds`() = runTest {
        // Given
        val page = 1
        val mockMovieDtos = listOf(createMockMovieDto())
        val mockPagedResult = PagedResultDto(mockMovieDtos, nextKey = 2, prevKey = null)

        coEvery { remoteMovieDataSource.getTopRatedMovies(page) } returns mockPagedResult

        val result = movieRepositoryImpl.getTopRatedMovies(page)

        assertEquals(1, result.data.size)
        assertEquals(456L, result.data[0].id)
        assertEquals("Test Movie", result.data[0].title)
        assertEquals(2, result.nextKey)
        assertEquals(null, result.prevKey)
        coVerify { remoteMovieDataSource.getTopRatedMovies(page) }
    }

    @Test
    fun `getTopRatedMovies should throw exception when remote call fails`() = runTest {
        // Given
        val page = 1
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getTopRatedMovies(page) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getTopRatedMovies(page)
        }
    }

    @Test
    fun `getPopularMovies should return list of movies when remote call succeeds`() = runTest {
        // Given
        val mockMovieDtos = listOf(createMockMovieDto())
        val expectedMovies = listOf(createMockMovie())

        coEvery { remoteMovieDataSource.getPopularMovies() } returns mockMovieDtos

        val result = movieRepositoryImpl.getPopularMovies()

        assertEquals(expectedMovies.size, result.size)
        assertEquals(expectedMovies[0].id, result[0].id)
        assertEquals(expectedMovies[0].title, result[0].title)
        coVerify { remoteMovieDataSource.getPopularMovies() }
    }

    @Test
    fun `getPopularMovies should return empty list when no popular movies found`() = runTest {
        // Given
        coEvery { remoteMovieDataSource.getPopularMovies() } returns emptyList()

        val result = movieRepositoryImpl.getPopularMovies()

        assertEquals(emptyList<Movie>(), result)
        coVerify { remoteMovieDataSource.getPopularMovies() }
    }

    @Test
    fun `getTrendingMovies should return paged result when remote call succeeds`() = runTest {
        // Given
        val page = 1
        val mockMovieDtos = listOf(createMockMovieDto())
        val mockPagedResult = PagedResultDto(mockMovieDtos, nextKey = 2, prevKey = null)

        coEvery { remoteMovieDataSource.getTrendingMovies(page) } returns mockPagedResult

        val result = movieRepositoryImpl.getTrendingMovies(page)

        assertEquals(1, result.data.size)
        assertEquals(456L, result.data[0].id)
        assertEquals("Test Movie", result.data[0].title)
        assertEquals(2, result.nextKey)
        assertEquals(null, result.prevKey)
        coVerify { remoteMovieDataSource.getTrendingMovies(page) }
    }


    @Test
    fun `getUpcomingMovies should return paged result when remote call succeeds`() = runTest {
        // Given
        val page = 1
        val genreId = 28L
        val pageSize = 20
        val mockMovieDtos = listOf(createMockMovieDto())
        val mockPagedResult = PagedResultDto(mockMovieDtos, nextKey = 2, prevKey = null)

        coEvery { remoteMovieDataSource.getUpcomingMovies(page, genreId) } returns mockPagedResult

        val result = movieRepositoryImpl.getUpcomingMovies(page, genreId, pageSize)

        assertEquals(1, result.data.size)
        assertEquals(456L, result.data[0].id)
        assertEquals("Test Movie", result.data[0].title)
        assertEquals(2, result.nextKey)
        assertEquals(null, result.prevKey)
        coVerify { remoteMovieDataSource.getUpcomingMovies(page, genreId) }
    }

    @Test
    fun `getUpcomingMovies should return paged result when genreId is null`() = runTest {
        // Given
        val page = 1
        val genreId = null
        val pageSize = 20
        val mockMovieDtos = listOf(createMockMovieDto())
        val mockPagedResult = PagedResultDto(mockMovieDtos, nextKey = 2, prevKey = null)

        coEvery { remoteMovieDataSource.getUpcomingMovies(page, genreId) } returns mockPagedResult

        val result = movieRepositoryImpl.getUpcomingMovies(page, genreId, pageSize)

        assertEquals(1, result.data.size)
        assertEquals(456L, result.data[0].id)
        assertEquals("Test Movie", result.data[0].title)
        coVerify { remoteMovieDataSource.getUpcomingMovies(page, genreId) }
    }

    @Test
    fun `getUpcomingMovies should throw exception when remote call fails`() = runTest {
        // Given
        val page = 1
        val genreId = 28L
        val pageSize = 20
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getUpcomingMovies(page, genreId) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getUpcomingMovies(page, genreId, pageSize)
        }
    }

    companion object {

        private fun createMockGenreDto(id: Long, name: String) = GenreDto(
            id = id,
            name = name,
            type = GenreDto.GenreType.MOVIE
        )

        private fun createMockMovieDto() = MovieDto(
            id = 456L,
            title = "Test Movie",
            genres = listOf(GenreDto(28, "Action", type = GenreDto.GenreType.MOVIE)),
            imdbRating = 8.0,
            userRating = 7.5,
            releaseDate = "2023-01-01",
            overview = "Test movie overview",
            posterPictureURL = "/movie_poster.jpg",
            runtimeMinutes = 120,
            trailerURL = " ",
        )

        private fun createMockGenre(
            id: Long,
            name: String
        ): Genre {
            return Genre(
                id = id,
                name = name
            )
        }

        private fun createMockMovie() = Movie(
            id = 456L,
            title = "Test Movie",
            genres = listOf(Genre(28, "Action")),
            averageRating = 8.0,
            userRating = 7.5,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Test movie overview",
            posterImageURL = "/movie_poster.jpg",
            runtimeMinutes = 120,
            trailerURL = " "
        )

        private fun createMockReviewDto() = ReviewDto(
            id = "review123",
            contentTitle = "Great movie! Highly recommended.",
            authorName = "John Reviewer",
            rating = 9.0f,
            authorAvatarUrl = "https://example.com/avatar.jpg",
            reviewText = "This movie was fantastic! The plot was engaging and the acting was top-notch.",
            postedDate = LocalDate.parse("2023-01-01").toString()
        )

        private fun createMockReview() = Review(
            id = "review123",
            contentTitle = "Great movie! Highly recommended.",
            authorName = "John Reviewer",
            rating = 9.0f,
            authorAvatarUrl = "https://example.com/avatar.jpg",
            reviewText = "This movie was fantastic! The plot was engaging and the acting was top-notch.",
            postedDate = LocalDate.parse("2023-01-01")
        )
    }
}