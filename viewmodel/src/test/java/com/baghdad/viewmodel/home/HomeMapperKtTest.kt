package com.baghdad.viewmodel.home

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.continueWatching.UserWatchedMedia.ContentType
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.Test

class HomeScreenMapperTest {

    private val sampleGenre = Genre(id = 101, name = "Drama")

    private val sampleMovie = Movie(
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

    private val sampleTvShow = TvShow(
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

    private val sampleContinueWatching = UserWatchedMedia(
        contentId = 10L,
        genreIds = listOf(101, 102),
        contentImageUrl = "https://continue.watching/image.jpg",
        contentType = ContentType.MOVIE,
        userId = 1234L
    )

    @Test
    fun `should map Genre to GenreUiState when toUiState is called`() {
        val uiState = sampleGenre.toUiState()

        assertThat(uiState.id).isEqualTo(sampleGenre.id)
        assertThat(uiState.name).isEqualTo(sampleGenre.name)
    }

    @Test
    fun `should map ContinueWatching to ContinueWatchingItemUiState when toUiState is called`() {
        val uiState = sampleContinueWatching.toUiState()

        assertThat(uiState.id).isEqualTo(sampleContinueWatching.contentId)
        assertThat(uiState.imageUrl).isEqualTo(sampleContinueWatching.contentImageUrl)
        assertThat(uiState.isSaved).isFalse()
        assertThat(uiState.contentType.name).isEqualTo(sampleContinueWatching.contentType.name)
    }

    @Test
    fun `should map SavableMovie when toPopularItemUiState is called`() {
        val savedMovie = SavedMovie(
            movie = sampleMovie,
            isSaved = true,
            listId = 200L
        )

        val uiState = savedMovie.toPopularItemUiState()

        assertThat(uiState.id).isEqualTo(sampleMovie.id)
        assertThat(uiState.name).isEqualTo(sampleMovie.title)
        assertThat(uiState.rating).isEqualTo(7.5)
        assertThat(uiState.imageUrl).isEqualTo(sampleMovie.posterImageURL)
        assertThat(uiState.isSaved).isTrue()
        assertThat(uiState.savedListId).isEqualTo(200L)
        assertThat(uiState.type).isEqualTo(HomeScreenState.PopularItemUiState.Type.MOVIE)
    }

    @Test
    fun `should map Movie to PopularItemUiState when toPopularItemUiState is called`() {
        val savedMovie = SavedMovie(
            movie = sampleMovie,
            isSaved = false,
            listId = null
        )

        val uiState = savedMovie.toPopularItemUiState()

        assertThat(uiState.savedListId).isEqualTo(-1L)
    }

    @Test
    fun `should map TvShow to PopularItemUiState when toPopularItemUiState is called`() {
        val uiState = sampleTvShow.toPopularItemUiState()

        assertThat(uiState.id).isEqualTo(sampleTvShow.id)
        assertThat(uiState.name).isEqualTo(sampleTvShow.title)
        assertThat(uiState.rating).isEqualTo(8.2)
        assertThat(uiState.imageUrl).isEqualTo(sampleTvShow.posterImageURL)
        assertThat(uiState.isSaved).isFalse()
        assertThat(uiState.type).isEqualTo(HomeScreenState.PopularItemUiState.Type.TV_SHOW)
    }

    @Test
    fun `should map Movie to TopRatingItemUiState when toTopRatingItemUiState is called`() {
        val savedMovie = SavedMovie(
            movie = sampleMovie,
            isSaved = true,
            listId = 300L
        )

        val uiState = savedMovie.toTopRatingItemUiState()

        assertThat(uiState.id).isEqualTo(sampleMovie.id)
        assertThat(uiState.imageUrl).isEqualTo(sampleMovie.posterImageURL)
        assertThat(uiState.isSaved).isTrue()
        assertThat(uiState.savedListId).isEqualTo(300L)
    }

    @Test
    fun `should map Movie to UpcomingItemUiState when toUpcomingItemUiState is called`() {
        val savedMovie = SavedMovie(
            movie = sampleMovie,
            isSaved = false,
            listId = null
        )

        val uiState = savedMovie.toUpcomingItemUiState()

        assertThat(uiState.id).isEqualTo(sampleMovie.id)
        assertThat(uiState.imageUrl).isEqualTo(sampleMovie.posterImageURL)
        assertThat(uiState.isSaved).isFalse()
        assertThat(uiState.savedListId).isEqualTo(-1L)
    }
}