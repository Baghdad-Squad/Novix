package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SearchApiService
import com.baghdad.remoteDataSource.response.search.ActorSearchResponse
import com.baghdad.remoteDataSource.response.search.MovieSearchResponse
import com.baghdad.remoteDataSource.response.search.TvShowSearchResponse
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteSearchDataSourceImplTest {

    private val searchApiService = mockk<SearchApiService>()
    private val logger = mockk<Logger>(relaxed = true)
    private val dataSource = RemoteSearchDataSourceImpl(searchApiService, logger)

    @Test
    fun `should return paged movies when searchMovies is called`() = runTest {
        val successResponse = Response.success(movieSearchResponse)
        coEvery { searchApiService.searchMovies(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchMovies(QUERY, PAGE, genres)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0]).isEqualTo(expectedMovieDto)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { searchApiService.searchMovies(QUERY, PAGE) }
    }

    @Test
    fun `should filter out movies with null ids when searchMovies is called`() = runTest {
        val successResponse = Response.success(movieSearchResponseWithNulls)
        coEvery { searchApiService.searchMovies(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchMovies(QUERY, PAGE, genres)

        assertThat(result.data).hasSize(2)
        assertThat(result.data[0]).isEqualTo(expectedMovieDto)
        assertThat(result.data[1]).isEqualTo(expectedMovieDtoWithDefaults)
    }

    @Test
    fun `should return empty list when searchMovies receives empty results`() = runTest {
        val successResponse = Response.success(movieSearchResponseEmpty)
        coEvery { searchApiService.searchMovies(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchMovies(QUERY, PAGE, genres)

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should return empty list when searchMovies receives null results`() = runTest {
        val successResponse = Response.success(movieSearchResponseNullResults)
        coEvery { searchApiService.searchMovies(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchMovies(QUERY, PAGE, genres)

        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should search movies with different query and page`() = runTest {
        val differentQuery = "different query"
        val differentPage = 2
        val successResponse = Response.success(movieSearchResponse)
        coEvery { searchApiService.searchMovies(differentQuery, differentPage) } returns successResponse

        dataSource.searchMovies(differentQuery, differentPage, genres)

        coVerify(exactly = 1) { searchApiService.searchMovies(differentQuery, differentPage) }
    }

    @Test
    fun `should return paged tv shows when searchTvShows is called`() = runTest {
        val successResponse = Response.success(tvShowSearchResponse)
        coEvery { searchApiService.searchTvShows(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchTvShows(QUERY, PAGE, genres)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0]).isEqualTo(expectedTvShowDto)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { searchApiService.searchTvShows(QUERY, PAGE) }
    }

    @Test
    fun `should filter out tv shows with null ids when searchTvShows is called`() = runTest {
        val successResponse = Response.success(tvShowSearchResponseWithNulls)
        coEvery { searchApiService.searchTvShows(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchTvShows(QUERY, PAGE, genres)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0]).isEqualTo(expectedTvShowDto)
    }

    @Test
    fun `should return empty list when searchTvShows receives empty results`() = runTest {
        val successResponse = Response.success(tvShowSearchResponseEmpty)
        coEvery { searchApiService.searchTvShows(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchTvShows(QUERY, PAGE, genres)

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should search tv shows with different query and page`() = runTest {
        val differentQuery = "different query"
        val differentPage = 3
        val successResponse = Response.success(tvShowSearchResponse)
        coEvery { searchApiService.searchTvShows(differentQuery, differentPage) } returns successResponse

        dataSource.searchTvShows(differentQuery, differentPage, genres)

        coVerify(exactly = 1) { searchApiService.searchTvShows(differentQuery, differentPage) }
    }

    @Test
    fun `should return paged actors when searchActors is called`() = runTest {
        val successResponse = Response.success(actorSearchResponse)
        coEvery { searchApiService.searchActors(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchActors(QUERY, PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0]).isEqualTo(expectedActorDto)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { searchApiService.searchActors(QUERY, PAGE) }
    }

    @Test
    fun `should filter out actors with null ids when searchActors is called`() = runTest {
        val successResponse = Response.success(actorSearchResponseWithNulls)
        coEvery { searchApiService.searchActors(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchActors(QUERY, PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0]).isEqualTo(expectedActorDto)
    }

    @Test
    fun `should return empty list when searchActors receives empty results`() = runTest {
        val successResponse = Response.success(actorSearchResponseEmpty)
        coEvery { searchApiService.searchActors(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchActors(QUERY, PAGE)

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should search actors with different query and page`() = runTest {
        val differentQuery = "different actor"
        val differentPage = 2
        val successResponse = Response.success(actorSearchResponse)
        coEvery { searchApiService.searchActors(differentQuery, differentPage) } returns successResponse

        dataSource.searchActors(differentQuery, differentPage)

        coVerify(exactly = 1) { searchApiService.searchActors(differentQuery, differentPage) }
    }

    @Test
    fun `should handle tv show with null poster path`() = runTest {
        val tvShowWithNullPoster = tvShowResult.copy(posterPath = null)
        val responseWithNullPoster = tvShowSearchResponse.copy(results = listOf(tvShowWithNullPoster))
        val successResponse = Response.success(responseWithNullPoster)
        coEvery { searchApiService.searchTvShows(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchTvShows(QUERY, PAGE, genres)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].posterPictureURL).isEmpty()
    }

    @Test
    fun `should handle actor with null profile path`() = runTest {
        val actorWithNullProfile = actorResult.copy(profilePath = null)
        val responseWithNullProfile = actorSearchResponse.copy(results = listOf(actorWithNullProfile))
        val successResponse = Response.success(responseWithNullProfile)
        coEvery { searchApiService.searchActors(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchActors(QUERY, PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].imageUrl).isEmpty()
    }

    @Test
    fun `should filter genres correctly for movies`() = runTest {
        val movieWithPartialGenres = movieResult.copy(genreIds = listOf(GENRE_ID_1, 999L)) // 999L doesn't exist in genres
        val responseWithPartialGenres = movieSearchResponse.copy(results = listOf(movieWithPartialGenres))
        val successResponse = Response.success(responseWithPartialGenres)
        coEvery { searchApiService.searchMovies(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchMovies(QUERY, PAGE, genres)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].genres).hasSize(1)
        assertThat(result.data[0].genres[0]).isEqualTo(genre1)
    }

    @Test
    fun `should handle empty genre list for movies`() = runTest {
        val successResponse = Response.success(movieSearchResponse)
        coEvery { searchApiService.searchMovies(QUERY, PAGE) } returns successResponse

        val result = dataSource.searchMovies(QUERY, PAGE, emptyList())

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].genres).isEmpty()
    }

    companion object {
        const val QUERY = "test query"
        const val PAGE = 1
        const val PAGE_SIZE = 20
        const val TOTAL_PAGES = 5
        const val TOTAL_RESULTS = 100
        const val MOVIE_ID = 123L
        const val TV_SHOW_ID = 456L
        const val ACTOR_ID = 789L
        const val GENRE_ID_1 = 28L
        const val GENRE_ID_2 = 12L
        const val MOVIE_TITLE = "Test Movie"
        const val TV_SHOW_TITLE = "Test TV Show"
        const val ACTOR_NAME = "Test Actor"
        const val OVERVIEW = "Test overview"
        const val RELEASE_DATE = "2023-01-01"
        const val POSTER_PATH = "/poster.jpg"
        const val PROFILE_PATH = "/profile.jpg"
        const val VOTE_AVERAGE = 7.5
        const val POPULARITY = 8.5
        const val KNOWN_FOR_DEPARTMENT = "Acting"

        val genre1 = GenreDto(id = GENRE_ID_1, name = "Action", type = GenreDto.GenreType.MOVIE)
        val genre2 = GenreDto(id = GENRE_ID_2, name = "Adventure", type = GenreDto.GenreType.MOVIE)

        val genres = listOf(genre1, genre2)

        val movieResult = MovieSearchResponse.Result(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            overview = OVERVIEW,
            releaseDate = RELEASE_DATE,
            posterPath = POSTER_PATH,
            voteAverage = VOTE_AVERAGE,
            genreIds = listOf(GENRE_ID_1, GENRE_ID_2),
            adult = false,
            backdropPath = "/backdrop.jpg",
            originalLanguage = "en",
            originalTitle = "Original Test Movie",
            popularity = POPULARITY,
            video = false,
            voteCount = 1000
        )

        val movieResultWithNulls = MovieSearchResponse.Result(
            id = null,
            title = null,
            overview = null,
            releaseDate = null,
            posterPath = null,
            voteAverage = null,
            genreIds = null,
            adult = null,
            backdropPath = null,
            originalLanguage = null,
            originalTitle = null,
            popularity = null,
            video = null,
            voteCount = null
        )

        val movieResultValidId = MovieSearchResponse.Result(
            id = MOVIE_ID,
            title = null,
            overview = null,
            releaseDate = null,
            posterPath = null,
            voteAverage = null,
            genreIds = null
        )

        val movieSearchResponse = MovieSearchResponse(
            page = PAGE,
            results = listOf(movieResult),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val movieSearchResponseWithNulls = MovieSearchResponse(
            page = PAGE,
            results = listOf(movieResult, movieResultWithNulls, movieResultValidId),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val movieSearchResponseEmpty = MovieSearchResponse(
            page = PAGE,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        val movieSearchResponseNullResults = MovieSearchResponse(
            page = PAGE,
            results = null,
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val tvShowResult = TvShowSearchResponse.Result(
            id = TV_SHOW_ID,
            title = TV_SHOW_TITLE,
            overview = OVERVIEW,
            releaseDate = RELEASE_DATE,
            posterPath = POSTER_PATH,
            voteAverage = VOTE_AVERAGE,
            genreIds = listOf(GENRE_ID_1, GENRE_ID_2),
            adult = false,
            backdropPath = "/backdrop.jpg",
            originalLanguage = "en",
            originalTitle = "Original Test TV Show",
            popularity = POPULARITY,
            video = false,
            voteCount = 1000
        )

        val tvShowResultWithNulls = TvShowSearchResponse.Result(
            id = null,
            title = null,
            overview = null,
            releaseDate = null,
            posterPath = null,
            voteAverage = null,
            genreIds = null
        )

        val tvShowSearchResponse = TvShowSearchResponse(
            page = PAGE,
            results = listOf(tvShowResult),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val tvShowSearchResponseWithNulls = TvShowSearchResponse(
            page = PAGE,
            results = listOf(tvShowResult, tvShowResultWithNulls),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val tvShowSearchResponseEmpty = TvShowSearchResponse(
            page = PAGE,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        val actorResult = ActorSearchResponse.Result(
            id = ACTOR_ID,
            name = ACTOR_NAME,
            profilePath = PROFILE_PATH,
            knownForDepartment = KNOWN_FOR_DEPARTMENT,
            adult = false,
            gender = 1,
            originalName = "Original Actor Name",
            popularity = POPULARITY
        )

        val actorResultWithNulls = ActorSearchResponse.Result(
            id = null,
            name = null,
            profilePath = null,
            knownForDepartment = null
        )

        val actorSearchResponse = ActorSearchResponse(
            page = PAGE,
            results = listOf(actorResult),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val actorSearchResponseWithNulls = ActorSearchResponse(
            page = PAGE,
            results = listOf(actorResult, actorResultWithNulls),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val actorSearchResponseEmpty = ActorSearchResponse(
            page = PAGE,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        val expectedMovieDto = MovieDto(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            genres = genres,
            imdbRating = VOTE_AVERAGE,
            userRating = null,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPictureURL = "https://image.tmdb.org/t/p/w500$POSTER_PATH",
            trailerURL = "",
            runtimeMinutes = 0
        )

        val expectedMovieDtoWithDefaults = MovieDto(
            id = MOVIE_ID,
            title = "",
            genres = emptyList(),
            imdbRating = 0.0,
            userRating = null,
            releaseDate = "",
            overview = "",
            posterPictureURL = "",
            trailerURL = "",
            runtimeMinutes = 0
        )

        val expectedTvShowDto = TvShowDto(
            id = TV_SHOW_ID,
            title = TV_SHOW_TITLE,
            genres = genres,
            imdbRating = VOTE_AVERAGE,
            userRating = 0,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPictureURL = "https://image.tmdb.org/t/p/w500$POSTER_PATH",
            numberOfSeasons = 1,
            trailerURL = "",
            headerImagesURLs = emptyList()
        )

        val expectedActorDto = ActorDto(
            id = ACTOR_ID,
            name = ACTOR_NAME,
            imageUrl = "https://image.tmdb.org/t/p/w500$PROFILE_PATH",
            biography = "",
            birthdayDate = null,
            deathDate = null,
            placeOfBirth = "",
            headerPictures = emptyList(),
            department = KNOWN_FOR_DEPARTMENT
        )
    }
}