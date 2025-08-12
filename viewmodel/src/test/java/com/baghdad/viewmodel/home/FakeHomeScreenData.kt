package com.baghdad.viewmodel.home

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.home.HomeScreenState.ContinueWatchingItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.GenreUiState
import com.baghdad.viewmodel.home.HomeScreenState.PopularItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.TopRatingItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.UpcomingItemUiState
import kotlinx.datetime.LocalDate

object FakeHomeScreenData {

    val genre = Genre(
        id = 1,
        name = "Action"
    )

    val genres = listOf(
        genre,
        genre.copy(id = 2, name = "Drama"),
        genre.copy(id = 3, name = "Comedy"),
        genre.copy(id = 4, name = "Romance"),
        genre.copy(id = 5, name = "Horror")
    )

    val movie = Movie(
        id = 1,
        title = "test",
        genres = genres,
        averageRating = 1.0,
        userRating = 1.0,
        releaseDate = LocalDate(2002, 2, 22),
        overview = "test",
        posterImageURL = "test",
        trailerURL = "test",
        runtimeMinutes = 5,
    )

    val tvShow = TvShow(
        id = 1,
        title = "test",
        genres = genres,
        averageRating = 1.0,
        userRating = 1,
        releaseDate = LocalDate(2002, 2, 22),
        overview = "test",
        posterImageURL = "test",
        trailerURL = "test",
        headerImagesURLs = listOf("test"),
        numberOfSeasons = 5,
    )

    val movies = listOf(
        movie,
        movie.copy(id = 2, title = "test2"),
        movie.copy(id = 3, title = "test3"),
        movie.copy(id = 4, title = "test4"),
        movie.copy(id = 5, title = "test5"),
        movie.copy(id = 6, title = "test6"),
        movie.copy(id = 7, title = "test7"),
        movie.copy(id = 8, title = "test8")
    )

    val savableMovies = movies.map {
        SavableMovie(
            movie = it,
            isSaved = false,
            listId = null
        )
    }

    val tvShows = listOf(
        tvShow,
        tvShow.copy(id = 2, title = "test2"),
        tvShow.copy(id = 3, title = "test3"),
        tvShow.copy(id = 4, title = "test4"),
        tvShow.copy(id = 5, title = "test5"),
        tvShow.copy(id = 6, title = "test6"),
        tvShow.copy(id = 7, title = "test7"),
    )

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
        TopRatingItemUiState(id = 3, imageUrl = "url3", isSaved = true),
        TopRatingItemUiState(id = 4, imageUrl = "url4", isSaved = false)
    )

    val continueWatchingItems = listOf(
        ContinueWatching(
            contentId = 5,
            genreIds = listOf(1, 2, 3, 4),
            contentImageUrl = "urlTest",
            contentType = ContinueWatching.ContentType.MOVIE,
            userId = 5,
            isSaved = false,
            listId = null
        ),
        ContinueWatching(
            contentId = 6,
            genreIds = listOf(1, 2, 3, 4, 5),
            contentImageUrl = "urlTest2",
            contentType = ContinueWatching.ContentType.MOVIE,
            userId = 6,
            isSaved = false,
            listId = null
        )
    )

    val continueWatchingItemsUiState = listOf(
        ContinueWatchingItemUiState(
            id = 5,
            imageUrl = "url5",
            contentType = ContinueWatchingItemUiState.ContentType.MOVIE
        ),
        ContinueWatchingItemUiState(
            id = 6,
            imageUrl = "url6",
            contentType = ContinueWatchingItemUiState.ContentType.TV_SHOW
        )
    )

    val upcomingGenres = listOf(
        GenreUiState(id = 7, name = "Genre 1"),
        GenreUiState(id = 8, name = "Genre 2")
    )

    val upcomingItems = listOf(
        UpcomingItemUiState(id = 9, imageUrl = "url7", isSaved = true),
        UpcomingItemUiState(id = 10, imageUrl = "url8", isSaved = false)
    )

    val homeScreenState = HomeScreenState(
        isPopularLoading = false,
        popularItems = popularItems,
        isTopRatingLoading = false,
        topRatingItems = topRatingItems,
        isContinueWatchingLoading = false,
        continueWatchingItems = continueWatchingItemsUiState,
        isUpcomingGenresLoading = false,
        upcomingGenres = upcomingGenres,
        isUpcomingMoviesLoading = false,
        selectedUpcomingGenreId = 8,
        upcomingItems = upcomingItems,
        isLoading = true
    )
}