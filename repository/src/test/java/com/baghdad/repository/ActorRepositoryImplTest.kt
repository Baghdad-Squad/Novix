//package com.baghdad.repository
//
//import com.baghdad.entity.media.Movie
//import com.baghdad.entity.media.TvShow
//import com.baghdad.repository.datasource.remote.RemoteActorDataSource
//import com.baghdad.repository.dummyData.DummyDataFactory.createMockActor
//import com.baghdad.repository.dummyData.DummyDataFactory.createMockActorDto
//import com.baghdad.repository.dummyData.DummyDataFactory.createMockMovieDto
//import com.baghdad.repository.dummyData.DummyDataFactory.createMockTvShowDto
//import com.baghdad.repository.mapper.toEntity
//import com.baghdad.repository.model.PagedResultDto
//import com.google.common.truth.Truth.assertThat
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.mockk
//import kotlinx.coroutines.test.runTest
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class ActorRepositoryImplTest {
//
//    private lateinit var remoteActorDataSource: RemoteActorDataSource
//    private lateinit var localActorDataSource: LocalActorDataSource
//    private lateinit var actorRepositoryImpl: ActorRepositoryImpl
//    val actorId = 123L
//
//    @BeforeEach
//    fun setUp() {
//        remoteActorDataSource = mockk()
//        localActorDataSource = mockk()
//        actorRepositoryImpl = ActorRepositoryImpl(
//            remoteActorDataSource = remoteActorDataSource,
//        )
//    }
//
//
//    @Test
//    fun `getActorInfo should return actor when remote call succeeds`() = runTest {
//        // Given
//        val actorId = 123L
//        val expectedActorDto = createMockActorDto()
//        val mockImages = listOf("/actor_header1.jpg", "/actor_header2.jpg")
//        val expectedActor = createMockActor()
//
//        coEvery { remoteActorDataSource.getActorDetails(actorId) } returns expectedActorDto
//        coEvery { remoteActorDataSource.getActorImages(actorId) } returns mockImages
//
//        // When
//        val result = actorRepositoryImpl.getActorInfo(actorId)
//
//        // Then
//        assertThat(expectedActor == result).isTrue()
//        coVerify { remoteActorDataSource.getActorDetails(actorId) }
//        coVerify { remoteActorDataSource.getActorImages(actorId) }
//    }
//
//    @Test
//    fun `getActorMovies should return empty list when no movies found`() = runTest {
//        // Given
//        coEvery { remoteActorDataSource.getActorMovies(actorId) } returns emptyList()
//
//        // When
//        val result = actorRepositoryImpl.getActorMovies(actorId)
//
//        // Then
//        assertThat(emptyList<Movie>() == result).isTrue()
//        coVerify { remoteActorDataSource.getActorMovies(actorId) }
//    }
//
//    @Test
//    fun `getActorMovies should return list of movies when remote call succeeds`() = runTest {
//        // Given
//        val mockMovieDtos = createMockMovieDto()
//        val expectedMovies = mockMovieDtos.map { it.toEntity() }
//
//        coEvery { remoteActorDataSource.getActorMovies(actorId) } returns mockMovieDtos
//
//        // When
//        val result = actorRepositoryImpl.getActorMovies(actorId)
//
//        // Then
//        assertThat(result).isEqualTo(expectedMovies)
//        coVerify { remoteActorDataSource.getActorMovies(actorId) }
//    }
//
//    @Test
//    fun `getActorTvShows should return empty list when no tv shows found`() = runTest {
//        // Given
//        coEvery { remoteActorDataSource.getActorTvShows(actorId) } returns emptyList()
//
//        // When
//        val result = actorRepositoryImpl.getActorTvShows(actorId)
//
//        // Then
//        assertThat(emptyList<TvShow>() == result).isTrue()
//        coVerify { remoteActorDataSource.getActorTvShows(actorId) }
//    }
//
//    @Test
//    fun `getActorTvShows should return list of tv shows when remote call succeeds`() = runTest {
//        // Given
//        val mockTvShowDtos = createMockTvShowDto()
//        val expectedTvShows = mockTvShowDtos.map { it.toEntity() }
//
//        coEvery { remoteActorDataSource.getActorTvShows(actorId) } returns mockTvShowDtos
//
//        // When
//        val result = actorRepositoryImpl.getActorTvShows(actorId)
//
//        // Then
//        assertThat(result).isEqualTo(expectedTvShows)
//        coVerify { remoteActorDataSource.getActorTvShows(actorId) }
//    }
//
//    @Test
//    fun `getActorGallery should return list of image urls when remote call succeeds`() = runTest {
//        // Given
//        val mockImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg")
//
//        coEvery { remoteActorDataSource.getActorImages(actorId) } returns mockImages
//        // When
//        val result = actorRepositoryImpl.getActorGallery(actorId)
//        // Then
//        assertThat(mockImages == result).isTrue()
//        coVerify { remoteActorDataSource.getActorImages(actorId) }
//    }
//
//    @Test
//    fun `getTrendingActors should return paged result when remote call succeeds`() = runTest {
//        // Given
//        val page = 1
//        val mockActorDtos =
//            listOf(createMockActorDto(), createMockActorDto().copy(id = 124L, name = "Jane Doe"))
//        val mockPagedResult = PagedResultDto(mockActorDtos, nextKey = 2, prevKey = null)
//        val expectedActors =
//            listOf(createMockActor(), createMockActor().copy(id = 124L, name = "Jane Doe"))
//        coEvery { remoteActorDataSource.getTrendingActors(page) } returns mockPagedResult
//
//        // When
//        val result = actorRepositoryImpl.getTrendingActors(page)
//
//        // Then
//        assertThat(expectedActors.size == result.data.size).isTrue()
//        assertThat(expectedActors[0].id == result.data[0].id).isTrue()
//        assertThat(expectedActors[0].name == result.data[0].name).isTrue()
//        assertThat(expectedActors[1].id == result.data[1].id).isTrue()
//        assertThat(expectedActors[1].name == result.data[1].name).isTrue()
//        assertThat(2 == result.nextKey).isTrue()
//        assertThat(null == result.prevKey).isTrue()
//        coVerify { remoteActorDataSource.getTrendingActors(page) }
//    }
//
//}