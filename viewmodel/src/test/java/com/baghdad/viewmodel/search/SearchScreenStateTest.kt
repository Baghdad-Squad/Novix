package com.baghdad.viewmodel.search

import com.baghdad.domain.model.search.RecentlyViewed
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SearchScreenStateTest {

    @Test
    fun `should return moviesFilter when selected tab is MOVIES`() {
        val moviesFilter = SearchScreenState.SearchFilterUiState(minimumRating = 7)
        val state = SearchScreenState(
            selectedSearchTab = SearchScreenState.SearchTab.MOVIES,
            bottomSheetUiState = SearchScreenState.FilterBottomSheetUiState(moviesFilter = moviesFilter)
        )

        assertThat(state.searchFilter).isEqualTo(moviesFilter)
    }

    @Test
    fun `should return tvShowsFilter when selected tab is TV_SHOWS`() {
        val tvShowsFilter = SearchScreenState.SearchFilterUiState(minimumYear = 2000)
        val state = SearchScreenState(
            selectedSearchTab = SearchScreenState.SearchTab.TV_SHOWS,
            bottomSheetUiState = SearchScreenState.FilterBottomSheetUiState(tvShowsFilter = tvShowsFilter)
        )

        assertThat(state.searchFilter).isEqualTo(tvShowsFilter)
    }

    @Test
    fun `should initialize default search screen state when no arguments provided`() {
        val state = SearchScreenState()

        assertThat(state.searchText).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.recentSearch).isEmpty()
        assertThat(state.recentViewed).isEmpty()
        assertThat(state.bottomSheetUiState.isBottomSheetVisible).isFalse()
    }

    @Test
    fun `should hold correct values in MovieUiState when provided with id, image URL and saved state`() {
        val movie =
            SearchScreenState.MovieUiState(id = 123, posterPictureURL = "url", isSaved = true)

        assertThat(movie.id).isEqualTo(123)
        assertThat(movie.posterPictureURL).isEqualTo("url")
        assertThat(movie.isSaved).isTrue()
    }

    @Test
    fun `should hold correct values in TvShowUiState when provided with id, image URL and saved state`() {
        val tvShow =
            SearchScreenState.TvShowUiState(id = 222, posterPictureURL = "tv_url", isSaved = false)

        assertThat(tvShow.id).isEqualTo(222)
        assertThat(tvShow.posterPictureURL).isEqualTo("tv_url")
        assertThat(tvShow.isSaved).isFalse()
    }

    @Test
    fun `should hold correct values in ActorUiState when provided with id, name and image URL`() {
        val actor = SearchScreenState.ActorUiState(
            id = 99,
            name = "Jane Doe",
            profilePictureURL = "pic.jpg"
        )

        assertThat(actor.id).isEqualTo(99)
        assertThat(actor.name).isEqualTo("Jane Doe")
        assertThat(actor.profilePictureURL).isEqualTo("pic.jpg")
    }

    @Test
    fun `should hold correct genre info when GenreUiState is created with id and name`() {
        val genre = SearchScreenState.GenreUiState(id = 5, name = "Fantasy")

        assertThat(genre.id).isEqualTo(5)
        assertThat(genre.name).isEqualTo("Fantasy")
    }

    @Test
    fun `should hold recent search query when RecentSearchUiState is initialized`() {
        val recentSearch = SearchScreenState.RecentSearchUiState(id = 1, query = "Sci-Fi")

        assertThat(recentSearch.id).isEqualTo(1)
        assertThat(recentSearch.query).isEqualTo("Sci-Fi")
    }

    @Test
    fun `should store correct content type when RecentlyViewedUiState is initialized with TV_SHOW`() {
        val recent = SearchScreenState.RecentlyViewedUiState(
            id = 42,
            posterPictureURL = "thumb.jpg",
            contentType = RecentlyViewed.ContentType.TV_SHOW
        )

        assertThat(recent.id).isEqualTo(42)
        assertThat(recent.posterPictureURL).isEqualTo("thumb.jpg")
        assertThat(recent.contentType).isEqualTo(RecentlyViewed.ContentType.TV_SHOW)
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

        assertThat(state.searchFilter).isEqualTo(moviesFilter)

        val switchedState = state.copy(selectedSearchTab = SearchScreenState.SearchTab.TV_SHOWS)
        assertThat(switchedState.searchFilter).isEqualTo(tvShowsFilter)
    }

    @Test
    fun `should return empty list when selectedGenres or allGenres is not populated`() {
        val filter = SearchScreenState.SearchFilterUiState(
            selectedGenres = emptyList(),
            allGenres = emptyList()
        )

        assertThat(filter.selectedGenres).isEmpty()
        assertThat(filter.allGenres).isEmpty()
    }

    @Test
    fun `should accept boundary year values when initializing SearchFilterUiState`() {
        val filter = SearchScreenState.SearchFilterUiState(
            minimumYear = 1874,
            maximumYear = 2035
        )

        assertThat(filter.minimumYear).isEqualTo(1874)
        assertThat(filter.maximumYear).isEqualTo(2035)
    }

    @Test
    fun `should initialize empty movie, TV show, and actor flows by default`() {
        val state = SearchScreenState()

        assertThat(state.moviesFlow).isNotNull()
        assertThat(state.tvShowsFlow).isNotNull()
        assertThat(state.actorsFlow).isNotNull()
    }

    @Test
    fun `should correctly assign ContentType MOVIE when creating RecentlyViewedUiState`() {
        val movieView = SearchScreenState.RecentlyViewedUiState(
            contentType = RecentlyViewed.ContentType.MOVIE
        )

        assertThat(movieView.contentType).isEqualTo(RecentlyViewed.ContentType.MOVIE)
    }

    @Test
    fun `should contain default movies and TV filters when FilterBottomSheetUiState is initialized`() {
        val bottomSheetState = SearchScreenState.FilterBottomSheetUiState()

        assertThat(bottomSheetState.moviesFilter)
            .isEqualTo(SearchScreenState.SearchFilterUiState())
        assertThat(bottomSheetState.tvShowsFilter)
            .isEqualTo(SearchScreenState.SearchFilterUiState())
    }
}