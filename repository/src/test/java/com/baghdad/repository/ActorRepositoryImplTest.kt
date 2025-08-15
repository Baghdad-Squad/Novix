package com.baghdad.repository

import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockActorDto
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockMovieDto
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockTvShowDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toSavableMovie
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ActorRepositoryImplTest {

    private val remoteActorDataSource: RemoteActorDataSource = mockk()
    private val savableMovieDataSource: SavableMovieDataSource = mockk()
    private val actorRepositoryImpl: ActorRepositoryImpl = ActorRepositoryImpl(
        remoteActorDataSource = remoteActorDataSource,
        savableMovieDataSource = savableMovieDataSource,
    )
    private val actorId = 123L

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
    fun `getActorDetails should return actor with header pictures`() = runTest {
        val mockActorDto = createMockActorDto()
        val mockImages = listOf("img1.jpg", "img2.jpg")

        mockGetActorDetails(mockActorDto)
        mockGetActorImages(mockImages)

        val expected = mockActorDto.toEntity().copy(headerPictures = mockImages)

        val result = actorRepositoryImpl.getActorDetails(actorId)

        assertThat(result).isEqualTo(expected)
        verifyGetActorDetailsCalled()
        verifyGetActorImagesCalled()
    }

    @Test
    fun `getActorMovies should map movies with saved state`() = runTest {
        val movieDtos = createMockMovieDto()
        val savedMoviesMap = mapOf(movieDtos.first().id to 1L)

        coEvery { savableMovieDataSource.getSavedMovies() } returns savedMoviesMap
        mockGetActorMovies(movieDtos)

        val expected = movieDtos.map {
            it.toSavableMovie(
                isSaved = savedMoviesMap.containsKey(it.id),
                listId = savedMoviesMap[it.id],
            )
        }

        val result = actorRepositoryImpl.getActorMovies(actorId)

        assertThat(result).isEqualTo(expected)
        verifyGetActorMoviesCalled()
    }

    @Test
    fun `getActorGallery should return images from remote`() = runTest {
        val mockImages = listOf("img1", "img2")
        mockGetActorImages(mockImages)

        val result = actorRepositoryImpl.getActorGallery(actorId)

        assertThat(result).isEqualTo(mockImages)
        verifyGetActorImagesCalled()
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