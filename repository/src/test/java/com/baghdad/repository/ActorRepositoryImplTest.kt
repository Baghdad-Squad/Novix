package com.baghdad.repository

import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.dummyData.DummyDataFactory.createMockActor
import com.baghdad.repository.dummyData.DummyDataFactory.createMockActorDto
import com.baghdad.repository.dummyData.DummyDataFactory.createMockMovieDto
import com.baghdad.repository.dummyData.DummyDataFactory.createMockTvShowDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.PagedResultDto
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
        // Given
        val expectedActorDto = createMockActorDto()
        val mockImages = listOf("/actor_header1.jpg", "/actor_header2.jpg")
        val expectedActor = createMockActor()

        coEvery { remoteActorDataSource.getActorDetails(actorId) } returns expectedActorDto
        coEvery { remoteActorDataSource.getActorImages(actorId) } returns mockImages

        // When
        val result = actorRepositoryImpl.getActorInfo(actorId)

        // Then
        assertThat(result).isEqualTo(expectedActor)
        coVerify { remoteActorDataSource.getActorDetails(actorId) }
        coVerify { remoteActorDataSource.getActorImages(actorId) }
    }

    @Test
    fun `getActorMovies should return empty list when actor has no movies`() = runTest {
        // Given
        coEvery { remoteActorDataSource.getActorMovies(actorId) } returns emptyList()

        // When
        val result = actorRepositoryImpl.getActorMovies(actorId)

        // Then
        assertThat(result).isEmpty()
        coVerify { remoteActorDataSource.getActorMovies(actorId) }
    }

    @Test
    fun `getActorMovies should return mapped movie entities when remote data is available`() =
        runTest {
            // Given
            val mockMovieDtos = createMockMovieDto()
            val expectedMovies = mockMovieDtos.map { it.toEntity() }

            coEvery { remoteActorDataSource.getActorMovies(actorId) } returns mockMovieDtos

            // When
            val result = actorRepositoryImpl.getActorMovies(actorId)

            // Then
            assertThat(result).isEqualTo(expectedMovies)
            coVerify { remoteActorDataSource.getActorMovies(actorId) }
        }

    @Test
    fun `getActorTvShows should return empty list when actor has no tv shows`() = runTest {
        // Given
        coEvery { remoteActorDataSource.getActorTvShows(actorId) } returns emptyList()

        // When
        val result = actorRepositoryImpl.getActorTvShows(actorId)

        // Then
        assertThat(result).isEmpty()
        coVerify { remoteActorDataSource.getActorTvShows(actorId) }
    }

    @Test
    fun `getActorTvShows should return list of tv shows when remote call succeeds`() = runTest {
        // Given
        val mockTvShowDtos = createMockTvShowDto()
        val expectedTvShows = mockTvShowDtos.map { it.toEntity() }

        coEvery { remoteActorDataSource.getActorTvShows(actorId) } returns mockTvShowDtos

        // When
        val result = actorRepositoryImpl.getActorTvShows(actorId)

        // Then
        assertThat(result).isEqualTo(expectedTvShows)
        coVerify { remoteActorDataSource.getActorTvShows(actorId) }
    }

    @Test
    fun `getActorTvShows should return mapped tv show entities when remote data is available`() =
        runTest {
            // Given
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
            // When
            coEvery { remoteActorDataSource.getTrendingActors(page) } returns mockPagedResult

            val result = actorRepositoryImpl.getTrendingActors(page)

            val expectedPagedResult = PagedResultDto(
                data = expectedActors,
                nextKey = 2,
                prevKey = null
            )
            // Then
            assertThat(result).isEqualTo(expectedPagedResult)
            coVerify { remoteActorDataSource.getTrendingActors(page) }
        }
}