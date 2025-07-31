package com.baghdad.viewmodel.search

import com.baghdad.domain.model.search.RecentlyViewed
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class SearchScreenStateTest {

    @Test
    fun `should return moviesFilter when selected tab is MOVIES`() {
        val moviesFilter = SearchScreenState.SearchFilterUiState(minimumRating = 7)
        val state = SearchScreenState(
            selectedSearchTab = SearchScreenState.SearchTab.MOVIES,
            bottomSheetUiState = SearchScreenState.FilterBottomSheetUiState(moviesFilter = moviesFilter)
        )

        Truth.assertThat(state.searchFilter).isEqualTo(moviesFilter)
    }

    @Test
    fun `should return tvShowsFilter when selected tab is TV_SHOWS`() {
        val tvShowsFilter = SearchScreenState.SearchFilterUiState(minimumYear = 2000)
        val state = SearchScreenState(
            selectedSearchTab = SearchScreenState.SearchTab.TV_SHOWS,
            bottomSheetUiState = SearchScreenState.FilterBottomSheetUiState(tvShowsFilter = tvShowsFilter)
        )

        Truth.assertThat(state.searchFilter).isEqualTo(tvShowsFilter)
    }

    @Test
    fun `should initialize default search screen state when no arguments provided`() {
        val state = SearchScreenState()

        Truth.assertThat(state.searchText).isEmpty()
        Truth.assertThat(state.isLoading).isFalse()
        Truth.assertThat(state.recentSearch).isEmpty()
        Truth.assertThat(state.recentViewed).isEmpty()
        Truth.assertThat(state.bottomSheetUiState.isBottomSheetVisible).isFalse()
    }

    @Test
    fun `should hold correct values in MovieUiState when provided with id, image URL and saved state`() {
        val movie =
            SearchScreenState.MovieUiState(id = 123, posterPictureURL = "url", isSaved = true)

        Truth.assertThat(movie.id).isEqualTo(123)
        Truth.assertThat(movie.posterPictureURL).isEqualTo("url")
        Truth.assertThat(movie.isSaved).isTrue()
    }

    @Test
    fun `should hold correct values in TvShowUiState when provided with id, image URL and saved state`() {
        val tvShow =
            SearchScreenState.TvShowUiState(id = 222, posterPictureURL = "tv_url", isSaved = false)

        Truth.assertThat(tvShow.id).isEqualTo(222)
        Truth.assertThat(tvShow.posterPictureURL).isEqualTo("tv_url")
        Truth.assertThat(tvShow.isSaved).isFalse()
    }

    @Test
    fun `should hold correct values in ActorUiState when provided with id, name and image URL`() {
        val actor = SearchScreenState.ActorUiState(
            id = 99,
            name = "Jane Doe",
            profilePictureURL = "pic.jpg"
        )

        Truth.assertThat(actor.id).isEqualTo(99)
        Truth.assertThat(actor.name).isEqualTo("Jane Doe")
        Truth.assertThat(actor.profilePictureURL).isEqualTo("pic.jpg")
    }

    @Test
    fun `should hold correct genre info when GenreUiState is created with id and name`() {
        val genre = SearchScreenState.GenreUiState(id = 5, name = "Fantasy")

        Truth.assertThat(genre.id).isEqualTo(5)
        Truth.assertThat(genre.name).isEqualTo("Fantasy")
    }

    @Test
    fun `should hold recent search query when RecentSearchUiState is initialized`() {
        val recentSearch = SearchScreenState.RecentSearchUiState(id = 1, query = "Sci-Fi")

        Truth.assertThat(recentSearch.id).isEqualTo(1)
        Truth.assertThat(recentSearch.query).isEqualTo("Sci-Fi")
    }

    @Test
    fun `should store correct content type when RecentlyViewedUiState is initialized with TV_SHOW`() {
        val recent = SearchScreenState.RecentlyViewedUiState(
            id = 42,
            posterPictureURL = "thumb.jpg",
            contentType = RecentlyViewed.ContentType.TV_SHOW
        )

        Truth.assertThat(recent.id).isEqualTo(42)
        Truth.assertThat(recent.posterPictureURL).isEqualTo("thumb.jpg")
        Truth.assertThat(recent.contentType).isEqualTo(RecentlyViewed.ContentType.TV_SHOW)
    }

    @Test
    fun `should switch filter to tvShowsFilter when selected tab is changed from MOVIES to TV_SHOWS`() {
        val moviesFilter = SearchScreenState.SearchFilterUiState(minimumRating = 4)
        val tvShowsFilter = SearchScreenState.SearchFilterUiState(minimumRating = 8)

        val state = SearchScreenState(
            selectedSearchTab = SearchScreenState.SearchTab.MOVIES,
            bottomSheetUiState = SearchScreenState.FilterBottomSheetUiState(
                moviesFilter = moviesFilter,
                tvShowsFilter = tvShowsFilter
            )
        )

        Truth.assertThat(state.searchFilter).isEqualTo(moviesFilter)

        val switchedState = state.copy(selectedSearchTab = SearchScreenState.SearchTab.TV_SHOWS)
        Truth.assertThat(switchedState.searchFilter).isEqualTo(tvShowsFilter)
    }

    @Test
    fun `should return empty list when selectedGenres or allGenres is not populated`() {
        val filter = SearchScreenState.SearchFilterUiState(
            selectedGenres = emptyList(),
            allGenres = emptyList()
        )

        Truth.assertThat(filter.selectedGenres).isEmpty()
        Truth.assertThat(filter.allGenres).isEmpty()
    }

    @Test
    fun `should accept boundary year values when initializing SearchFilterUiState`() {
        val filter = SearchScreenState.SearchFilterUiState(
            minimumYear = 1874,
            maximumYear = 2035
        )

        Truth.assertThat(filter.minimumYear).isEqualTo(1874)
        Truth.assertThat(filter.maximumYear).isEqualTo(2035)
    }

    @Test
    fun `should initialize empty movie, TV show, and actor flows by default`() {
        val state = SearchScreenState()

        Truth.assertThat(state.moviesFlow).isNotNull()
        Truth.assertThat(state.tvShowsFlow).isNotNull()
        Truth.assertThat(state.actorsFlow).isNotNull()
    }

    @Test
    fun `should correctly assign ContentType MOVIE when creating RecentlyViewedUiState`() {
        val movieView = SearchScreenState.RecentlyViewedUiState(
            contentType = RecentlyViewed.ContentType.MOVIE
        )

        Truth.assertThat(movieView.contentType).isEqualTo(RecentlyViewed.ContentType.MOVIE)
    }

    @Test
    fun `should contain default movies and TV filters when FilterBottomSheetUiState is initialized`() {
        val bottomSheetState = SearchScreenState.FilterBottomSheetUiState()

        Truth.assertThat(bottomSheetState.moviesFilter)
            .isEqualTo(SearchScreenState.SearchFilterUiState())
        Truth.assertThat(bottomSheetState.tvShowsFilter)
            .isEqualTo(SearchScreenState.SearchFilterUiState())
    }
}