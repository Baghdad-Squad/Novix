package com.baghdad.viewmodel.home

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class HomeScreenStateTest {
    @Test
    fun `should have correct initial values when default HomeScreenState is created`() {
        // Given
        val homeScreenState = HomeScreenState()

        // Then
        assertThat(homeScreenState.isPopularLoading).isTrue()
        assertThat(homeScreenState.popularItems).isEmpty()
        assertThat(homeScreenState.isTopRatingLoading).isTrue()
        assertThat(homeScreenState.topRatingItems).isEmpty()
        assertThat(homeScreenState.isContinueWatchingLoading).isTrue()
        assertThat(homeScreenState.continueWatchingItems).isEmpty()
        assertThat(homeScreenState.isUpcomingGenresLoading).isTrue()
        assertThat(homeScreenState.upcomingGenres).isEmpty()
        assertThat(homeScreenState.isUpcomingMoviesLoading).isTrue()
        assertThat(homeScreenState.selectedUpcomingGenreId).isNull()
        assertThat(homeScreenState.upcomingItems).isEmpty()
        assertThat(homeScreenState.isLoading).isFalse()
    }

    @Test
    fun `should accept custom values when created with parameters`() {
        // Given
        val homeScreenState = FakeHomeScreenData.homeScreenState

        // Then
        assertThat(homeScreenState.isPopularLoading).isFalse()
        assertThat(homeScreenState.popularItems).isEqualTo(FakeHomeScreenData.popularItems)
        assertThat(homeScreenState.isTopRatingLoading).isFalse()
        assertThat(homeScreenState.topRatingItems).isEqualTo(FakeHomeScreenData.topRatingItems)
        assertThat(homeScreenState.isContinueWatchingLoading).isFalse()
        assertThat(homeScreenState.continueWatchingItems).isEqualTo(FakeHomeScreenData.continueWatchingItemsUiState)
        assertThat(homeScreenState.isUpcomingGenresLoading).isFalse()
        assertThat(homeScreenState.upcomingGenres).isEqualTo(FakeHomeScreenData.upcomingGenres)
        assertThat(homeScreenState.isUpcomingMoviesLoading).isFalse()
        assertThat(homeScreenState.selectedUpcomingGenreId).isEqualTo(8)
        assertThat(homeScreenState.upcomingItems).isEqualTo(FakeHomeScreenData.upcomingItems)
        assertThat(homeScreenState.isLoading).isTrue()
    }

    @Test
    fun `should set correct default values when PopularItemUiState is created with default constructor`() {
        // Given
        val popularItemUiState = HomeScreenState.PopularItemUiState()

        // Then
        assertThat(popularItemUiState.id).isEqualTo(0L)
        assertThat(popularItemUiState.name).isEqualTo("")
        assertThat(popularItemUiState.rating).isEqualTo(0.0)
        assertThat(popularItemUiState.imageUrl).isEqualTo("")
        assertThat(popularItemUiState.isSaved).isFalse()
        assertThat(popularItemUiState.type).isEqualTo(HomeScreenState.PopularItemUiState.Type.MOVIE)
    }

    @Test
    fun `should set correct values when PopularItemUiState is created with parameters`() {
        // Given
        val popularItemUiState = FakeHomeScreenData.popularItems

        // Then
        assertThat(popularItemUiState[0].id).isEqualTo(1)
        assertThat(popularItemUiState[0].name).isEqualTo("Item 1")
        assertThat(popularItemUiState[0].rating).isEqualTo(4.5)
        assertThat(popularItemUiState[0].imageUrl).isEqualTo("url1")
        assertThat(popularItemUiState[0].isSaved).isTrue()
        assertThat(popularItemUiState[0].type).isEqualTo(HomeScreenState.PopularItemUiState.Type.MOVIE)
    }

    @Test
    fun `should set correct default values when TopRatingItemUiState is created with default constructor`() {
        // Given
        val topRatingItemUiState = HomeScreenState.TopRatingItemUiState()

        // Then
        assertThat(topRatingItemUiState.id).isEqualTo(0L)
        assertThat(topRatingItemUiState.imageUrl).isEqualTo("")
        assertThat(topRatingItemUiState.isSaved).isFalse()
    }

    @Test
    fun `should set correct values when TopRatingItemUiState is created with parameters`() {
        // Given
        val topRatingItemUiState = FakeHomeScreenData.topRatingItems

        // Then
        assertThat(topRatingItemUiState[0].id).isEqualTo(3)
        assertThat(topRatingItemUiState[0].imageUrl).isEqualTo("url3")
        assertThat(topRatingItemUiState[0].isSaved).isTrue()
    }

    @Test
    fun `should set correct default values when ContinueWatchingItemUiState is created with default constructor`() {
        // Given
        val continueWatchingItemUiState = HomeScreenState.ContinueWatchingItemUiState(contentType = HomeScreenState.ContinueWatchingItemUiState.ContentType.MOVIE)

        // Then
        assertThat(continueWatchingItemUiState.id).isEqualTo(0L)
        assertThat(continueWatchingItemUiState.imageUrl).isEqualTo("")
        assertThat(continueWatchingItemUiState.isSaved).isFalse()
    }

    @Test
    fun `should set correct values when ContinueWatchingItemUiState is created with parameters`() {
        // Given
        val continueWatchingItemUiState = FakeHomeScreenData.continueWatchingItemsUiState

        // Then
        assertThat(continueWatchingItemUiState[0].id).isEqualTo(5)
        assertThat(continueWatchingItemUiState[0].imageUrl).isEqualTo("url5")
        assertThat(continueWatchingItemUiState[0].isSaved).isFalse()
    }

    @Test
    fun `should set correct default values when GenreUiState is created with default constructor`() {
        // Given
        val genreUiState = HomeScreenState.GenreUiState()

        // Then
        assertThat(genreUiState.id).isEqualTo(0)
        assertThat(genreUiState.name).isEqualTo("")
    }

    @Test
    fun `should set correct values when GenreUiState is created with parameters`() {
        // Given
        val genreUiState = FakeHomeScreenData.upcomingGenres

        // Then
        assertThat(genreUiState[0].id).isEqualTo(7)
        assertThat(genreUiState[0].name).isEqualTo("Genre 1")
    }

    @Test
    fun `should set correct default values when UpcomingItemUiState is created with default constructor`() {
        // Given
        val upcomingItemUiState = HomeScreenState.UpcomingItemUiState()

        // Then
        assertThat(upcomingItemUiState.id).isEqualTo(0L)
        assertThat(upcomingItemUiState.imageUrl).isEqualTo("")
        assertThat(upcomingItemUiState.isSaved).isFalse()
    }

    @Test
    fun `should set correct values when UpcomingItemUiState is created with parameters`() {
        // Given
        val upcomingItemUiState = FakeHomeScreenData.upcomingItems

        // Then
        assertThat(upcomingItemUiState[0].id).isEqualTo(9)
        assertThat(upcomingItemUiState[0].imageUrl).isEqualTo("url7")
        assertThat(upcomingItemUiState[0].isSaved).isTrue()
    }

    @Test
    fun `should set correct values when copy is called`() {
        // Given
        val homeScreenState = FakeHomeScreenData.homeScreenState

        // When
        val copiedScreenState = homeScreenState.copy(
            isPopularLoading = false,
            popularItems = FakeHomeScreenData.popularItems,
            isTopRatingLoading = false,
            topRatingItems = FakeHomeScreenData.topRatingItems,
            isContinueWatchingLoading = false,
            continueWatchingItems = FakeHomeScreenData.continueWatchingItemsUiState,
            isUpcomingGenresLoading = false,
            upcomingGenres = FakeHomeScreenData.upcomingGenres,
            isUpcomingMoviesLoading = false,
            selectedUpcomingGenreId = 8,
            upcomingItems = FakeHomeScreenData.upcomingItems,
            isLoading = true
        )

        // Then
        assertThat(copiedScreenState.isPopularLoading).isFalse()
        assertThat(copiedScreenState.popularItems).isEqualTo(FakeHomeScreenData.popularItems)
        assertThat(copiedScreenState.isTopRatingLoading).isFalse()
        assertThat(copiedScreenState.topRatingItems).isEqualTo(FakeHomeScreenData.topRatingItems)
        assertThat(copiedScreenState.isContinueWatchingLoading).isFalse()
        assertThat(copiedScreenState.continueWatchingItems).isEqualTo(FakeHomeScreenData.continueWatchingItemsUiState)
        assertThat(copiedScreenState.isUpcomingGenresLoading).isFalse()
        assertThat(copiedScreenState.upcomingGenres).isEqualTo(FakeHomeScreenData.upcomingGenres)
        assertThat(copiedScreenState.isUpcomingMoviesLoading).isFalse()
        assertThat(copiedScreenState.selectedUpcomingGenreId).isEqualTo(8)
        assertThat(copiedScreenState.upcomingItems).isEqualTo(FakeHomeScreenData.upcomingItems)
        assertThat(copiedScreenState.isLoading).isTrue()
    }

    @Test
    fun `should implement BaseUiState interface`() {
        // Given
        val homeScreenState = FakeHomeScreenData.homeScreenState

        // Then
        assertThat(homeScreenState.isLoading).isTrue()
    }
}
