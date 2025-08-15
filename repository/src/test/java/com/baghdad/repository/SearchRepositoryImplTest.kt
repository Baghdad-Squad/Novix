package com.baghdad.repository

import com.baghdad.entity.search.RecentSearch
import com.baghdad.repository.datasource.local.RecentSearchDataSource
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockActorDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.RecentSearchDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SearchRepositoryImplTest {
    private val searchRemoteDataSource: RemoteSearchDataSource = mockk()
    private val remoteGenreDataSource: RemoteGenreDataSource = mockk()
    private val recentSearchDataSource: RecentSearchDataSource = mockk()
    private val savableMovieDataSource: SavableMovieDataSource = mockk()
    private val searchRepositoryImpl: SearchRepositoryImpl = SearchRepositoryImpl(
        searchRemoteDataSource = searchRemoteDataSource,
        remoteGenreDataSource = remoteGenreDataSource,
        recentSearchDataSource = recentSearchDataSource,
        savableMovieDataSource = savableMovieDataSource,
    )

    @Test
    fun `searchActorsByName should fetch actors from remote`() = runTest {
        val query = "Test Actor"
        val page = 1
        val pageSize = 20
        val remoteActors = listOf(createMockActorDto())
        val remoteResult = PagedResultDto(remoteActors, nextKey = 2, prevKey = null)

        coEvery { searchRemoteDataSource.searchActors(query, page) } returns remoteResult
        coEvery { recentSearchDataSource.addRecentSearchQuery(query) } returns Unit
        coEvery { searchRemoteDataSource.searchActors(query, page) } returns remoteResult

        val result = searchRepositoryImpl.searchActorsByName(query, page, pageSize)

        assertThat(1 == result.data.size).isTrue()
        assertThat("Test Actor" == result.data[0].name).isTrue()
        assertThat(2 == result.nextKey).isTrue()
        assertThat(result.prevKey).isNull()
        coVerify { searchRemoteDataSource.searchActors(query, page) }
    }


    @Test
    fun `searchMoviesByTitle should fetch movies from remote`() =
        runTest {
            val title = "Avatar"
            val page = 1
            val pageSize = 20
            val movieGenres = listOf(createMockGenreDto(1L, "Action", GenreDto.GenreType.MOVIE))
            val remoteMovies = listOf(createMockMovieDto(3L, "Avatar"))
            val remoteResult = PagedResultDto(remoteMovies, nextKey = 2, prevKey = null)

            coEvery { savableMovieDataSource.getSavedMovies() } returns emptyMap()
            coEvery { remoteGenreDataSource.getMovieGenre("en") } returns movieGenres
            coEvery { recentSearchDataSource.addRecentSearchQuery(title) } returns Unit
            coEvery {
                searchRemoteDataSource.searchMovies(
                    title,
                    page,
                    movieGenres
                )
            } returns remoteResult

            val result = searchRepositoryImpl.searchMoviesByTitle(title, page, pageSize)

            assertThat(1 == result.data.size).isTrue()
            assertThat("Avatar" == result.data[0].movie.title).isTrue()
            coVerify { remoteGenreDataSource.getMovieGenre("en") }
            coVerify { searchRemoteDataSource.searchMovies(title, page, movieGenres) }
        }

    @Test
    fun `searchTvShowsByName should fetch tv shows from remote`() =
        runTest {
            val title = "The Office"
            val page = 1
            val pageSize = 20
            val tvGenres = listOf(createMockGenreDto(2L, "Comedy", GenreDto.GenreType.TV_SHOW))
            val remoteTvShows = listOf(createMockTvShowDto(3L, "The Office"))
            val remoteResult = PagedResultDto(remoteTvShows, nextKey = 2, prevKey = null)

            coEvery { remoteGenreDataSource.getTvShowGenre("en") } returns tvGenres
            coEvery { recentSearchDataSource.addRecentSearchQuery(title) } returns Unit
            coEvery {
                searchRemoteDataSource.searchTvShows(
                    title,
                    page,
                    tvGenres
                )
            } returns remoteResult

            val result = searchRepositoryImpl.searchTvShowsByName(title, page, pageSize)

            assertThat(1 == result.data.size).isTrue()
            assertThat("The Office" == result.data[0].title).isTrue()
            coVerify { remoteGenreDataSource.getTvShowGenre("en") }
            coVerify { searchRemoteDataSource.searchTvShows(title, page, tvGenres) }
        }

    @Test
    fun `getRecentSearches should return mapped recent searches flow`() = runTest {
        val mockRecentSearchDtos: List<RecentSearchDto> = listOf(
            createMockRecentSearchDto(1L, "John Doe"),
            createMockRecentSearchDto(2L, "Inception")
        )
        val expectedRecentSearches = listOf(
            createMockRecentSearch(1L, "John Doe"),
            createMockRecentSearch(2L, "Inception")
        )

        coEvery { recentSearchDataSource.getAllRecentSearches() } returns flowOf(
            mockRecentSearchDtos
        )

        val resultFlow = searchRepositoryImpl.getRecentSearches()

        resultFlow.collect { result ->
            assertThat(expectedRecentSearches.size == result.size).isTrue()
            assertThat(result).isEqualTo(expectedRecentSearches)
        }

        coVerify { recentSearchDataSource.getAllRecentSearches() }
    }

    @Test
    fun `getRecentSearches should return empty flow when no recent searches exist`() = runTest {
        coEvery { recentSearchDataSource.getAllRecentSearches() } returns flowOf(emptyList())

        val resultFlow = searchRepositoryImpl.getRecentSearches()

        resultFlow.collect { result ->
            assertEquals(emptyList<RecentSearch>(), result)
        }
        coVerify { recentSearchDataSource.getAllRecentSearches() }
    }

    @Test
    fun `deleteRecentSearchById should call local data source with correct id`() = runTest {
        val searchId = 123L
        coEvery { recentSearchDataSource.deleteRecentSearchById(searchId) } returns Unit

        searchRepositoryImpl.deleteRecentSearchById(searchId)

        coVerify { recentSearchDataSource.deleteRecentSearchById(searchId) }
    }

    @Test
    fun `deleteAllRecentSearches should call local data source`() = runTest {
        coEvery { recentSearchDataSource.deleteAllRecentSearches() } returns Unit

        searchRepositoryImpl.deleteAllRecentSearches()

        coVerify { recentSearchDataSource.deleteAllRecentSearches() }
    }

    companion object {

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
                searchedAt = 1694000000000L
            )
        }

        private fun createMockRecentSearch(
            id: Long,
            query: String
        ) = RecentSearch(
            id = id,
            query = query,
            searchedAt = LocalDateTime.parse("2023-09-06T04:33:20") // Fixed: proper ISO format
        )
    }
}