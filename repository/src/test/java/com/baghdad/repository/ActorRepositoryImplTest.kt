package com.baghdad.repository

import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.PagedResultDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ActorRepositoryImplTest {

    private lateinit var remoteActorDataSource: RemoteActorDataSource
    private lateinit var localActorDataSource: LocalActorDataSource
    private lateinit var actorRepositoryImpl: ActorRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteActorDataSource = mockk()
        localActorDataSource = mockk()
        actorRepositoryImpl = ActorRepositoryImpl(
            remoteActorDataSource = remoteActorDataSource,
        )
    }


    @Test
    fun `getActorInfo should return actor when remote call succeeds`() = runTest {
        // Given
        val actorId = 123L
        val expectedActorDto = createMockActorDto()
        val mockImages = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg", "/header4.jpg")
        val expectedActor = createMockActor()

        coEvery { remoteActorDataSource.getActorDetails(actorId) } returns expectedActorDto
        coEvery { remoteActorDataSource.getActorImages(actorId) } returns mockImages

        val result = actorRepositoryImpl.getActorInfo(actorId)

        assertEquals(expectedActor, result)
        coVerify { remoteActorDataSource.getActorDetails(actorId) }
        coVerify { remoteActorDataSource.getActorImages(actorId) }
    }

    @Test
    fun `getActorInfo should limit header pictures to 3 images`() = runTest {

        val actorId = 123L
        val expectedActorDto = createMockActorDto()
        val mockImages = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg", "/header4.jpg", "/header5.jpg")

        coEvery { remoteActorDataSource.getActorDetails(actorId) } returns expectedActorDto
        coEvery { remoteActorDataSource.getActorImages(actorId) } returns mockImages

        val result = actorRepositoryImpl.getActorInfo(actorId)

        assertEquals(3, result.headerPictures.size)
        assertEquals(listOf("/header1.jpg", "/header2.jpg", "/header3.jpg"), result.headerPictures)
        coVerify { remoteActorDataSource.getActorDetails(actorId) }
        coVerify { remoteActorDataSource.getActorImages(actorId) }
    }


    @Test
    fun `getActorMovies should return empty list when no movies found`() = runTest {
        val actorId = 123L
        coEvery { remoteActorDataSource.getActorMovies(actorId) } returns emptyList()

        val result = actorRepositoryImpl.getActorMovies(actorId)

        assertEquals(emptyList<Movie>(), result)
        coVerify { remoteActorDataSource.getActorMovies(actorId) }
    }

    @Test
    fun `getActorTvShows should return empty list when no tv shows found`() = runTest {

        val actorId = 123L
        coEvery { remoteActorDataSource.getActorTvShows(actorId) } returns emptyList()

        val result = actorRepositoryImpl.getActorTvShows(actorId)

        assertEquals(emptyList<TvShow>(), result)
        coVerify { remoteActorDataSource.getActorTvShows(actorId) }
    }

    @Test
    fun `getActorGallery should return list of image urls when remote call succeeds`() = runTest {

        val actorId = 123L
        val mockImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg")

        coEvery { remoteActorDataSource.getActorImages(actorId) } returns mockImages

        val result = actorRepositoryImpl.getActorGallery(actorId)

        assertEquals(mockImages, result)
        coVerify { remoteActorDataSource.getActorImages(actorId) }
    }

    @Test
    fun `getActorGallery should return empty list when no images found`() = runTest {

        val actorId = 123L
        coEvery { remoteActorDataSource.getActorImages(actorId) } returns emptyList()

        val result = actorRepositoryImpl.getActorGallery(actorId)

        assertEquals(emptyList<String>(), result)
        coVerify { remoteActorDataSource.getActorImages(actorId) }
    }

    @Test
    fun `getActorMovies should throw exception when remote call fails`() = runTest {

        val actorId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteActorDataSource.getActorMovies(actorId) } throws exception

        assertThrows<Exception> {
            actorRepositoryImpl.getActorMovies(actorId)
        }
    }

    @Test
    fun `getActorTvShows should throw exception when remote call fails`() = runTest {

        val actorId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteActorDataSource.getActorTvShows(actorId) } throws exception

        assertThrows<Exception> {
            actorRepositoryImpl.getActorTvShows(actorId)
        }
    }

    @Test
    fun `getActorGallery should throw exception when remote call fails`() = runTest {

        val actorId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteActorDataSource.getActorImages(actorId) } throws exception

        assertThrows<Exception> {
            actorRepositoryImpl.getActorGallery(actorId)
        }
    }

    @Test
    fun `getTrendingActors should return paged result when remote call succeeds`() = runTest {
        // Given
        val page = 1
        val mockActorDtos = listOf(createMockActorDto(), createMockActorDto().copy(id = 124L, name = "Jane Doe"))
        val mockPagedResult = PagedResultDto(mockActorDtos, nextKey = 2, prevKey = null)
        val expectedActors = listOf(createMockActor(), createMockActor().copy(id = 124L, name = "Jane Doe"))

        coEvery { remoteActorDataSource.getTrendingActors(page) } returns mockPagedResult

        val result = actorRepositoryImpl.getTrendingActors(page)

        assertEquals(expectedActors.size, result.data.size)
        assertEquals(expectedActors[0].id, result.data[0].id)
        assertEquals(expectedActors[0].name, result.data[0].name)
        assertEquals(expectedActors[1].id, result.data[1].id)
        assertEquals(expectedActors[1].name, result.data[1].name)
        assertEquals(2, result.nextKey)
        assertEquals(null, result.prevKey)
        coVerify { remoteActorDataSource.getTrendingActors(page) }
    }

    @Test
    fun `getTrendingActors should return empty paged result when no trending actors found`() = runTest {
        // Given
        val page = 1
        val emptyPagedResult = PagedResultDto<ActorDto>(emptyList(), nextKey = null, prevKey = null)

        coEvery { remoteActorDataSource.getTrendingActors(page) } returns emptyPagedResult

        val result = actorRepositoryImpl.getTrendingActors(page)

        assertEquals(0, result.data.size)
        assertEquals(null, result.nextKey)
        assertEquals(null, result.prevKey)
        coVerify { remoteActorDataSource.getTrendingActors(page) }
    }

    @Test
    fun `getTrendingActors should throw exception when remote call fails`() = runTest {
        // Given
        val page = 1
        val exception = RuntimeException("Network error")
        coEvery { remoteActorDataSource.getTrendingActors(page) } throws exception

        assertThrows<Exception> {
            actorRepositoryImpl.getTrendingActors(page)
        }
    }

    companion object {
        private fun createMockActorDto() = ActorDto(
            id = 123L,
            name = "John Doe",
            imageUrl = "/profile.jpg",
            biography = "Famous actor biography",
            birthdayDate = "1980-01-01",
            deathDate = null,
            placeOfBirth = "New York, USA",
            headerPictures = listOf("/header1.jpg", "/header2.jpg"),
            department = "Acting"
        )

        private fun createMockActor() = Actor(
            id = 123L,
            name = "John Doe",
            profilePictureURL = "/profile.jpg",
            birthDate = LocalDate.parse("1980-01-01"),
            placeOfBirth = "New York, USA",
            deathDate = null,
            biography = "Famous actor biography",
            headerPictures = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg"),
            department = "Acting"
        )
    }
}