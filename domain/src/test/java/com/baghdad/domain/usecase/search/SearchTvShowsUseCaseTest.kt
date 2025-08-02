package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchTvShowsUseCaseTest {

    @BeforeEach
    fun setUp() {
        searchRepository = mockk(relaxed = true)
        favoriteGenreRepository = mockk(relaxed = true)
        searchTvShowsUseCase =
            SearchTvShowsUseCase(searchRepository, favoriteGenreRepository)
    }

    @Test
    fun `searchTvShowsUseCase() should sort by favorite genre score correctly when favorite genres exist`() = runTest {
        val query = "Sort Test"
        val page = 1

        val favoriteGenres = mapOf("Drama" to 10, "Action" to 5, "Comedy" to 1)
        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns favoriteGenres
        coEvery { searchRepository.searchTvShowsByName(query, page) } returns sampleTvShows

        val result = searchTvShowsUseCase(query, page)

        assertThat(result.data).hasSize(3)
        assertThat(result.data[0].title).isEqualTo("Game of Thrones")
        assertThat(result.data[1].title).isEqualTo("Breaking Bad")
        assertThat(result.data[2].title).isEqualTo("The Office")
    }

    @Test
    fun `searchTvShowsUseCase() should make exactly one repository call when executed`() = runTest {
        val query = "Single Call"
        val page = 1
        SearchFilter(
            minimumYear = 2010,
            maximumYear = 2020,
            selectedGenres = emptyList(),
            minimumRating = 7
        )

        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns emptyMap()
        coEvery { searchRepository.searchTvShowsByName(query, page) } returns sampleTvShows

        searchTvShowsUseCase(query, page)

        coVerify(exactly = 1) { searchRepository.searchTvShowsByName(query, page) }
        coVerify(exactly = 1) { favoriteGenreRepository.getFavoriteGenres() }
    }

    @Test
    fun `searchTvShowsUseCase() should preserve pagination keys when repository returns paginated result`() = runTest {
        val query = "Pagination"
        val page = 2
        val paginatedResult = sampleTvShows.copy(
            prevKey = 1,
            nextKey = 3,
            data = listOf(sampleTvShows.data[0])
        )

        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns emptyMap()
        coEvery { searchRepository.searchTvShowsByName(query, page) } returns paginatedResult

        val result = searchTvShowsUseCase(query, page)

        assertThat(result.prevKey).isEqualTo(1)
        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.data).hasSize(1)
    }

    companion object {
        private lateinit var searchRepository: SearchRepository
        private lateinit var favoriteGenreRepository: FavoriteGenreRepository
        private lateinit var searchTvShowsUseCase: SearchTvShowsUseCase

        private val sampleTvShows = PagedResult(
            prevKey = null,
            nextKey = 2,
            data = listOf(
                TvShow(
                    id = 1L,
                    title = "Game of Thrones",
                    overview = "Fantasy drama",
                    posterImageURL = "https://example.com/got.jpg",
                    headerImagesURLs = listOf("https://example.com/got_backdrop.jpg"),
                    averageRating = 8.5,
                    releaseDate = LocalDate(2011, 4, 17),
                    genres = listOf(Genre(1L, "Drama"), Genre(2L, "Action")),
                    numberOfSeasons = 8,
                    userRating = 9.0,
                    trailerURL = "https://example.com/got_trailer.mp4"
                ),
                TvShow(
                    id = 2L,
                    title = "Breaking Bad",
                    overview = "Crime drama",
                    posterImageURL = "https://example.com/bb.jpg",
                    headerImagesURLs = listOf("https://example.com/bb_backdrop.jpg"),
                    averageRating = 9.1,
                    releaseDate = LocalDate(2011, 1, 20),
                    genres = listOf(Genre(1L, "Drama")),
                    numberOfSeasons = 5,
                    userRating = 9.0,
                    trailerURL = "https://example.com/bb_trailer.mp4"
                ),
                TvShow(
                    id = 3L,
                    title = "The Office",
                    overview = "Mockumentary sitcom",
                    posterImageURL = "https://example.com/office.jpg",
                    headerImagesURLs = listOf("https://example.com/office_backdrop.jpg"),
                    averageRating = 6.8,
                    releaseDate = LocalDate(2019, 7, 15),
                    genres = listOf(Genre(3L, "Comedy")),
                    numberOfSeasons = 9,
                    userRating = 9.0,
                    trailerURL = "https://example.com/office_trailer.mp4"
                )
            )
        )
    }
}