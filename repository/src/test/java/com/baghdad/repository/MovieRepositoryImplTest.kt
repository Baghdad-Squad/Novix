package com.baghdad.repository

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.Review
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.local.LocalGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.dummyData.DummyDataFactory.createMockCastMember
import com.baghdad.repository.dummyData.DummyDataFactory.createMockCastMemberDto
import com.baghdad.repository.dummyData.DummyDataFactory.createMockGenre
import com.baghdad.repository.dummyData.DummyDataFactory.createMockGenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Locale

class MovieRepositoryImplTest {
    private lateinit var remoteGenreDataSource: RemoteGenreDataSource
    private lateinit var localGenreDataSource: LocalGenreDataSource
    private lateinit var remoteMovieDataSource: RemoteMovieDataSource
    private lateinit var movieRepositoryImpl: MovieRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteGenreDataSource = mockk()
        localGenreDataSource = mockk()
        remoteMovieDataSource = mockk()
        movieRepositoryImpl = MovieRepositoryImpl(
            remoteGenreDataSource = remoteGenreDataSource,
            localGenreDataSource = localGenreDataSource,
            remoteMovieDataSource = remoteMovieDataSource
        )
    }

    @Test
    fun `getGenres should return list of genres when remote call succeeds`() = runTest {
        // Given
        val mockGenreDtos = listOf(
            createMockGenreDto(1L, "Action"),
            createMockGenreDto(2L, "Comedy")
        )
        val expectedGenres = listOf(
            createMockGenre(1L, "Action"),
            createMockGenre(2L, "Comedy")
        )

        coEvery { remoteGenreDataSource.getMovieGenre(language = any()) } returns mockGenreDtos

        // When
        val result = movieRepositoryImpl.getGenres()

        // Then
        assertThat(result.size).isEqualTo(expectedGenres.size)
        assertThat(result[0].id).isEqualTo(expectedGenres[0].id)
        assertThat(result[0].name).isEqualTo(expectedGenres[0].name)
        assertThat(result[1].id).isEqualTo(expectedGenres[1].id)
        assertThat(result[1].name).isEqualTo(expectedGenres[1].name)
        coVerify { remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language) }
    }

    @Test
    fun `getGenres should return empty list when no genres found`() = runTest {
        // Given
        coEvery { remoteGenreDataSource.getMovieGenre(language = any()) } returns emptyList()

        // When
        val result = movieRepositoryImpl.getGenres()

        // Then
        assertThat(result).isEqualTo(emptyList<Genre>())
        coVerify { remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language) }
    }

    @Test
    fun `getGenres should throw exception when remote call fails`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { remoteGenreDataSource.getMovieGenre(language = any()) } throws exception

        // When & Then
        assertThrows<Exception> {
            movieRepositoryImpl.getGenres()
        }
    }

    @Test
    fun `getSimilarMovies should return list of movies when remote call succeeds`() = runTest {
        // Given
        val movieId = 123L
        val mockMovieDtos = listOf(createMockMovieDto())
        val expectedMovies = listOf(createMockMovie())

        coEvery { remoteMovieDataSource.getSimilarMovies(movieId) } returns mockMovieDtos

        // When
        val result = movieRepositoryImpl.getSimilarMovies(movieId)

        // Then
        assertThat(result.size).isEqualTo(expectedMovies.size)
        val movie = result[0]
        assertThat(movie.id).isEqualTo(456L)
        assertThat(movie.title).isEqualTo("Test Movie")
        assertThat(movie.genres.size).isEqualTo(1)
        assertThat(movie.averageRating).isEqualTo(8.0)
        assertThat(movie.userRating).isEqualTo(7.5)
        assertThat(movie.releaseDate).isEqualTo(LocalDate.parse("2023-01-01"))
        assertThat(movie.overview).isEqualTo("Test movie overview")
        assertThat(movie.posterImageURL).isEqualTo("/movie_poster.jpg")
        assertThat(movie.runtimeMinutes).isEqualTo(120)
        coVerify { remoteMovieDataSource.getSimilarMovies(movieId) }
    }

    @Test
    fun `getSimilarMovies should return empty list when no similar movies found`() = runTest {
        // Given
        val movieId = 123L
        coEvery { remoteMovieDataSource.getSimilarMovies(movieId) } returns emptyList()

        // When
        val result = movieRepositoryImpl.getSimilarMovies(movieId)

        // Then
        assertThat(result).isEqualTo(emptyList<Movie>())
        coVerify { remoteMovieDataSource.getSimilarMovies(movieId) }
    }

    @Test
    fun `getSimilarMovies should throw exception when remote call fails`() = runTest {
        // Given
        val movieId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getSimilarMovies(movieId) } throws exception

        // When & Then
        assertThrows<Exception> {
            movieRepositoryImpl.getSimilarMovies(movieId)
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

        // When
        val result = movieRepositoryImpl.getMovieDetails(movieId)

        // Then
        assertThat(result.id).isEqualTo(expectedMovie.id)
        assertThat(result.title).isEqualTo(expectedMovie.title)
        assertThat(result.trailerURL).isEqualTo(expectedMovie.trailerURL)
        coVerify { remoteMovieDataSource.getMovieDetails(movieId) }
        coVerify { remoteMovieDataSource.getMovieTrailer(movieId) }
    }

    @Test
    fun `getMovieDetails should throw exception when remote call fails`() = runTest {
        // Given
        val movieId = 456L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getMovieDetails(movieId) } throws exception

        // When & Then
        assertThrows<Exception> {
            movieRepositoryImpl.getMovieDetails(movieId)
        }
    }

    @Test
    fun `getMovieCastMembers should return list of cast members when remote call succeeds`() =
        runTest {
            // Given
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

            // When
            val result = movieRepositoryImpl.getMovieCastMembers(movieId)

            // Then
            assertThat(result.size).isEqualTo(expectedCastMembers.size)
            assertThat(result[0].actor.id).isEqualTo(expectedCastMembers[0].actor.id)
            assertThat(result[0].actor.name).isEqualTo(expectedCastMembers[0].actor.name)
            assertThat(result[0].characterName).isEqualTo(expectedCastMembers[0].characterName)
            assertThat(result[1].actor.id).isEqualTo(expectedCastMembers[1].actor.id)
            assertThat(result[1].actor.name).isEqualTo(expectedCastMembers[1].actor.name)
            assertThat(result[1].characterName).isEqualTo(expectedCastMembers[1].characterName)
            coVerify { remoteMovieDataSource.getMovieCastMembers(movieId) }
        }

    @Test
    fun `getMovieCastMembers should return empty list when no cast members found`() = runTest {
        // Given
        val movieId = 456L
        coEvery { remoteMovieDataSource.getMovieCastMembers(movieId) } returns emptyList()

        // When
        val result = movieRepositoryImpl.getMovieCastMembers(movieId)

        // Then
        assertThat(result).isEqualTo(emptyList<CastMember>())
        coVerify { remoteMovieDataSource.getMovieCastMembers(movieId) }
    }

    @Test
    fun `getMovieCastMembers should throw exception when remote call fails`() = runTest {
        // Given
        val movieId = 456L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getMovieCastMembers(movieId) } throws exception

        // When & Then
        assertThrows<Exception> {
            movieRepositoryImpl.getMovieCastMembers(movieId)
        }
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

        // When
        val result = movieRepositoryImpl.getMoviesByGenre(genreId, page, pageSize)

        // Then
        assertThat(result.data.size).isEqualTo(1)
        assertThat(result.data[0].id).isEqualTo(456L)
        assertThat(result.data[0].title).isEqualTo("Test Movie")
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
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

        // When
        val result = movieRepositoryImpl.getMoviesByGenre(genreId, page, pageSize)

        // Then
        assertThat(result.data.size).isEqualTo(0)
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
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

        // When & Then
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

        // When
        val result = movieRepositoryImpl.getMovieReviews(movieId)

        // Then
        assertThat(result.size).isEqualTo(expectedReviews.size)
        assertThat(result[0].id).isEqualTo(expectedReviews[0].id)
        assertThat(result[0].contentTitle).isEqualTo(expectedReviews[0].contentTitle)
        assertThat(result[0].authorName).isEqualTo(expectedReviews[0].authorName)
        coVerify { remoteMovieDataSource.getMovieReviews(movieId) }
    }

    @Test
    fun `getMovieReviews should return empty list when no reviews found`() = runTest {
        // Given
        val movieId = 123L
        coEvery { remoteMovieDataSource.getMovieReviews(movieId) } returns emptyList()

        // When
        val result = movieRepositoryImpl.getMovieReviews(movieId)

        // Then
        assertThat(result).isEqualTo(emptyList<Review>())
        coVerify { remoteMovieDataSource.getMovieReviews(movieId) }
    }

    @Test
    fun `getMovieReviews should throw exception when remote call fails`() = runTest {
        // Given
        val movieId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getMovieReviews(movieId) } throws exception

        // When & Then
        assertThrows<Exception> {
            movieRepositoryImpl.getMovieReviews(movieId)
        }
    }

    @Test
    fun `getMovieImages should return list of image urls when remote call succeeds`() = runTest {
        // Given
        val movieId = 123L
        val mockImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg")

        coEvery { remoteMovieDataSource.getMovieImages(movieId) } returns mockImages

        // When
        val result = movieRepositoryImpl.getMovieImages(movieId)

        // Then
        assertThat(result).isEqualTo(mockImages)
        coVerify { remoteMovieDataSource.getMovieImages(movieId) }
    }

    @Test
    fun `getMovieImages should return empty list when no images found`() = runTest {
        // Given
        val movieId = 123L
        coEvery { remoteMovieDataSource.getMovieImages(movieId) } returns emptyList()

        // When
        val result = movieRepositoryImpl.getMovieImages(movieId)

        // Then
        assertThat(result).isEqualTo(emptyList<String>())
        coVerify { remoteMovieDataSource.getMovieImages(movieId) }
    }

    @Test
    fun `getMovieImages should throw exception when remote call fails`() = runTest {
        // Given
        val movieId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getMovieImages(movieId) } throws exception

        // When & Then
        assertThrows<Exception> {
            movieRepositoryImpl.getMovieImages(movieId)
        }
    }

    @Test
    fun `getTopRatedMovies should return paged result when remote call succeeds`() = runTest {
        // Given
        val page = 1
        val mockMovieDtos = listOf(createMockMovieDto())
        val mockPagedResult = PagedResultDto(mockMovieDtos, nextKey = 2, prevKey = null)

        coEvery { remoteMovieDataSource.getTopRatedMovies(page) } returns mockPagedResult

        // When
        val result = movieRepositoryImpl.getTopRatedMovies(page)

        // Then
        assertThat(result.data.size).isEqualTo(1)
        assertThat(result.data[0].id).isEqualTo(456L)
        assertThat(result.data[0].title).isEqualTo("Test Movie")
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify { remoteMovieDataSource.getTopRatedMovies(page) }
    }

    @Test
    fun `getPopularMovies should return list of movies when remote call succeeds`() = runTest {
        // Given
        val mockMovieDtos = listOf(createMockMovieDto())
        val expectedMovies = listOf(createMockMovie())

        coEvery { remoteMovieDataSource.getPopularMovies() } returns mockMovieDtos

        // When
        val result = movieRepositoryImpl.getPopularMovies()

        // Then
        assertThat(result.size).isEqualTo(expectedMovies.size)
        assertThat(result[0].id).isEqualTo(expectedMovies[0].id)
        assertThat(result[0].title).isEqualTo(expectedMovies[0].title)
        coVerify { remoteMovieDataSource.getPopularMovies() }
    }

    @Test
    fun `getPopularMovies should return empty list when no popular movies found`() = runTest {
        // Given
        coEvery { remoteMovieDataSource.getPopularMovies() } returns emptyList()

        // When
        val result = movieRepositoryImpl.getPopularMovies()

        // Then
        assertThat(result).isEqualTo(emptyList<Movie>())
        coVerify { remoteMovieDataSource.getPopularMovies() }
    }


    @Test
    fun `getTrendingMovies should return paged result when remote call succeeds`() = runTest {
        // Given
        val page = 1
        val mockMovieDtos = listOf(createMockMovieDto())
        val mockPagedResult = PagedResultDto(mockMovieDtos, nextKey = 2, prevKey = null)

        coEvery { remoteMovieDataSource.getTrendingMovies(page) } returns mockPagedResult

        // When
        val result = movieRepositoryImpl.getTrendingMovies(page)

        // Then
        assertThat(result.data.size).isEqualTo(1)
        assertThat(result.data[0].id).isEqualTo(456L)
        assertThat(result.data[0].title).isEqualTo("Test Movie")
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify { remoteMovieDataSource.getTrendingMovies(page) }
    }

    @Test
    fun `getUpcomingMovies should return list of movies when remote call succeeds`() = runTest {
        // Given
        val genreId = 28L
        val mockMovieDtos = listOf(createMockMovieDto())
        val expectedMovies = listOf(createMockMovie())

        coEvery { remoteMovieDataSource.getUpcomingMovies(genreId) } returns mockMovieDtos

        // When
        val result = movieRepositoryImpl.getUpcomingMovies(genreId)

        // Then
        assertThat(result.size).isEqualTo(expectedMovies.size)
        assertThat(result[0].id).isEqualTo(expectedMovies[0].id)
        assertThat(result[0].title).isEqualTo(expectedMovies[0].title)
        coVerify { remoteMovieDataSource.getUpcomingMovies(genreId) }
    }

    @Test
    fun `getUpcomingMovies should return list of movies when genreId is null`() = runTest {
        // Given
        val genreId = null
        val mockMovieDtos = listOf(createMockMovieDto())
        val expectedMovies = listOf(createMockMovie())

        coEvery { remoteMovieDataSource.getUpcomingMovies(genreId) } returns mockMovieDtos

        // When
        val result = movieRepositoryImpl.getUpcomingMovies(genreId)

        // Then
        assertThat(result.size).isEqualTo(expectedMovies.size)
        assertThat(result[0].id).isEqualTo(expectedMovies[0].id)
        assertThat(result[0].title).isEqualTo(expectedMovies[0].title)
        coVerify { remoteMovieDataSource.getUpcomingMovies(genreId) }
    }

    @Test
    fun `getUpcomingMovies should return empty list when no upcoming movies found`() = runTest {
        // Given
        val genreId = 28L
        coEvery { remoteMovieDataSource.getUpcomingMovies(genreId) } returns emptyList()

        // When
        val result = movieRepositoryImpl.getUpcomingMovies(genreId)

        // Then
        assertThat(result).isEqualTo(emptyList<Movie>())
        coVerify { remoteMovieDataSource.getUpcomingMovies(genreId) }
    }

    companion object {

        private fun createMockMovieDto() = MovieDto(
            id = 456L,
            title = "Test Movie",
            genres = listOf(createMockGenreDto(28L, "Action")),
            imdbRating = 8.0,
            userRating = 7.5,
            releaseDate = "2023-01-01",
            overview = "Test movie overview",
            posterPictureURL = "/movie_poster.jpg",
            runtimeMinutes = 120,
            trailerURL = " "
        )

        private fun createMockMovie() = Movie(
            id = 456L,
            title = "Test Movie",
            genres = listOf(createMockGenre(28L, "Action")),
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
            rating = 9.0,
            authorAvatarUrl = "https://example.com/avatar.jpg",
            reviewText = "This movie was fantastic! The plot was engaging and the acting was top-notch.",
            postedDate = LocalDate.parse("2023-01-01").toString()
        )

        private fun createMockReview() = Review(
            id = "review123",
            contentTitle = "Great movie! Highly recommended.",
            authorName = "John Reviewer",
            rating = 9.0,
            authorAvatarUrl = "https://example.com/avatar.jpg",
            reviewText = "This movie was fantastic! The plot was engaging and the acting was top-notch.",
            postedDate = LocalDate.parse("2023-01-01")
        )
    }
}