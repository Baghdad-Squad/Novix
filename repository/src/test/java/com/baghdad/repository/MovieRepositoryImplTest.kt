package com.baghdad.repository

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.userRating.RatedMedia
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.dummyData.DummyDataFactory
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.toSavableMovie
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toIsMediaRated
import com.baghdad.repository.mapper.toMedia
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto
import com.baghdad.repository.model.UserDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.util.Locale

class MovieRepositoryImplTest {

    private val mockRemoteGenreDataSource: RemoteGenreDataSource = mockk(relaxed = true)
    private val mockRemoteMovieDataSource: RemoteMovieDataSource = mockk(relaxed = true)
    private val mockSavableMovieDataSource: SavableMovieDataSource = mockk(relaxed = true)
    private val mockAuthenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private val movieRepositoryUnderTest: MovieRepositoryImpl = MovieRepositoryImpl(
        remoteGenreDataSource = mockRemoteGenreDataSource,
        remoteMovieDataSource = mockRemoteMovieDataSource,
        savableMovieDataSource = mockSavableMovieDataSource,
        authenticationRepository = mockAuthenticationRepository
    )

    @Test
    fun `getGenres should return mapped genres when remote call succeeds`() = runTest {
        val genreDtos =
            listOf(DummyDataFactory.DummyDataFactory.createMockGenreDto(id = 1, name = "Action"))
        val expectedGenres =
            listOf(DummyDataFactory.DummyDataFactory.createMockGenre(id = 1, name = "Action"))

        mockGetMovieGenres(genreDtos)

        val result = movieRepositoryUnderTest.getGenres()

        assertThat(result).isEqualTo(expectedGenres)
        verifyGetMovieGenres()
    }

    @Test
    fun `getMovieCastMembers should return mapped cast members when remote call succeeds`() =
        runTest {
            val movieId = 1L
            val castMemberDtos = listOf(DummyDataFactory.DummyDataFactory.createMockCastMemberDto())
            val expectedCastMembers =
                listOf(DummyDataFactory.DummyDataFactory.createMockCastMember())

            mockGetMovieCastMembers(movieId, castMemberDtos)

            val result = movieRepositoryUnderTest.getMovieCastMembers(movieId)

            assertThat(result).isEqualTo(expectedCastMembers)
            verifyGetMovieCastMembers(movieId)
        }

    @Test
    fun `getMovieImages should return image URLs when remote call succeeds`() = runTest {
        val movieId = 1L
        val expectedImages = listOf("/image1.jpg", "/image2.jpg")

        mockGetMovieImages(movieId, expectedImages)

        val result = movieRepositoryUnderTest.getMovieImages(movieId)

        assertThat(result).isEqualTo(expectedImages)
        verifyGetMovieImages(movieId)
    }


    @Test
    fun `deleteMovieRate should call remote data source when executed`() = runTest {
        val movieId = 1L
        mockDeleteMovieRate(movieId)

        movieRepositoryUnderTest.deleteMovieRate(movieId)

        verifyDeleteMovieRate(movieId)
    }

    @Test
    fun `addMovieRate should call remote data source with correct parameters`() = runTest {
        val movieId = 1L
        val rating = 8
        mockAddMovieRate(movieId, rating)

        movieRepositoryUnderTest.addMovieRate(movieId, rating)

        verifyAddMovieRate(movieId, rating)
    }

    @Test
    fun `getMovieReviews should return mapped reviews when remote call succeeds`() = runTest {
        val movieId = 1L
        val reviewDtos = listOf(DummyDataFactory.DummyDataFactory.createMockReviewDto())
        val expectedReviews = listOf(DummyDataFactory.DummyDataFactory.createMockReview())

        mockGetMovieReviews(movieId, reviewDtos)

        val result = movieRepositoryUnderTest.getMovieReviews(movieId)

        assertThat(result).isEqualTo(expectedReviews)
        verifyGetMovieReviews(movieId)
    }

    @Test
    fun `getPopularMovies should return mapped saved movies when remote call succeeds`() = runTest {
        val savedMovies = mapOf(1L to 42L)
        val movieDtos = listOf(DummyDataFactory.DummyDataFactory.MOVIE_DTO)
        val expectedSavedMovies =
            listOf(DummyDataFactory.DummyDataFactory.SAVABLE_MOVIE_DTO.toSavableMovie())

        mockGetSavedMovies(savedMovies)
        mockGetPopularMovies(movieDtos)

        val result = movieRepositoryUnderTest.getPopularMovies()

        assertThat(result).isEqualTo(expectedSavedMovies)
        verifyGetSavedMovies()
        verifyGetPopularMovies()
    }

    @Test
    fun `getUserRatedMovies should return paged result when user is logged in`() = runTest {
        val page = 1
        val pageSize = 20
        val userDto = DummyDataFactory.DummyDataFactory.USER_DTO
        val pagedResultDto = PagedResultDto(
            data = listOf(DummyDataFactory.DummyDataFactory.MOVIE_DTO), nextKey = 2, prevKey = null
        )
        val expectedPagedResult = PagedResult(
            data = listOf(DummyDataFactory.DummyDataFactory.MOVIE_DTO.toMedia()),
            nextPage = 2,
            prevPage = null
        )

        mockGetLoggedInUser(userDto)
        mockGetUserRatedMovies(userDto.id, page, pagedResultDto)

        val result = movieRepositoryUnderTest.getUserRatedMovies(page, pageSize)

        assertThat(result).isEqualTo(expectedPagedResult)
        verifyGetLoggedInUser()
        verifyGetUserRatedMovies(userDto.id, page)
    }

    @Test
    fun `getUserRatedMovies should return empty paged result when user is not logged in`() =
        runTest {
            val page = 1
            val pageSize = 20
            val expectedPagedResult = PagedResult<RatedMedia>(
                data = emptyList(), nextPage = null, prevPage = null
            )

            mockGetLoggedInUser(null)

            val result = movieRepositoryUnderTest.getUserRatedMovies(page, pageSize)

            assertThat(result).isEqualTo(expectedPagedResult)
            verifyGetLoggedInUser()
            coVerify(exactly = 0) { mockRemoteMovieDataSource.getUserRatedMovies(any(), any()) }
        }

    private fun mockGetMovieGenres(genreDtos: List<GenreDto>) {
        coEvery { mockRemoteGenreDataSource.getMovieGenre(Locale.getDefault().language) } returns genreDtos
    }

    private fun mockGetMovieCastMembers(movieId: Long, castMemberDtos: List<CastMemberDto>) {
        coEvery { mockRemoteMovieDataSource.getMovieCastMembers(movieId) } returns castMemberDtos
    }

    private fun mockGetMovieImages(movieId: Long, images: List<String>) {
        coEvery { mockRemoteMovieDataSource.getMovieImages(movieId) } returns images
    }

    private fun mockGetMovieAccountStates(movieId: Long, isRated: Boolean) {
        coEvery { mockRemoteMovieDataSource.getMovieAccountStates(movieId) } returns mockk {
            coEvery { toIsMediaRated() } returns isRated
        }
    }

    private fun mockGetSavedMovies(savedMovies: Map<Long, Long>) {
        coEvery { mockSavableMovieDataSource.getSavedMovies() } returns savedMovies
    }

    private fun mockGetSimilarMovies(movieId: Long, movieDtos: List<MovieDto>) {
        coEvery { mockRemoteMovieDataSource.getSimilarMovies(movieId) } returns movieDtos
    }

    private fun mockDeleteMovieRate(movieId: Long) {
        coEvery { mockRemoteMovieDataSource.deleteMovieRate(movieId) } returns Unit
    }

    private fun mockAddMovieRate(movieId: Long, rating: Int) {
        coEvery { mockRemoteMovieDataSource.addMovieRate(movieId, rating) } returns Unit
    }

    private fun mockGetMovieReviews(movieId: Long, reviewDtos: List<ReviewDto>) {
        coEvery { mockRemoteMovieDataSource.getMovieReviews(movieId) } returns reviewDtos
    }

    private fun mockGetTopRatedMovies(page: Int, pagedResultDto: PagedResultDto<MovieDto>) {
        coEvery { mockRemoteMovieDataSource.getTopRatedMovies(page) } returns pagedResultDto
    }

    private fun mockGetMovieTrailer(movieId: Long, trailerUrl: String) {
        coEvery { mockRemoteMovieDataSource.getMovieTrailer(movieId) } returns trailerUrl
    }

    private fun mockGetMovieDetails(movieId: Long, movieDto: MovieDto) {
        coEvery { mockRemoteMovieDataSource.getMovieDetails(movieId) } returns movieDto
    }

    private fun mockGetPopularMovies(movieDtos: List<MovieDto>) {
        coEvery { mockRemoteMovieDataSource.getPopularMovies() } returns movieDtos
    }

    private fun mockGetTrendingMovies(page: Int, pagedResultDto: PagedResultDto<MovieDto>) {
        coEvery { mockRemoteMovieDataSource.getTrendingMovies(page) } returns pagedResultDto
    }

    private fun mockGetUpcomingMovies(genreId: Long?, movieDtos: List<MovieDto>) {
        coEvery { mockRemoteMovieDataSource.getUpcomingMovies(genreId) } returns movieDtos
    }

    private fun mockGetMoviesByGenre(
        genreId: Long, page: Int, pagedResultDto: PagedResultDto<MovieDto>
    ) {
        coEvery { mockRemoteMovieDataSource.getMoviesByGenre(genreId, page) } returns pagedResultDto
    }

    private fun mockGetLoggedInUser(userDto: UserDto?) {
        val user = userDto?.toEntity()
        coEvery { mockAuthenticationRepository.getUserInfo() } returns user
    }

    private fun mockGetUserRatedMovies(
        userId: Long, page: Int, pagedResultDto: PagedResultDto<MovieDto>
    ) {
        coEvery {
            mockRemoteMovieDataSource.getUserRatedMovies(
                userId, page
            )
        } returns pagedResultDto
    }

    private fun verifyGetMovieGenres() {
        coVerify { mockRemoteGenreDataSource.getMovieGenre(Locale.getDefault().language) }
    }

    private fun verifyGetMovieCastMembers(movieId: Long) {
        coVerify { mockRemoteMovieDataSource.getMovieCastMembers(movieId) }
    }

    private fun verifyGetMovieImages(movieId: Long) {
        coVerify { mockRemoteMovieDataSource.getMovieImages(movieId) }
    }

    private fun verifyGetMovieAccountStates(movieId: Long) {
        coVerify { mockRemoteMovieDataSource.getMovieAccountStates(movieId) }
    }

    private fun verifyGetSavedMovies() {
        coVerify { mockSavableMovieDataSource.getSavedMovies() }
    }

    private fun verifyGetSimilarMovies(movieId: Long) {
        coVerify { mockRemoteMovieDataSource.getSimilarMovies(movieId) }
    }

    private fun verifyDeleteMovieRate(movieId: Long) {
        coVerify { mockRemoteMovieDataSource.deleteMovieRate(movieId) }
    }

    private fun verifyAddMovieRate(movieId: Long, rating: Int) {
        coVerify { mockRemoteMovieDataSource.addMovieRate(movieId, rating) }
    }

    private fun verifyGetMovieReviews(movieId: Long) {
        coVerify { mockRemoteMovieDataSource.getMovieReviews(movieId) }
    }

    private fun verifyGetTopRatedMovies(page: Int) {
        coVerify { mockRemoteMovieDataSource.getTopRatedMovies(page) }
    }

    private fun verifyGetMovieTrailer(movieId: Long) {
        coVerify { mockRemoteMovieDataSource.getMovieTrailer(movieId) }
    }

    private fun verifyGetMovieDetails(movieId: Long) {
        coVerify { mockRemoteMovieDataSource.getMovieDetails(movieId) }
    }

    private fun verifyGetPopularMovies() {
        coVerify { mockRemoteMovieDataSource.getPopularMovies() }
    }

    private fun verifyGetTrendingMovies(page: Int) {
        coVerify { mockRemoteMovieDataSource.getTrendingMovies(page) }
    }

    private fun verifyGetUpcomingMovies(genreId: Long?) {
        coVerify { mockRemoteMovieDataSource.getUpcomingMovies(genreId) }
    }

    private fun verifyGetMoviesByGenre(genreId: Long, page: Int) {
        coVerify { mockRemoteMovieDataSource.getMoviesByGenre(genreId, page) }
    }

    private fun verifyGetLoggedInUser() {
        coVerify { mockAuthenticationRepository.getUserInfo() }
    }

    private fun verifyGetUserRatedMovies(userId: Long, page: Int) {
        coVerify { mockRemoteMovieDataSource.getUserRatedMovies(userId, page) }
    }
}