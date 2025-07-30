package com.baghdad.home

import com.baghdad.viewmodel.home.HomeScreenState
import com.baghdad.viewmodel.home.HomeScreenState.ContinueWatchingItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.GenreUiState
import com.baghdad.viewmodel.home.HomeScreenState.PopularItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.TopRatingItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.UpcomingItemUiState

object FakeHomeScreenData {

    val popularItems = listOf(
        PopularItemUiState(
            id = 1,
            name = "Item 1",
            rating = 4.5,
            imageUrl = "url1",
            isSaved = true,
            type = PopularItemUiState.Type.MOVIE
        ),
        PopularItemUiState(
            id = 2,
            name = "Item 2",
            rating = 3.8,
            imageUrl = "url2",
            isSaved = false,
            type = PopularItemUiState.Type.TV_SHOW
        )
    )

    val topRatingItems = listOf(
        TopRatingItemUiState(
            id = 3,
            imageUrl = "url3",
            isSaved = true
        ),
        TopRatingItemUiState(
            id = 4,
            imageUrl = "url4",
            isSaved = false
        )
    )

    val continueWatchingItems = listOf(
        ContinueWatchingItemUiState(
            id = 5,
            imageUrl = "url5",
            isSaved = true
        ),
        ContinueWatchingItemUiState(
            id = 6,
            imageUrl = "url6",
            isSaved = false
        )
    )

    val upcomingGenres = listOf(
        GenreUiState(
            id = 7,
            name = "Genre 1"
        ),
        GenreUiState(
            id = 8,
            name = "Genre 2"
        )
    )

    val upcomingItems = listOf(
        UpcomingItemUiState(
            id = 9,
            imageUrl = "url7",
            isSaved = true
        ),
        UpcomingItemUiState(
            id = 10,
            imageUrl = "url8",
            isSaved = false
        )
    )

    val homeScreenState = HomeScreenState(
        isPopularLoading = false,
        popularItems = popularItems,
        isTopRatingLoading = false,
        topRatingItems = topRatingItems,
        isContinueWatchingLoading = false,
        continueWatchingItems = continueWatchingItems,
        isUpcomingGenresLoading = false,
        upcomingGenres = upcomingGenres,
        isUpcomingMoviesLoading = false,
        selectedUpcomingGenreId = 8,
        upcomingItems = upcomingItems,
        isLoading = true
    )
}
