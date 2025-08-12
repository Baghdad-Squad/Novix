package com.baghdad.repository

import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.dummyData.DummyDataFactory.createMockCastMember
import com.baghdad.repository.dummyData.DummyDataFactory.createMockCastMemberDto
import com.baghdad.repository.dummyData.DummyDataFactory.createMockGenre
import com.baghdad.repository.dummyData.DummyDataFactory.createMockGenreDto
import com.baghdad.repository.dummyData.DummyDataFactory.createMockReview
import com.baghdad.repository.dummyData.DummyDataFactory.createMockReviewDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Locale

class MovieRepositoryImplTest {
    private lateinit var remoteGenreDataSource: RemoteGenreDataSource
    private lateinit var remoteMovieDataSource: RemoteMovieDataSource
    private lateinit var movieRepositoryImpl: MovieRepositoryImpl
    private lateinit var savableMovieDataSource: LocalSavableMovieDataSource
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        remoteGenreDataSource = mockk()
        remoteMovieDataSource = mockk()
        savableMovieDataSource = mockk()
        authenticationRepository = mockk()
        movieRepositoryImpl = MovieRepositoryImpl(
            remoteGenreDataSource = remoteGenreDataSource,
            remoteMovieDataSource = remoteMovieDataSource,
            savableMovieDataSource = savableMovieDataSource,
            authenticationRepository = authenticationRepository
        )
    }

    @Test
    fun `getGenres should return list of genres when remote call succeeds`() = runTest {
        val mockGenreDtos = listOf(
            createMockGenreDto(1L, "Action"),
            createMockGenreDto(2L, "Comedy")
        )
        val expectedGenres = listOf(
            createMockGenre(1L, "Action"),
            createMockGenre(2L, "Comedy")
        )
        coEvery { remoteGenreDataSource.getMovieGenre(language = any()) } returns mockGenreDtos

        val result = movieRepositoryImpl.getGenres()

        assertThat(result).isEqualTo(expectedGenres)
        coVerify { remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language) }
    }

    @Test
    fun `getGenres should return empty list when no genres found`() = runTest {
        coEvery { remoteGenreDataSource.getMovieGenre(language = any()) } returns emptyList()

        val result = movieRepositoryImpl.getGenres()

        assertThat(result).isEqualTo(emptyList<Genre>())
        coVerify { remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language) }
    }

    @Test
    fun `getSimilarMovies should return empty list when no similar movies found`() = runTest {
        val movieId = 123L
        coEvery { remoteMovieDataSource.getSimilarMovies(movieId) } returns emptyList()

        val result = movieRepositoryImpl.getSimilarMovies(movieId)

        assertThat(result).isEqualTo(emptyList<Movie>())
        coVerify { remoteMovieDataSource.getSimilarMovies(movieId) }
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

            assertThat(result).isEqualTo(expectedCastMembers)
            coVerify { remoteMovieDataSource.getMovieCastMembers(movieId) }
        }

    @Test
    fun `getMovieCastMembers should return empty list when no cast members found`() = runTest {
        val movieId = 456L
        coEvery { remoteMovieDataSource.getMovieCastMembers(movieId) } returns emptyList()

        val result = movieRepositoryImpl.getMovieCastMembers(movieId)

        assertThat(result).isEqualTo(emptyList<CastMember>())
        coVerify { remoteMovieDataSource.getMovieCastMembers(movieId) }
    }

    @Test
    fun `getMoviesByGenre should return empty paged result when no movies found`() = runTest {
        val genreId = 28L
        val page = 1
        val pageSize = 20
        val emptyPagedResult = PagedResultDto<MovieDto>(emptyList(), nextKey = null, prevKey = null)
        coEvery { remoteMovieDataSource.getMoviesByGenre(genreId, page) } returns emptyPagedResult

        val result = movieRepositoryImpl.getMoviesByGenre(genreId, page, pageSize)

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
        coVerify { remoteMovieDataSource.getMoviesByGenre(genreId, page) }
    }

    @Test
    fun `getMoviesByGenre should throw exception when remote call fails`() = runTest {
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
        val movieId = 123L
        val mockReviewDtos = listOf(createMockReviewDto())
        val expectedReviews = listOf(createMockReview())
        coEvery { remoteMovieDataSource.getMovieReviews(movieId) } returns mockReviewDtos

        val result = movieRepositoryImpl.getMovieReviews(movieId)

        assertThat(result).isEqualTo(expectedReviews)
        coVerify { remoteMovieDataSource.getMovieReviews(movieId) }
    }

    @Test
    fun `getMovieReviews should return empty list when no reviews found`() = runTest {
        val movieId = 123L
        coEvery { remoteMovieDataSource.getMovieReviews(movieId) } returns emptyList()

        val result = movieRepositoryImpl.getMovieReviews(movieId)

        assertThat(result).isEmpty()
        coVerify { remoteMovieDataSource.getMovieReviews(movieId) }
    }

    @Test
    fun `getMovieReviews should throw exception when remote call fails`() = runTest {
        val movieId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getMovieReviews(movieId) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getMovieReviews(movieId)
        }
    }

    @Test
    fun `getMovieImages should return list of image urls when remote call succeeds`() = runTest {
        val movieId = 123L
        val mockImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg")
        coEvery { remoteMovieDataSource.getMovieImages(movieId) } returns mockImages

        val result = movieRepositoryImpl.getMovieImages(movieId)

        assertThat(result).isEqualTo(mockImages)
        coVerify { remoteMovieDataSource.getMovieImages(movieId) }
    }

    @Test
    fun `getMovieImages should return empty list when no images found`() = runTest {
        val movieId = 123L
        coEvery { remoteMovieDataSource.getMovieImages(movieId) } returns emptyList()

        val result = movieRepositoryImpl.getMovieImages(movieId)

        assertThat(result).isEmpty()
        coVerify { remoteMovieDataSource.getMovieImages(movieId) }
    }

    @Test
    fun `getMovieImages should throw exception when remote call fails`() = runTest {
        val movieId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getMovieImages(movieId) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getMovieImages(movieId)
        }
    }

    @Test
    fun `getPopularMovies should return empty list when no popular movies found`() = runTest {
        coEvery { remoteMovieDataSource.getPopularMovies() } returns emptyList()

        val result = movieRepositoryImpl.getPopularMovies()

        assertThat(result).isEmpty()
        coVerify { remoteMovieDataSource.getPopularMovies() }
    }

    @Test
    fun `getUpcomingMovies should return empty list when no upcoming movies found`() = runTest {
        val genreId = 28L
        coEvery { remoteMovieDataSource.getUpcomingMovies(genreId) } returns emptyList()

        val result = movieRepositoryImpl.getUpcomingMovies(genreId)

        assertThat(result).isEmpty()
        coVerify { remoteMovieDataSource.getUpcomingMovies(genreId) }
    }
}