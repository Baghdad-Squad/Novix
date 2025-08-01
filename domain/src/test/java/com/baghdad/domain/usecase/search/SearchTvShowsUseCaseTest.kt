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
        filterHelper = mockk(relaxed = true)
        searchTvShowsUseCase =
            SearchTvShowsUseCase(searchRepository, favoriteGenreRepository, filterHelper)
    }

    @Test
    fun `searchTvShowsUseCase() should return filtered and sorted tv shows when repository returns data and favorite genres exist`() = runTest {
        // Given
        val query = "Game"
        val page = 1
        val filter = SearchFilter(
            minimumRating = 7,
            minimumYear = 2010,
            maximumYear = 2020,
            selectedGenres = emptyList()
        )

        val favoriteGenres = mapOf("Drama" to 5, "Action" to 3, "Comedy" to 1)
        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns favoriteGenres
        coEvery { searchRepository.searchTvShowsByName(query, page) } returns sampleTvShows

        coEvery { filterHelper.matchesRatingFilter(8.5, 7) } returns true
        coEvery { filterHelper.matchesRatingFilter(9.1, 7) } returns true
        coEvery { filterHelper.matchesRatingFilter(6.8, 7) } returns false

        coEvery { filterHelper.matchesYearFilter(2011, 2010, 2020) } returns true
        coEvery { filterHelper.matchesYearFilter(2019, 2010, 2020) } returns true

        coEvery {
            filterHelper.matchesGenreFilter(
                listOf(Genre(1L, "Drama"), Genre(2L, "Action")),
                emptyList()
            )
        } returns true
        coEvery {
            filterHelper.matchesGenreFilter(
                listOf(Genre(1L, "Drama")),
                emptyList()
            )
        } returns true

        // When
        val result = searchTvShowsUseCase(query, filter, page)

        // Then
        assertThat(result.data).hasSize(2)
        assertThat(result.data[0].title).isEqualTo("Game of Thrones")
        assertThat(result.data[1].title).isEqualTo("Breaking Bad")
    }

    @Test
    fun `searchTvShowsUseCase() should return empty result when no matches found after filtering`() = runTest {
        val query = "No Match"
        val page = 1
        val filter = SearchFilter(
            minimumRating = 9,
            minimumYear = 2010,
            maximumYear = 2020,
            selectedGenres = emptyList()
        )

        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns emptyMap()
        coEvery { searchRepository.searchTvShowsByName(query, page) } returns sampleTvShows
        coEvery { filterHelper.matchesRatingFilter(any(), 9) } returns false

        val result = searchTvShowsUseCase(query, filter, page)

        assertThat(result.data).isEmpty()
    }

    @Test
    fun `searchTvShowsUseCase() should apply all filters correctly when all filters are provided`() = runTest {
        val query = "Filter Test"
        val page = 1
        val filter = SearchFilter(
            minimumRating = 8,
            minimumYear = 2010,
            maximumYear = 2020,
            selectedGenres = listOf(Genre(1L, "Drama"))
        )

        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns emptyMap()
        coEvery { searchRepository.searchTvShowsByName(query, page) } returns sampleTvShows

        coEvery { filterHelper.matchesRatingFilter(8.5, 8) } returns true
        coEvery { filterHelper.matchesRatingFilter(9.1, 8) } returns true
        coEvery { filterHelper.matchesRatingFilter(6.8, 8) } returns false

        coEvery { filterHelper.matchesYearFilter(2011, 2010, 2020) } returns true
        coEvery { filterHelper.matchesYearFilter(2019, 2010, 2020) } returns true

        coEvery {
            filterHelper.matchesGenreFilter(
                listOf(Genre(1L, "Drama"), Genre(2L, "Action")),
                listOf(Genre(1L, "Drama"))
            )
        } returns true
        coEvery {
            filterHelper.matchesGenreFilter(
                listOf(Genre(1L, "Drama")),
                listOf(Genre(1L, "Drama"))
            )
        } returns true
        coEvery {
            filterHelper.matchesGenreFilter(
                listOf(Genre(3L, "Comedy")),
                listOf(Genre(1L, "Drama"))
            )
        } returns false

        val result = searchTvShowsUseCase(query, filter, page)

        assertThat(result.data).hasSize(2)
        assertThat(result.data[0].title).isEqualTo("Game of Thrones")
        assertThat(result.data[1].title).isEqualTo("Breaking Bad")
    }

    @Test
    fun `searchTvShowsUseCase() should sort by favorite genre score correctly when favorite genres exist`() = runTest {
        val query = "Sort Test"
        val page = 1
        val filter = SearchFilter(
            minimumYear = 2010,
            maximumYear = 2020,
            selectedGenres = emptyList(),
            minimumRating = 7
        )

        val favoriteGenres = mapOf("Drama" to 10, "Action" to 5, "Comedy" to 1)
        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns favoriteGenres
        coEvery { searchRepository.searchTvShowsByName(query, page) } returns sampleTvShows

        coEvery { filterHelper.matchesRatingFilter(any(), any()) } returns true
        coEvery { filterHelper.matchesYearFilter(any(), any(), any()) } returns true
        coEvery { filterHelper.matchesGenreFilter(any(), any()) } returns true

        val result = searchTvShowsUseCase(query, filter, page)

        assertThat(result.data).hasSize(3)
        assertThat(result.data[0].title).isEqualTo("Game of Thrones")
        assertThat(result.data[1].title).isEqualTo("Breaking Bad")
        assertThat(result.data[2].title).isEqualTo("The Office")
    }

    @Test
    fun `searchTvShowsUseCase() should make exactly one repository call when executed`() = runTest {
        val query = "Single Call"
        val page = 1
        val filter = SearchFilter(
            minimumYear = 2010,
            maximumYear = 2020,
            selectedGenres = emptyList(),
            minimumRating = 7
        )

        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns emptyMap()
        coEvery { searchRepository.searchTvShowsByName(query, page) } returns sampleTvShows
        coEvery { filterHelper.matchesRatingFilter(any(), any()) } returns true
        coEvery { filterHelper.matchesYearFilter(any(), any(), any()) } returns true
        coEvery { filterHelper.matchesGenreFilter(any(), any()) } returns true

        searchTvShowsUseCase(query, filter, page)

        coVerify(exactly = 1) { searchRepository.searchTvShowsByName(query, page) }
        coVerify(exactly = 1) { favoriteGenreRepository.getFavoriteGenres() }
    }

    @Test
    fun `searchTvShowsUseCase() should preserve pagination keys when repository returns paginated result`() = runTest {
        val query = "Pagination"
        val page = 2
        val filter = SearchFilter(
            minimumYear = 2010,
            maximumYear = 2020,
            selectedGenres = emptyList(),
            minimumRating = 0
        )

        val paginatedResult = sampleTvShows.copy(
            prevKey = 1,
            nextKey = 3,
            data = listOf(sampleTvShows.data[0])
        )

        coEvery { favoriteGenreRepository.getFavoriteGenres() } returns emptyMap()
        coEvery { searchRepository.searchTvShowsByName(query, page) } returns paginatedResult
        coEvery { filterHelper.matchesRatingFilter(any(), any()) } returns true
        coEvery { filterHelper.matchesYearFilter(any(), any(), any()) } returns true
        coEvery { filterHelper.matchesGenreFilter(any(), any()) } returns true

        val result = searchTvShowsUseCase(query, filter, page)

        assertThat(result.prevKey).isEqualTo(1)
        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.data).hasSize(1)
    }

    companion object {
        private lateinit var searchRepository: SearchRepository
        private lateinit var favoriteGenreRepository: FavoriteGenreRepository
        private lateinit var filterHelper: SearchFilterHelper
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