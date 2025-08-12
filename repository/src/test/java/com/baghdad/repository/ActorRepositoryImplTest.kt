package com.baghdad.repository

import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.dummyData.DummyDataFactory.createMockActor
import com.baghdad.repository.dummyData.DummyDataFactory.createMockActorDto
import com.baghdad.repository.dummyData.DummyDataFactory.createMockMovieDto
import com.baghdad.repository.dummyData.DummyDataFactory.createMockTvShowDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ActorRepositoryImplTest {

    private lateinit var remoteActorDataSource: RemoteActorDataSource
    private lateinit var savableMovieDataSource: LocalSavableMovieDataSource
    private lateinit var actorRepositoryImpl: ActorRepositoryImpl
    private val actorId = 123L

    @BeforeEach
    fun setUp() {
        remoteActorDataSource = mockk()
        savableMovieDataSource = mockk()
        actorRepositoryImpl = ActorRepositoryImpl(
            remoteActorDataSource = remoteActorDataSource,
            savableMovieDataSource = savableMovieDataSource,
        )
    }

    @Test
    fun `getActorInfo should return complete actor details when remote calls succeed`() = runTest {
        val expectedActorDto = createMockActorDto()
        val mockImages = listOf("/actor_header1.jpg", "/actor_header2.jpg")
        val expectedActor = createMockActor()

        mockGetActorDetails(expectedActorDto)
        mockGetActorImages(mockImages)

        val result = actorRepositoryImpl.getActorInfo(actorId)

        assertThat(result).isEqualTo(expectedActor)
        verifyGetActorDetailsCalled()
        verifyGetActorImagesCalled()
    }

    @Test
    fun `getActorMovies should return empty list when actor has no movies`() = runTest {
        mockGetActorMovies(emptyList())

        val result = actorRepositoryImpl.getActorMovies(actorId)

        assertThat(result).isEmpty()
        verifyGetActorMoviesCalled()
    }

    @Test
    fun `getActorMovies should return mapped movie entities when remote data is available`() =
        runTest {
            val mockMovieDtos = createMockMovieDto()
            val expectedMovies = mockMovieDtos.map { it.toEntity() }

            mockGetActorMovies(mockMovieDtos)

            val result = actorRepositoryImpl.getActorMovies(actorId)

            assertThat(result).isEqualTo(expectedMovies)
            verifyGetActorMoviesCalled()
        }

    @Test
    fun `getActorTvShows should return empty list when actor has no tv shows`() = runTest {
        mockGetActorTvShows(emptyList())

        val result = actorRepositoryImpl.getActorTvShows(actorId)

        assertThat(result).isEmpty()
        verifyGetActorTvShowsCalled()
    }

    @Test
    fun `getActorTvShows should return list of tv shows when remote call succeeds`() = runTest {
        val mockTvShowDtos = createMockTvShowDto()
        val expectedTvShows = mockTvShowDtos.map { it.toEntity() }

        mockGetActorTvShows(mockTvShowDtos)

        val result = actorRepositoryImpl.getActorTvShows(actorId)

        assertThat(result).isEqualTo(expectedTvShows)
        verifyGetActorTvShowsCalled()
    }

    @Test
    fun `getTrendingActors should return mapped actors and pagination info`() = runTest {
        val page = 1
        val mockActorDtos = listOf(
            createMockActorDto(),
            createMockActorDto().copy(id = 124L, name = "Max ")
        )
        val mockPagedResult = PagedResultDto(mockActorDtos, nextKey = 2, prevKey = null)
        val expectedActors = listOf(
            createMockActor(),
            createMockActor().copy(id = 124L, name = "Max")
        )
        val expectedPagedResult = PagedResultDto(
            data = expectedActors,
            nextKey = 2,
            prevKey = null
        )

        mockGetTrendingActors(page, mockPagedResult)

        val result = actorRepositoryImpl.getTrendingActors(page)

        assertThat(result).isEqualTo(expectedPagedResult)
        verifyGetTrendingActorsCalled(page)
    }

    private fun mockGetActorDetails(actorDto: ActorDto) {
        coEvery { remoteActorDataSource.getActorDetails(actorId) } returns actorDto
    }

    private fun mockGetActorImages(images: List<String>) {
        coEvery { remoteActorDataSource.getActorImages(actorId) } returns images
    }

    private fun mockGetActorMovies(movieDtos: List<MovieDto>) {
        coEvery { remoteActorDataSource.getActorMovies(actorId) } returns movieDtos
    }

    private fun mockGetActorTvShows(tvShowDtos: List<TvShowDto>) {
        coEvery { remoteActorDataSource.getActorTvShows(actorId) } returns tvShowDtos
    }

    private fun mockGetTrendingActors(page: Int, pagedResultDto: PagedResultDto<ActorDto>) {
        coEvery { remoteActorDataSource.getTrendingActors(page) } returns pagedResultDto
    }

    private fun verifyGetActorDetailsCalled() {
        coVerify { remoteActorDataSource.getActorDetails(actorId) }
    }

    private fun verifyGetActorImagesCalled() {
        coVerify { remoteActorDataSource.getActorImages(actorId) }
    }

    private fun verifyGetActorMoviesCalled() {
        coVerify { remoteActorDataSource.getActorMovies(actorId) }
    }

    private fun verifyGetActorTvShowsCalled() {
        coVerify { remoteActorDataSource.getActorTvShows(actorId) }
    }

    private fun verifyGetTrendingActorsCalled(page: Int) {
        coVerify { remoteActorDataSource.getTrendingActors(page) }
    }
}