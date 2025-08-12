package com.baghdad.viewmodel.home

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.ContinueWatching.ContentType
import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.home.HomeScreenState.ContinueWatchingItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.ContinueWatchingItemUiState.ContentType.MOVIE
import com.baghdad.viewmodel.home.HomeScreenState.GenreUiState
import com.baghdad.viewmodel.home.HomeScreenState.PopularItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.TopRatingItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.UpcomingItemUiState
import com.baghdad.viewmodel.util.roundToFirstDecimal
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class HomeScreenMapperTest {

    @Test
    fun `should map Genre to GenreUiState when toUiState is called`() {
        val uiState = sampleGenre.toUiState()
        assertThat(uiState).isEqualTo(sampleGenreUiState)
    }

    @Test
    fun `should map ContinueWatching to ContinueWatchingItemUiState when toUiState is called`() {
        val uiState = continueWatchingMovieSample.toUiState()
        assertThat(uiState).isEqualTo(continueWatchingItemUiStateSample)
    }

    @Test
    fun `should map SavableMovie when toPopularItemUiState is called`() {
        val uiState = savableMovieSample.toPopularItemUiState()
        assertThat(uiState).isEqualTo(popularMovieUiStateSample)
    }

    @Test
    fun `should map TvShow to PopularItemUiState when toPopularItemUiState is called`() {
        val uiState = tvShowSample.toPopularItemUiState()
        assertThat(uiState).isEqualTo(popularTvShowUiStateSample)
    }

    @Test
    fun `should map Movie to TopRatingItemUiState when toTopRatingItemUiState is called`() {
        val uiState = savableMovieSample.toTopRatingItemUiState()
        assertThat(uiState).isEqualTo(topRatingMovieUiStateSample)
    }

    @Test
    fun `should map Movie to UpcomingItemUiState when toUpcomingItemUiState is called`() {
        val uiState = savableMovieSample.toUpcomingItemUiState()
        assertThat(uiState).isEqualTo(upComingItemUiStateSample)
    }

    private companion object {

        private val sampleGenre = Genre(id = 101, name = "Drama")
        private val sampleGenreUiState = GenreUiState(id = 101, name = "Drama")

        private val movieSample = Movie(
            id = 1L,
            title = "Test Movie",
            genres = listOf(sampleGenre),
            averageRating = 7.46,
            userRating = null,
            releaseDate = LocalDate(2023, 8, 7),
            overview = "Movie overview",
            posterImageURL = "https://test.movie/poster.jpg",
            trailerURL = "https://test.movie/trailer.mp4",
            runtimeMinutes = 120
        )

        private val tvShowSample = TvShow(
            id = 2L,
            title = "Test TV Show",
            genres = listOf(sampleGenre),
            averageRating = 8.23,
            userRating = 5,
            releaseDate = LocalDate(2020, 1, 1),
            overview = "Tv show overview",
            posterImageURL = "https://test.tvshow/poster.jpg",
            trailerURL = "https://test.tvshow/trailer.mp4",
            headerImagesURLs = listOf("https://test.tvshow/header1.jpg"),
            numberOfSeasons = 3
        )

        private val continueWatchingMovieSample = ContinueWatching(
            contentId = 10L,
            genreIds = listOf(101, 102),
            contentImageUrl = "https://continue.watching/image.jpg",
            contentType = ContentType.MOVIE,
            userId = 1234L,
            isSaved = false,
            listId = null
        )
        private val continueWatchingItemUiStateSample = ContinueWatchingItemUiState(
            id = 10L,
            imageUrl = "https://continue.watching/image.jpg",
            isSaved = false,
            savedListId = -1L,
            contentType = MOVIE
        )
        val savableMovieSample = SavableMovie(
            movie = movieSample, isSaved = true, listId = 200L
        )
        val popularMovieUiStateSample = PopularItemUiState(
            id = movieSample.id,
            name = movieSample.title,
            rating = movieSample.averageRating.roundToFirstDecimal(),
            imageUrl = movieSample.posterImageURL,
            isSaved = true,
            savedListId = 200L,
            type = PopularItemUiState.Type.MOVIE
        )
        val popularTvShowUiStateSample = PopularItemUiState(
            id = tvShowSample.id,
            name = tvShowSample.title,
            rating = tvShowSample.averageRating.roundToFirstDecimal(),
            imageUrl = tvShowSample.posterImageURL,
            isSaved = false,
            savedListId = -1L,
            type = PopularItemUiState.Type.TV_SHOW
        )

        val topRatingMovieUiStateSample = TopRatingItemUiState(
            id = movieSample.id,
            imageUrl = movieSample.posterImageURL,
            isSaved = savableMovieSample.isSaved,
            savedListId = savableMovieSample.listId ?: -1L,
        )
        val upComingItemUiStateSample = UpcomingItemUiState(
            id = savableMovieSample.movie.id,
            imageUrl = savableMovieSample.movie.posterImageURL,
            isSaved = savableMovieSample.isSaved,
            savedListId = savableMovieSample.listId ?: -1L,
        )

    }
}