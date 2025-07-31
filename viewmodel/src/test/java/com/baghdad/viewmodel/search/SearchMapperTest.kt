package com.baghdad.viewmodel.search

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.search.RecentSearch
import com.google.common.truth.Truth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Test

class SearchMapperTest {
    @Test
    fun `should map Movie to MovieUiState when toMovieUI is called`() {
        val ui = MOVIE.toMovieUI()

        Truth.assertThat(ui.id).isEqualTo(1L)
        Truth.assertThat(ui.posterPictureURL).isEqualTo("https://example.com/posters/echoes.jpg")
    }

    @Test
    fun `should map TvShow to TvShowUiState when toTvShowUI is called`() {
        val ui = TV_SHOW.toTvShowUI()

        Truth.assertThat(ui.id).isEqualTo(1L)
        Truth.assertThat(ui.posterPictureURL).isEqualTo("https://example.com/posters/avalon.jpg")
    }

    @Test
    fun `should map Actor to ActorUiState when toActorUI is called`() {
        val ui = ACTOR.toActorUI()

        Truth.assertThat(ui.id).isEqualTo(1L)
        Truth.assertThat(ui.name).isEqualTo("Layla Nasr")
        Truth.assertThat(ui.profilePictureURL)
            .isEqualTo("https://example.com/profiles/layla_nasr.jpg")
    }

    @Test
    fun `should map RecentlyViewed to RecentlyViewedUiState when toRecentlyViewedUI is called`() {
        val ui = RECENTLY_VIEWED.toRecentlyViewedUI()

        Truth.assertThat(ui.id).isEqualTo(1L)
        Truth.assertThat(ui.posterPictureURL)
            .isEqualTo("https://example.com/images/echoes_of_eternity.jpg")
        Truth.assertThat(ui.contentType).isEqualTo(RecentlyViewed.ContentType.MOVIE)
    }

    @Test
    fun `should map RecentSearch to RecentSearchUiState correctly`() {
        val result = RECENT_SEARCH.toRecentSearchUI()

        Truth.assertThat(result.id).isEqualTo(1L)
        Truth.assertThat(result.query).isEqualTo("kotlin")
    }

    @Test
    fun `should map Genre to GenreUiState when toGenreUI is called`() {
        val ui = GENRE.toGenreUI()

        Truth.assertThat(ui.id).isEqualTo(1L)
        Truth.assertThat(ui.name).isEqualTo("Action")
    }

    @Test
    fun `should map GenreUiState to Genre when toGenre is called`() {
        val ui = SearchScreenState.GenreUiState(id = 7L, name = "Drama")
        val domain = ui.toGenre()

        Truth.assertThat(domain.id).isEqualTo(7L)
        Truth.assertThat(domain.name).isEqualTo("Drama")
    }

    @Test
    fun `should map SearchFilterUiState to SearchFilter when toSearchFilter is called`() {
        val ui = SearchScreenState.SearchFilterUiState(
            minimumYear = 1990,
            maximumYear = 2020,
            minimumRating = 3,
            selectedGenres = listOf(SearchScreenState.GenreUiState(8L, "Comedy"))
        )

        val domain = ui.toSearchFilter()

        Truth.assertThat(domain.minimumYear).isEqualTo(1990)
        Truth.assertThat(domain.maximumYear).isEqualTo(2020)
        Truth.assertThat(domain.minimumRating).isEqualTo(3)
        Truth.assertThat(domain.selectedGenres.first().name).isEqualTo("Comedy")
    }

    companion object {
        private val MOVIE = Movie(
            id = 1L,
            title = "Echoes of Eternity",
            genres = listOf(Genre(1L, "Fantasy"), Genre(2L, "Adventure")),
            averageRating = 8.3,
            userRating = 8.0,
            releaseDate = LocalDate.Companion.parse("2022-05-10"),
            overview = "In a world where dreams shape reality, a rogue dreamweaver fights to restore balance.",
            posterImageURL = "https://example.com/posters/echoes.jpg",
            trailerURL = "https://example.com/trailers/echoes.mp4",
            runtimeMinutes = 132
        )
        private val TV_SHOW = TvShow(
            id = 1L,
            title = "Chronicles of Avalon",
            genres = listOf(Genre(1L, "Fantasy"), Genre(2L, "Mystery")),
            averageRating = 9.1,
            userRating = 8.8,
            releaseDate = kotlinx.datetime.LocalDate(year = 2022, monthNumber = 9, dayOfMonth = 22),
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
            )
        )
        private val GENRE = Genre(id = 1L, name = "Action")
        private val RECENT_SEARCH = RecentSearch(
            id = 1L,
            query = "kotlin",
            searchedAt = kotlinx.datetime.LocalDateTime(
                year = 2025,
                monthNumber = 7,
                dayOfMonth = 31,
                hour = 20,
                minute = 15,
                second = 0
            )
        )
    }
}