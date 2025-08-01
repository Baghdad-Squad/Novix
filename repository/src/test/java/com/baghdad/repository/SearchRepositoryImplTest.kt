package com.baghdad.repository

import com.baghdad.entity.search.RecentSearch
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.datasource.local.LocalGenreDataSource
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.datasource.local.LocalSearchQueryDataSource
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.RecentSearchDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Locale

class SearchRepositoryImplTest {
    private lateinit var searchRemoteDataSource: RemoteSearchDataSource
    private lateinit var remoteGenreDataSource: RemoteGenreDataSource
    private lateinit var localRecentSearchDataSource: LocalRecentSearchDataSource
    private lateinit var localActorDataSource: LocalActorDataSource
    private lateinit var localMovieDataSource: LocalMovieDataSource
    private lateinit var localTvShowDataSource: LocalTvShowDataSource
    private lateinit var localGenreDataSource: LocalGenreDataSource
    private lateinit var localSearchQueryDataSource: LocalSearchQueryDataSource
    private lateinit var searchRepositoryImpl: SearchRepositoryImpl

    @BeforeEach
    fun setUp() {
        searchRemoteDataSource = mockk(relaxed = true)
        remoteGenreDataSource = mockk()
        localRecentSearchDataSource = mockk()
        localActorDataSource = mockk()
        localMovieDataSource = mockk()
        localTvShowDataSource = mockk()
        localGenreDataSource = mockk()
        localSearchQueryDataSource = mockk()

        searchRepositoryImpl = SearchRepositoryImpl(
            searchRemoteDataSource = searchRemoteDataSource,
            remoteGenreDataSource = remoteGenreDataSource,
            localRecentSearchDataSource = localRecentSearchDataSource,
            localActorDataSource = localActorDataSource,
            localMovieDataSource = localMovieDataSource,
            localTvShowDataSource = localTvShowDataSource,
            localGenreDataSource = localGenreDataSource,
            localSearchQueryDataSource = localSearchQueryDataSource
        )

    }

    @Test
    fun `searchActorsByName should fetch from remote when cache is empty`() = runTest {
        // Given
        val query = "Jane Doe"
        val page = 1
        val pageSize = 20
        val remoteActors = listOf(createMockActorDto(2L, "Jane Doe"))
        val remoteResult = PagedResultDto(remoteActors, nextKey = 2, prevKey = null)
        coEvery { localSearchQueryDataSource.deleteInvalidSearchQueries(any()) } returns Unit
        coEvery {
            localActorDataSource.searchActorsByName(
                query,
                page,
                pageSize
            )
        } returnsMany listOf(
            emptyList(),
            remoteActors
        )
        coEvery { searchRemoteDataSource.searchActors(query, page) } returns remoteResult
        coEvery { localRecentSearchDataSource.addRecentSearchQuery(query) } returns Unit
        coEvery { localActorDataSource.addActors(remoteActors) } returns Unit
        coEvery { localSearchQueryDataSource.addSearchQueries(any()) } returns Unit
        // When
        val result = searchRepositoryImpl.searchActorsByName(query, page, pageSize)
        // Then
        assertThat(1 == result.data.size).isTrue()
        assertThat("Jane Doe" == result.data[0].name).isTrue()
        assertThat(2 == result.nextKey).isTrue()
        assertThat(result.prevKey).isNull()
        coVerify { searchRemoteDataSource.searchActors(query, page) }
        coVerify { localRecentSearchDataSource.addRecentSearchQuery(query) }
        coVerify { localActorDataSource.addActors(remoteActors) }
    }


    @Test
    fun `searchMoviesByTitle should fetch from remote and update genre cache when local cache is empty`() =
        runTest {
            // Given
            val title = "Avatar"
            val page = 1
            val pageSize = 20
            val movieGenres = listOf(createMockGenreDto(1L, "Action", GenreDto.GenreType.MOVIE))
            val tvGenres = listOf(createMockGenreDto(2L, "Drama", GenreDto.GenreType.TV_SHOW))
            val remoteMovies = listOf(createMockMovieDto(3L, "Avatar"))
            val remoteResult = PagedResultDto(remoteMovies, nextKey = 2, prevKey = null)

            mockkStatic(Locale::class)
            every { Locale.getDefault().language } returns "en"

            coEvery { localSearchQueryDataSource.deleteInvalidSearchQueries(any()) } returns Unit
            coEvery {
                localMovieDataSource.searchMoviesByTitle(
                    title,
                    page,
                    pageSize
                )
            } returnsMany listOf(
                emptyList(),
                remoteMovies
            )
            coEvery { remoteGenreDataSource.getMovieGenre("en") } returns movieGenres
            coEvery { remoteGenreDataSource.getTvShowGenre("en") } returns tvGenres
            coEvery { localGenreDataSource.addGenre(any()) } returns Unit
            coEvery { localGenreDataSource.getMovieGenre("en") } returns movieGenres
            coEvery {
                searchRemoteDataSource.searchMovies(
                    title,
                    page,
                    movieGenres
                )
            } returns remoteResult
            coEvery { localRecentSearchDataSource.addRecentSearchQuery(title) } returns Unit
            coEvery { localMovieDataSource.addMovies(remoteMovies) } returns Unit
            coEvery { localSearchQueryDataSource.addSearchQueries(any()) } returns Unit

            // When
            val result = searchRepositoryImpl.searchMoviesByTitle(title, page, pageSize)

            // Then
            assertThat(1 == result.data.size).isTrue()
            assertThat("Avatar" == result.data[0].title).isTrue()
            coVerify { remoteGenreDataSource.getMovieGenre("en") }
            coVerify { remoteGenreDataSource.getTvShowGenre("en") }
            coVerify { localGenreDataSource.addGenre(movieGenres[0]) }
            coVerify { localGenreDataSource.addGenre(tvGenres[0]) }
            coVerify { searchRemoteDataSource.searchMovies(title, page, movieGenres) }
        }

    @Test
    fun `searchTvShowsByName should fetch from remote and update genre cache when local cache is empty`() =
        runTest {
            // Given
            val title = "The Office"
            val page = 1
            val pageSize = 20
            val movieGenres = listOf(createMockGenreDto(1L, "Action", GenreDto.GenreType.MOVIE))
            val tvGenres = listOf(createMockGenreDto(2L, "Comedy", GenreDto.GenreType.TV_SHOW))
            val remoteTvShows = listOf(createMockTvShowDto(3L, "The Office"))
            val remoteResult = PagedResultDto(remoteTvShows, nextKey = 2, prevKey = null)

            mockkStatic(Locale::class)
            every { Locale.getDefault().language } returns "en"

            coEvery { localSearchQueryDataSource.deleteInvalidSearchQueries(any()) } returns Unit
            coEvery {
                localTvShowDataSource.searchTvShowsByTitle(
                    title,
                    page,
                    pageSize
                )
            } returnsMany listOf(
                emptyList(),
                remoteTvShows
            )
            coEvery { remoteGenreDataSource.getMovieGenre("en") } returns movieGenres
            coEvery { remoteGenreDataSource.getTvShowGenre("en") } returns tvGenres
            coEvery { localGenreDataSource.addGenre(any()) } returns Unit
            coEvery { localGenreDataSource.getTvShowGenre("en") } returns tvGenres
            coEvery {
                searchRemoteDataSource.searchTvShows(
                    title,
                    page,
                    tvGenres
                )
            } returns remoteResult
            coEvery { localRecentSearchDataSource.addRecentSearchQuery(title) } returns Unit
            coEvery { localTvShowDataSource.addTvShows(remoteTvShows) } returns Unit
            coEvery { localSearchQueryDataSource.addSearchQueries(any()) } returns Unit

            // When
            val result = searchRepositoryImpl.searchTvShowsByName(title, page, pageSize)

            // Then
            assertThat(1 == result.data.size).isTrue()
            assertThat("The Office" == result.data[0].title).isTrue()
            coVerify { remoteGenreDataSource.getMovieGenre("en") }
            coVerify { remoteGenreDataSource.getTvShowGenre("en") }
            coVerify { searchRemoteDataSource.searchTvShows(title, page, tvGenres) }
        }

    @Test
    fun `getRecentSearches should return mapped recent searches flow`() = runTest {
        // Given
        val mockRecentSearchDtos: List<RecentSearchDto> = listOf(
            createMockRecentSearchDto(1L, "John Doe"),
            createMockRecentSearchDto(2L, "Inception")
        )
        val expectedRecentSearches = listOf(
            createMockRecentSearch(1L, "John Doe"),
            createMockRecentSearch(2L, "Inception")
        )

        coEvery { localRecentSearchDataSource.getAllRecentSearches() } returns flowOf(
            mockRecentSearchDtos
        )

        val resultFlow = searchRepositoryImpl.getRecentSearches()

        resultFlow.collect { result ->
            assertThat(expectedRecentSearches.size == result.size).isTrue()
            assertThat(expectedRecentSearches[0].id == result[0].id).isTrue()
            assertThat(expectedRecentSearches[0].query == result[0].query).isTrue()
            assertThat(expectedRecentSearches[1].id == result[1].id).isTrue()
            assertThat(expectedRecentSearches[1].query == result[1].query).isTrue()
        }

        coVerify { localRecentSearchDataSource.getAllRecentSearches() }
    }

    @Test
    fun `getRecentSearches should return empty flow when no recent searches exist`() = runTest {

        coEvery { localRecentSearchDataSource.getAllRecentSearches() } returns flowOf(emptyList())

        val resultFlow = searchRepositoryImpl.getRecentSearches()

        resultFlow.collect { result ->
            assertEquals(emptyList<RecentSearch>(), result)
        }

        coVerify { localRecentSearchDataSource.getAllRecentSearches() }
    }

    @Test
    fun `deleteRecentSearchById should call local data source with correct id`() = runTest {

        val searchId = 123L
        coEvery { localRecentSearchDataSource.deleteRecentSearchById(searchId) } returns Unit

        searchRepositoryImpl.deleteRecentSearchById(searchId)

        coVerify { localRecentSearchDataSource.deleteRecentSearchById(searchId) }
    }

    @Test
    fun `deleteAllRecentSearches should call local data source`() = runTest {

        coEvery { localRecentSearchDataSource.deleteAllRecentSearches() } returns Unit

        searchRepositoryImpl.deleteAllRecentSearches()

        coVerify { localRecentSearchDataSource.deleteAllRecentSearches() }
    }

    @Test
    fun `searchActorsByName should save recent search even when data comes from cache`() = runTest {
        // Given
        val query = "Cached Actor"
        val page = 1
        val pageSize = 20
        val cachedActors = listOf(createMockActorDto(1L, "Cached Actor"))

        coEvery { localSearchQueryDataSource.deleteInvalidSearchQueries(any()) } returns Unit
        coEvery {
            localActorDataSource.searchActorsByName(
                query,
                page,
                pageSize
            )
        } returns cachedActors
        coEvery { localRecentSearchDataSource.addRecentSearchQuery(query) } returns Unit

        // When
        val result = searchRepositoryImpl.searchActorsByName(query, page, pageSize)

        // Then
        assertThat(1 == result.data.size).isTrue()
        assertThat("Cached Actor" == result.data[0].name).isTrue()
        coVerify { localRecentSearchDataSource.addRecentSearchQuery(query) }
        coVerify(exactly = 0) { searchRemoteDataSource.searchActors(any(), any()) }
    }

    @Test
    fun `searchMoviesByTitle should save recent search even when data comes from cache`() =
        runTest {
            // Given
            val title = "Cached Movie"
            val page = 1
            val pageSize = 20
            val cachedMovies = listOf(createMockMovieDto(1L, "Cached Movie"))
            coEvery { localSearchQueryDataSource.deleteInvalidSearchQueries(any()) } returns Unit
            coEvery {
                localMovieDataSource.searchMoviesByTitle(
                    title,
                    page,
                    pageSize
                )
            } returns cachedMovies
            coEvery { localRecentSearchDataSource.addRecentSearchQuery(title) } returns Unit

            // When
            val result = searchRepositoryImpl.searchMoviesByTitle(title, page, pageSize)

            // Then
            assertThat(1 == result.data.size).isTrue()
            assertThat("Cached Movie" == result.data[0].title).isTrue()

            coVerify { localRecentSearchDataSource.addRecentSearchQuery(title) }
            coVerify(exactly = 0) { searchRemoteDataSource.searchMovies(any(), any(), any()) }
        }

    @Test
    fun `searchTvShowsByName should save recent search even when data comes from cache`() =
        runTest {
            // Given
            val title = "Cached TvShow"
            val page = 1
            val pageSize = 20
            val cachedTvShows = listOf(createMockTvShowDto(1L, "Cached TvShow"))

            coEvery { localSearchQueryDataSource.deleteInvalidSearchQueries(any()) } returns Unit
            coEvery {
                localTvShowDataSource.searchTvShowsByTitle(
                    title,
                    page,
                    pageSize
                )
            } returns cachedTvShows
            coEvery { localRecentSearchDataSource.addRecentSearchQuery(title) } returns Unit

            // When
            val result = searchRepositoryImpl.searchTvShowsByName(title, page, pageSize)

            // Then
            assertThat(1 == result.data.size).isTrue()
            assertThat("Cached TvShow" == result.data[0].title).isTrue()

            coVerify { localRecentSearchDataSource.addRecentSearchQuery(title) }
            coVerify(exactly = 0) { searchRemoteDataSource.searchTvShows(any(), any(), any()) }
        }

    companion object {

        private fun createMockActorDto(
            id: Long,
            name: String,
        ): ActorDto = ActorDto(
            id = id,
            name = name,
            imageUrl = "https://example.com/image.jpg",
            biography = "Biography",
            birthdayDate = "1990-01-01",
            deathDate = null,
            placeOfBirth = "USA",
            headerPictures = listOf("https://example.com/header.jpg"),
            department = "Acting",
        )

        private fun createMockMovieDto(
            id: Long,
            title: String
        ) = MovieDto(
            id = id,
            title = title,
            genres = listOf(createMockGenreDto(1L, "Action", GenreDto.GenreType.MOVIE)),
            imdbRating = 8.0,
            userRating = null,
            releaseDate = "2023-01-01",
            overview = "Overview",
            posterPictureURL = "https://example.com/poster.jpg",
            runtimeMinutes = 120,
            trailerURL = " "
        )

        private fun createMockTvShowDto(
            id: Long,
            title: String
        ) = TvShowDto(
            id = id,
            title = title,
            genres = listOf(createMockGenreDto(1L, "Comedy", GenreDto.GenreType.TV_SHOW)),
            imdbRating = 9.0,
            userRating = null,
            releaseDate = "2023-01-01",
            overview = "Overview",
            posterPictureURL = "https://example.com/poster.jpg",
            numberOfSeasons = 5,
            trailerURL = " ",
            headerImagesURLs = listOf("https://example.com/header.jpg")
        )

        private fun createMockGenreDto(
            id: Long,
            name: String,
            genreType: GenreDto.GenreType
        ) = GenreDto(
            id = id,
            name = name,
            type = genreType
        )

        private fun createMockRecentSearchDto(
            id: Long,
            query: String
        ): RecentSearchDto {
            return RecentSearchDto(
                id = id,
                query = query,
                searchedAt = System.currentTimeMillis()
            )
        }

        private fun createMockRecentSearch(
            id: Long,
            query: String
        ) = RecentSearch(
            id = id,
            query = query,
            searchedAt = LocalDateTime.parse("2020-09-09T10:15:30") // Fixed: proper ISO format
        )
    }
}