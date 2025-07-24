package com.baghdad.repository

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
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
            localPopularPeopleDataSource = localActorDataSource
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
    fun `getActorInfo should throw exception when remote call fails`() = runTest {

        val actorId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteActorDataSource.getActorDetails(actorId) } throws exception

        assertThrows<Exception> {
            actorRepositoryImpl.getActorInfo(actorId)
        }
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
            headerPictures = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg"), // First 3 images
            department = "Acting"
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

        private fun createMockMovie() = Movie(
            id = 456L,
            title = "Test Movie",
            genres = listOf(Genre(123L, " Action")),
            averageRating = 8.0,
            userRating = 7.5,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Test movie overview",
            posterImageURL = "/movie_poster.jpg",
            runtimeMinutes = 120,
            trailerURL = " "
        )

        private fun createMockTvShowDto() = TvShowDto(
            id = 789L,
            title = "Test TV Show",
            genres = listOf(
                GenreDto(18, "Drama", type = GenreDto.GenreType.TV_SHOW),
                GenreDto(35, "Comedy", type = GenreDto.GenreType.TV_SHOW)
            ),
            imdbRating = 7.9,
            userRating = 8.1,
            releaseDate = "2023-01-01",
            overview = "Test overview for TV Show",
            posterPictureURL = "/tv_poster.jpg",
            numberOfSeasons = 3,
            trailerURL = " ",
            headerImagesURLs = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg", "/header4.jpg")
        )

        private fun createMockTvShow() = TvShow(
            id = 789L,
            title = "Test TV Show",
            genres = listOf(Genre(654L, "Comedy")),
            averageRating = 7.9,
            userRating = 8.1,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Test overview for TV Show",
            posterImageURL = "/tv_poster.jpg",
            numberOfSeasons = 3,
            trailerURL = " ",
            headerImagesURLs = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg")
        )
    }
}