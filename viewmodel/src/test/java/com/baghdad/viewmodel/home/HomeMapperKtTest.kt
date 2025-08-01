package com.baghdad.viewmodel.home

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.ContinueWatching.ContentType
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.movieDetails.roundToFirstDecimal
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class HomeMapperKtTest {
    @Test
    fun `should map Genre to GenreUiState when toUiState is called`() {
        //Given
        val genre = FakeHomeScreenData.genre
        // When
        val genreUiState = genre.toUiState()

        // Then
        assertThat(genre.id).isEqualTo(genreUiState.id)
        assertThat(genre.name).isEqualTo(genreUiState.name)
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
        // Given
        val movie = FakeHomeScreenData.movie

        // When
        val popularItemUiState = movie.toPopularItemUiState()

        // Then
        assertThat(movie.id).isEqualTo(popularItemUiState.id)
        assertThat(movie.title).isEqualTo(popularItemUiState.name)
        assertThat(movie.averageRating).isEqualTo(popularItemUiState.rating.roundToFirstDecimal())
        assertThat(movie.posterImageURL).isEqualTo(popularItemUiState.imageUrl)
        assertThat(popularItemUiState.type).isEqualTo(HomeScreenState.PopularItemUiState.Type.MOVIE)
        assertThat(popularItemUiState.isSaved).isFalse()
    }

    @Test
    fun `should map TvShow to PopularItemUiState when toPopularItemUiState is called`() {
        //Given
        val tvShow = TvShow(
            id = 1,
            title = "test",
            genres = FakeHomeScreenData.genres,
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
        // Given
        val movie = FakeHomeScreenData.movie

        // When
        val topRatingItemUiState = movie.toTopRatingItemUiState()

        // Then
        assertThat(movie.id).isEqualTo(topRatingItemUiState.id)
        assertThat(movie.posterImageURL).isEqualTo(topRatingItemUiState.imageUrl)
        assertThat(topRatingItemUiState.isSaved).isFalse()
    }

    @Test
    fun `should map Movie to UpcomingItemUiState when toUpcomingItemUiState is called`() {
        // Given
        val movie = FakeHomeScreenData.movie
        // When
        val upcomingItemUiState = movie.toUpcomingItemUiState()

        // Then
        assertThat(movie.id).isEqualTo(upcomingItemUiState.id)
        assertThat(movie.posterImageURL).isEqualTo(upcomingItemUiState.imageUrl)
        assertThat(upcomingItemUiState.isSaved).isFalse()
    }
}