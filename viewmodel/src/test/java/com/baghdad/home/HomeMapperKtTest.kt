package com.baghdad.home

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.ContinueWatching.ContentType
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.home.HomeScreenState
import com.baghdad.viewmodel.home.toPopularItemUiState
import com.baghdad.viewmodel.home.toTopRatingItemUiState
import com.baghdad.viewmodel.home.toUiState
import com.baghdad.viewmodel.home.toUpcomingItemUiState
import com.baghdad.viewmodel.movieDetails.roundToFirstDecimal
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class HomeMapperKtTest {
    @Test
    fun `should map Genre to GenreUiState when toUiState is called`() {
        // When
        val genreUiState = GENRE.toUiState()

        // Then
        assertThat(GENRE.id).isEqualTo(genreUiState.id)
        assertThat(GENRE.name).isEqualTo(genreUiState.name)
    }

    @Test
    fun `should map ContinueWatching to ContinueWatchingItemUiState when toUiState is called`() {
        // Given
        val continueWatching = ContinueWatching(
            contentId = 1,
            genreIds = listOf(1, 2, 3, 4),
            contentImageUrl = "test",
            contentType = ContentType.MOVIE,
            userId = 1
        )

        // When
        val continueWatchingItemUiState = continueWatching.toUiState()

        // Then
        assertThat(continueWatching.contentId).isEqualTo(continueWatchingItemUiState.id)
        assertThat(continueWatching.contentImageUrl).isEqualTo(continueWatchingItemUiState.imageUrl)
    }

    @Test
    fun `should map Movie to PopularItemUiState when toPopularItemUiState is called`() {
        // When
        val popularItemUiState = MOVIE.toPopularItemUiState()

        // Then
        assertThat(MOVIE.id).isEqualTo(popularItemUiState.id)
        assertThat(MOVIE.title).isEqualTo(popularItemUiState.name)
        assertThat(MOVIE.averageRating).isEqualTo(popularItemUiState.rating.roundToFirstDecimal())
        assertThat(MOVIE.posterImageURL).isEqualTo(popularItemUiState.imageUrl)
        assertThat(popularItemUiState.type).isEqualTo(HomeScreenState.PopularItemUiState.Type.MOVIE)
        assertThat(popularItemUiState.isSaved).isFalse()
    }

    @Test
    fun `should map TvShow to PopularItemUiState when toPopularItemUiState is called`() {
        //Given
        val tvShow = TvShow(
            id = 1,
            title = "test",
            genres = GENRES,
            averageRating = 1.0,
            userRating = 1.0,
            releaseDate = LocalDate(2002, 2, 22),
            overview = "test",
            posterImageURL = "test",
            trailerURL = "test",
            numberOfSeasons = 1,
            headerImagesURLs = emptyList()
        )

        // When
        val popularItemUiState = tvShow.toPopularItemUiState()

        // Then
        assertThat(tvShow.id).isEqualTo(popularItemUiState.id)
        assertThat(tvShow.title).isEqualTo(popularItemUiState.name)
        assertThat(tvShow.averageRating).isEqualTo(popularItemUiState.rating.roundToFirstDecimal())
        assertThat(tvShow.posterImageURL).isEqualTo(popularItemUiState.imageUrl)
        assertThat(popularItemUiState.type).isEqualTo(HomeScreenState.PopularItemUiState.Type.TV_SHOW)
        assertThat(popularItemUiState.isSaved).isFalse()
    }

    @Test
    fun `should map Movie to TopRatingItemUiState when toTopRatingItemUiState is called`() {
        // When
        val topRatingItemUiState = MOVIE.toTopRatingItemUiState()

        // Then
        assertThat(MOVIE.id).isEqualTo(topRatingItemUiState.id)
        assertThat(MOVIE.posterImageURL).isEqualTo(topRatingItemUiState.imageUrl)
        assertThat(topRatingItemUiState.isSaved).isFalse()
    }

    @Test
    fun `should map Movie to UpcomingItemUiState when toUpcomingItemUiState is called`() {
        // When
        val upcomingItemUiState = MOVIE.toUpcomingItemUiState()

        // Then
        assertThat(MOVIE.id).isEqualTo(upcomingItemUiState.id)
        assertThat(MOVIE.posterImageURL).isEqualTo(upcomingItemUiState.imageUrl)
        assertThat(upcomingItemUiState.isSaved).isFalse()
    }

    companion object {

        private val GENRE = Genre(
            id = 1,
            name = "Action"
        )

        private val GENRES = listOf(
            GENRE,
            GENRE.copy(id = 2, name = "Drama"),
            GENRE.copy(id = 3, name = "Comedy"),
            GENRE.copy(id = 4, name = "Romance"),
            GENRE.copy(id = 5, name = "Horror")
        )

        private val MOVIE = Movie(
            id = 1,
            title = "test",
            genres = GENRES,
            averageRating = 1.0,
            userRating = 1.0,
            releaseDate = LocalDate(2002, 2, 22),
            overview = "test",
            posterImageURL = "test",
            trailerURL = "test",
            runtimeMinutes = 5,
        )
    }
}