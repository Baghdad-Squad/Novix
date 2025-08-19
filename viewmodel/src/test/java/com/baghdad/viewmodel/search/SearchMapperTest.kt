package com.baghdad.viewmodel.search

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.search.RecentSearch
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Test

class SearchMapperTest {
    @Test
    fun `should map Movie to MovieUiState when toMovieUI is called`() {
        val ui = SAVE_MOVIE.toMovieUI()

        assertThat(ui.id).isEqualTo(SAVE_MOVIE.movie.id)
        assertThat(ui.posterPictureURL).isEqualTo(SAVE_MOVIE.movie.posterImageURL)
    }

    @Test
    fun `should map TvShow to TvShowUiState when toTvShowUI is called`() {
        val ui = TV_SHOW.toTvShowUI()

        assertThat(ui.id).isEqualTo(TV_SHOW.id)
        assertThat(ui.posterPictureURL).isEqualTo(TV_SHOW.posterImageURL)
    }

    @Test
    fun `should map Actor to ActorUiState when toActorUI is called`() {
        val ui = ACTOR.toActorUI()

        assertThat(ui.id).isEqualTo(1L)
        assertThat(ui.name).isEqualTo(ACTOR.name)
        assertThat(ui.profilePictureURL)
            .isEqualTo(ACTOR.profilePictureURL)
    }

    @Test
    fun `should map RecentlyViewed to RecentlyViewedUiState when toRecentlyViewedUI is called`() {
        val ui = RECENTLY_VIEWED.toRecentlyViewedUI()

        assertThat(ui.id).isEqualTo(RECENTLY_VIEWED.listId)
        assertThat(ui.posterPictureURL)
            .isEqualTo(RECENTLY_VIEWED.contentImageUrl)
        assertThat(ui.contentType.name).isEqualTo(RECENTLY_VIEWED.contentType.name)
    }

    @Test
    fun `should map RecentSearch to RecentSearchUiState correctly`() {
        val result = RECENT_SEARCH.toRecentSearchUI()

        assertThat(result.id).isEqualTo(RECENT_SEARCH.id)
        assertThat(result.query).isEqualTo(RECENT_SEARCH.query)
    }

    companion object {

        val MOVIE = Movie(
            id = 42L,
            title = "Test Title",
            genres = listOf(Genre(1L, "Drama")),
            averageRating = 7.5,
            userRating = 7.0,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Test overview",
            posterImageURL = "https://example.com/poster.jpg",
            trailerURL = "https://example.com/trailer.mp4",
            runtimeMinutes = 120
        )
        val SAVE_MOVIE = SavedMovie(
            movie = MOVIE,
            isSaved = true,
            listId = 99L
        )
        private val TV_SHOW = TvShow(
            id = 1L,
            title = "Chronicles of Avalon",
            genres = listOf(Genre(1L, "Fantasy"), Genre(2L, "Mystery")),
            averageRating = 9.1,
            userRating = 8,
            releaseDate = LocalDate(year = 2022, month = 9, day = 22),
            overview = "In a land where ancient magic awakens once more, a young oracle must choose between destiny and freedom.",
            posterImageURL = "https://example.com/posters/avalon.jpg",
            trailerURL = "https://example.com/trailers/avalon.mp4",
            headerImagesURLs = listOf(
                "https://example.com/headers/avalon_1.jpg",
                "https://example.com/headers/avalon_2.jpg"
            ),
            numberOfSeasons = 3
        )
        private val ACTOR = Actor(
            id = 1L,
            name = "Layla Nasr",
            profilePictureURL = "https://example.com/profiles/layla_nasr.jpg",
            birthDate = LocalDate(year = 1984, month = 3, day = 27),
            placeOfBirth = "Cairo, Egypt",
            deathDate = null, // Still alive
            biography = "Layla Nasr is a versatile Egyptian actress known for her powerful performances in historical dramas and contemporary thrillers. Her breakout role in 'The Pharaoh’s Shadow' launched her international career.",
            headerPictures = listOf(
                "https://example.com/headers/layla_1.jpg",
                "https://example.com/headers/layla_2.jpg"
            ),
            department = "Acting"
        )
        private val RECENTLY_VIEWED = RecentlyViewed(
            contentId = 1L,
            contentImageUrl = "https://example.com/images/echoes_of_eternity.jpg",
            contentType = RecentlyViewed.ContentType.MOVIE,
            viewedAt = LocalDateTime(
                year = 2025,
                month = 7,
                day = 31,
                hour = 20,
                minute = 15,
                second = 0,
                nanosecond = 0
            ),
            isSaved = false,
            listId = 1L
        )
        private val RECENT_SEARCH = RecentSearch(
            id = 1L,
            query = "kotlin",
            searchedAt = LocalDateTime(
                year = 2025,
                month = 7,
                day = 31,
                hour = 20,
                minute = 15,
                second = 0,
                nanosecond = 0
            )
        )
    }
}