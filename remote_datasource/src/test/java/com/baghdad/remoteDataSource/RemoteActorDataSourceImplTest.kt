package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.ActorApiService
import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.baghdad.remoteDataSource.response.actor.ActorMoviesResponse
import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import com.baghdad.remoteDataSource.response.actor.TrendingActorResponse
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteActorDataSourceImplTest {

    private val actorApiService = mockk<ActorApiService>()
    private val logger = mockk<Logger>(relaxed = true)
    private val dataSource = RemoteActorDataSourceImpl(actorApiService, logger)

    @Test
    fun `should return actor dto when getActorDetails is called with valid person id`() = runTest {
        val successResponse = Response.success(actorDetailsResponse)
        coEvery { actorApiService.getActorDetails(PERSON_ID) } returns successResponse

        val result = dataSource.getActorDetails(PERSON_ID)

        assertThat(result).isEqualTo(expectedActorDto)
        coVerify(exactly = 1) { actorApiService.getActorDetails(PERSON_ID) }
    }

    @Test
    fun `should return empty list when getActorImages receives null profiles`() = runTest {
        val successResponse = Response.success(actorImagesResponseWithNullProfiles)
        coEvery { actorApiService.getActorImages(PERSON_ID) } returns successResponse

        val result = dataSource.getActorImages(PERSON_ID)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when getActorMovies receives null cast`() = runTest {
        val successResponse = Response.success(actorMoviesResponseWithNullCast)
        coEvery { actorApiService.getActorMovies(PERSON_ID) } returns successResponse

        val result = dataSource.getActorMovies(PERSON_ID)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should filter out movies with null id when getActorMovies is called`() = runTest {
        val successResponse = Response.success(actorMoviesResponseWithNullId)
        coEvery { actorApiService.getActorMovies(PERSON_ID) } returns successResponse

        val result = dataSource.getActorMovies(PERSON_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].title).isEqualTo("Valid Movie")
    }

    @Test
    fun `should return empty list when getActorTvShows receives null cast`() = runTest {
        val successResponse = Response.success(actorTvShowsResponseWithNullCast)
        coEvery { actorApiService.getActorTvShows(PERSON_ID) } returns successResponse

        val result = dataSource.getActorTvShows(PERSON_ID)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should filter out tv shows with null id when getActorTvShows is called`() = runTest {
        val successResponse = Response.success(actorTvShowsResponseWithNullId)
        coEvery { actorApiService.getActorTvShows(PERSON_ID) } returns successResponse

        val result = dataSource.getActorTvShows(PERSON_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].title).isEqualTo("Valid TV Show")
    }

    @Test
    fun `should return paged actor dto when getTrendingActors is called with valid page`() = runTest {
        val successResponse = Response.success(trendingActorResponse)
        coEvery { actorApiService.getTrendingActors(PAGE) } returns successResponse

        val result = dataSource.getTrendingActors(PAGE)

        assertThat(result.data).hasSize(2)
        assertThat(result.data[0]).isEqualTo(expectedTrendingActorDto1)
        assertThat(result.data[1]).isEqualTo(expectedTrendingActorDto2)
        coVerify(exactly = 1) { actorApiService.getTrendingActors(PAGE) }
    }

    @Test
    fun `should return empty paged result when getTrendingActors receives null results`() = runTest {
        val successResponse = Response.success(trendingActorResponseWithNullResults)
        coEvery { actorApiService.getTrendingActors(PAGE) } returns successResponse

        val result = dataSource.getTrendingActors(PAGE)

        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should filter out actors with null id when getTrendingActors is called`() = runTest {
        val successResponse = Response.success(trendingActorResponseWithNullId)
        coEvery { actorApiService.getTrendingActors(PAGE) } returns successResponse

        val result = dataSource.getTrendingActors(PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(1L)
        assertThat(result.data[0].name).isEqualTo("Valid Actor")
    }

    @Test
    fun `should handle actors with null name in getTrendingActors`() = runTest {
        val successResponse = Response.success(trendingActorResponseWithNullName)
        coEvery { actorApiService.getTrendingActors(PAGE) } returns successResponse

        val result = dataSource.getTrendingActors(PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].name).isEqualTo("")
    }

    companion object {
        const val PERSON_ID = 123L
        const val PAGE = 1

        val actorDetailsResponse = ActorDetailsResponse(
            id = PERSON_ID,
            name = "John Doe",
            profilePath = "/path/to/profile.jpg",
            biography = "Famous actor",
            birthday = "1980-01-01",
            deathday = null,
            placeOfBirth = "New York",
            knownForDepartment = "Acting"
        )

        val actorDetailsResponseWithNulls = ActorDetailsResponse(
            id = null,
            name = null,
            profilePath = null,
            biography = null,
            birthday = null,
            deathday = null,
            placeOfBirth = null,
            knownForDepartment = null
        )

        val imageResponses = listOf(
            ActorImagesResponse.ImageResponse(filePath = "/image1.jpg"),
            ActorImagesResponse.ImageResponse(filePath = "/image2.jpg"),
            ActorImagesResponse.ImageResponse(filePath = null)
        )

        val actorImagesResponse = ActorImagesResponse(
            id = PERSON_ID,
            profiles = imageResponses
        )

        val actorImagesResponseWithNullProfiles = ActorImagesResponse(
            id = PERSON_ID,
            profiles = null
        )

        val movieCast = listOf(
            ActorMoviesResponse.ActorMovieDto(
                id = 1L,
                title = "Movie 1",
                voteAverage = 8.5,
                releaseDate = "2023-01-01",
                overview = "Great movie",
                posterPath = "/poster1.jpg"
            ),
            ActorMoviesResponse.ActorMovieDto(
                id = 2L,
                title = "Movie 2",
                voteAverage = null,
                releaseDate = "",
                overview = null,
                posterPath = null
            )
        )

        val movieCastWithNullId = listOf(
            ActorMoviesResponse.ActorMovieDto(
                id = 1L,
                title = "Valid Movie",
                voteAverage = 8.5,
                releaseDate = "2023-01-01",
                overview = "Great movie",
                posterPath = "/poster1.jpg"
            ),
            ActorMoviesResponse.ActorMovieDto(
                id = null,
                title = "Invalid Movie",
                voteAverage = 7.0,
                releaseDate = "2023-02-01",
                overview = "Should be filtered",
                posterPath = "/poster2.jpg"
            )
        )

        val actorMoviesResponse = ActorMoviesResponse(cast = movieCast)
        val actorMoviesResponseWithNullId = ActorMoviesResponse(cast = movieCastWithNullId)
        val actorMoviesResponseWithNullCast = ActorMoviesResponse(cast = null)

        val tvShowCast = listOf(
            ActorTvShowsResponse.ActorTvShowDto(
                id = 1L,
                name = "TV Show 1",
                originalName = "Original TV Show 1",
                voteAverage = 9.0,
                firstAirDate = "2023-01-01",
                overview = "Great TV show",
                posterPath = "/tvposter1.jpg"
            ),
            ActorTvShowsResponse.ActorTvShowDto(
                id = 2L,
                name = null,
                originalName = "Original TV Show 2",
                voteAverage = null,
                firstAirDate = "",
                overview = null,
                posterPath = null
            )
        )

        val tvShowCastWithNullId = listOf(
            ActorTvShowsResponse.ActorTvShowDto(
                id = 1L,
                name = "Valid TV Show",
                originalName = "Original Valid TV Show",
                voteAverage = 8.5,
                firstAirDate = "2023-01-01",
                overview = "Great TV show",
                posterPath = "/tvposter1.jpg"
            ),
            ActorTvShowsResponse.ActorTvShowDto(
                id = null,
                name = "Invalid TV Show",
                originalName = "Original Invalid TV Show",
                voteAverage = 7.0,
                firstAirDate = "2023-02-01",
                overview = "Should be filtered",
                posterPath = "/tvposter2.jpg"
            )
        )

        val actorTvShowsResponse = ActorTvShowsResponse(cast = tvShowCast)
        val actorTvShowsResponseWithNullId = ActorTvShowsResponse(cast = tvShowCastWithNullId)
        val actorTvShowsResponseWithNullCast = ActorTvShowsResponse(cast = null)

        val trendingActors = listOf(
            TrendingActorResponse.TrendingActorDetails(
                id = 1L,
                name = "Actor 1",
                profilePath = "/actor1.jpg"
            ),
            TrendingActorResponse.TrendingActorDetails(
                id = 2L,
                name = "Actor 2",
                profilePath = "/actor2.jpg"
            )
        )

        val trendingActorsWithNullId = listOf(
            TrendingActorResponse.TrendingActorDetails(
                id = 1L,
                name = "Valid Actor",
                profilePath = "/actor1.jpg"
            ),
            TrendingActorResponse.TrendingActorDetails(
                id = null,
                name = "Invalid Actor",
                profilePath = "/actor2.jpg"
            )
        )

        val trendingActorsWithNullName = listOf(
            TrendingActorResponse.TrendingActorDetails(
                id = 1L,
                name = null,
                profilePath = "/actor1.jpg"
            )
        )

        val trendingActorsWithNullProfilePath = listOf(
            TrendingActorResponse.TrendingActorDetails(
                id = 1L,
                name = "Actor Name",
                profilePath = null
            )
        )

        val trendingActorResponse = TrendingActorResponse(
            page = PAGE,
            results = trendingActors,
            totalPages = 5
        )

        val trendingActorResponseWithNullResults = TrendingActorResponse(
            page = PAGE,
            results = null,
            totalPages = 5
        )

        val trendingActorResponseWithNullId = TrendingActorResponse(
            page = PAGE,
            results = trendingActorsWithNullId,
            totalPages = 5
        )

        val trendingActorResponseWithNullName = TrendingActorResponse(
            page = PAGE,
            results = trendingActorsWithNullName,
            totalPages = 5
        )

        val trendingActorResponseWithNullProfilePath = TrendingActorResponse(
            page = PAGE,
            results = trendingActorsWithNullProfilePath,
            totalPages = 5
        )

        val expectedActorDto = ActorDto(
            id = PERSON_ID,
            name = "John Doe",
            imageUrl = "https://image.tmdb.org/t/p/w500/path/to/profile.jpg",
            biography = "Famous actor",
            birthdayDate = "1980-01-01",
            deathDate = null,
            placeOfBirth = "New York",
            headerPictures = listOf("https://image.tmdb.org/t/p/w500/path/to/profile.jpg"),
            department = "Acting"
        )

        val expectedActorDtoWithDefaults = ActorDto(
            id = 0L,
            name = "",
            imageUrl = "https://image.tmdb.org/t/p/w500null",
            biography = "",
            birthdayDate = null,
            deathDate = null,
            placeOfBirth = "",
            headerPictures = listOf("https://image.tmdb.org/t/p/w500null"),
            department = ""
        )

        val expectedImageUrls = listOf(
            "https://image.tmdb.org/t/p/w500/image1.jpg",
            "https://image.tmdb.org/t/p/w500/image2.jpg",
            "https://image.tmdb.org/t/p/w500null"
        )

        val expectedMovieDto1 = MovieDto(
            id = 1L,
            title = "Movie 1",
            genres = emptyList(),
            imdbRating = 8.5,
            userRating = null,
            releaseDate = "2023-01-01",
            overview = "Great movie",
            posterPictureURL = "https://image.tmdb.org/t/p/w500/poster1.jpg",
            runtimeMinutes = 0,
            trailerURL = ""
        )

        val expectedMovieDto2 = MovieDto(
            id = 2L,
            title = "Movie 2",
            genres = emptyList(),
            imdbRating = 0.0,
            userRating = null,
            releaseDate = "0001-01-01",
            overview = "",
            posterPictureURL = "https://image.tmdb.org/t/p/w500",
            runtimeMinutes = 0,
            trailerURL = ""
        )

        val expectedTvShowDto1 = TvShowDto(
            id = 1L,
            title = "TV Show 1",
            genres = emptyList(),
            imdbRating = 9.0,
            userRating = null,
            releaseDate = "2023-01-01",
            overview = "Great TV show",
            posterPictureURL = "https://image.tmdb.org/t/p/w500/tvposter1.jpg",
            numberOfSeasons = 0,
            trailerURL = "",
            headerImagesURLs = emptyList()
        )

        val expectedTvShowDto2 = TvShowDto(
            id = 2L,
            title = "Original TV Show 2",
            genres = emptyList(),
            imdbRating = 0.0,
            userRating = null,
            releaseDate = "0001-01-01",
            overview = "",
            posterPictureURL = "https://image.tmdb.org/t/p/w500null",
            numberOfSeasons = 0,
            trailerURL = "",
            headerImagesURLs = emptyList()
        )

        val expectedTrendingActorDto1 = ActorDto(
            id = 1L,
            name = "Actor 1",
            imageUrl = "https://image.tmdb.org/t/p/w500/actor1.jpg",
            biography = "",
            birthdayDate = null,
            deathDate = null,
            placeOfBirth = "",
            headerPictures = emptyList(),
            department = ""
        )

        val expectedTrendingActorDto2 = ActorDto(
            id = 2L,
            name = "Actor 2",
            imageUrl = "https://image.tmdb.org/t/p/w500/actor2.jpg",
            biography = "",
            birthdayDate = null,
            deathDate = null,
            placeOfBirth = "",
            headerPictures = emptyList(),
            department = ""
        )
    }
}