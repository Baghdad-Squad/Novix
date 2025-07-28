package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.TvShow
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toDtos
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.model.RecentlyViewedDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowTest {

    @Test
    fun `should create TvShow and check fields`() {
        // Then
        assertThat(TV_SHOW.id).isEqualTo(1L)
        assertThat(TV_SHOW.title).isEqualTo("Game of Thrones")
        assertThat(TV_SHOW.genres).isEqualTo(listOf(1L, 2L))
        assertThat(TV_SHOW.imdbRating).isEqualTo(9.2)
        assertThat(TV_SHOW.userRating).isEqualTo(8.5)
        assertThat(TV_SHOW.releaseDate).isEqualTo("2011-04-17")
        assertThat(TV_SHOW.overview).isEqualTo("Nine noble families fight for control over the lands of Westeros, while an ancient enemy returns after being dormant for millennia.")
        assertThat(TV_SHOW.posterPictureURL).isEqualTo("https://example.com/poster.jpg")
    }

    @Test
    fun `should map TvShow to TvShowDto`() {
        // When
        val tvShowDto = TV_SHOW.toDto(GENRES.map { it.toDto() })
        // Then
        assertThat(tvShowDto.id).isEqualTo(TV_SHOW.id)
        assertThat(tvShowDto.title).isEqualTo(TV_SHOW.title)
        assertThat(tvShowDto.genres).isEqualTo(GENRES.map { it.toDto() })
    }

    @Test
    fun `should map TvShowDto to TvShow`() {
        // Given
        val tvShowDto = TV_SHOW.toDto(GENRES.map { it.toDto() })

        // When
        val tvShow = tvShowDto.toLocalDto()
        // Then
        assertThat(tvShow.id).isEqualTo(tvShowDto.id)
        assertThat(tvShow.title).isEqualTo(tvShowDto.title)
        assertThat(tvShow.genres).isEqualTo(tvShowDto.genres.map { it.id })
        assertThat(tvShow.imdbRating).isEqualTo(tvShowDto.imdbRating)
        assertThat(tvShow.userRating).isEqualTo(tvShowDto.userRating)
        assertThat(tvShow.releaseDate).isEqualTo(tvShowDto.releaseDate)
        assertThat(tvShow.overview).isEqualTo(tvShowDto.overview)
        assertThat(tvShow.posterPictureURL).isEqualTo(tvShowDto.posterPictureURL)
    }

    @Test
    fun `should map list of TvShow to list of TvShowDto`() {
        // Given
        val tvShows = listOf(
            TV_SHOW,
            TV_SHOW.copy(id = 2L),
            TV_SHOW.copy(id = 3L),
            TV_SHOW.copy(id = 4L)
        )

        // When
        val tvShowDtos = tvShows.toDtos(GENRES.associate { it.id to it })

        // Then
        assertThat(tvShowDtos.size).isEqualTo(tvShows.size)
        assertThat(tvShowDtos).isEqualTo(tvShows.map { it.toDto(GENRES.map { it.toDto() }) })
    }


    companion object {
        val TV_SHOW = TvShow(
            id = 1L,
            title = "Game of Thrones",
            genres = listOf(1L, 2L),
            imdbRating = 9.2,
            userRating = 8.5,
            releaseDate = "2011-04-17",
            overview = "Nine noble families fight for control over the lands of Westeros, while an ancient enemy returns after being dormant for millennia.",
            posterPictureURL = "https://example.com/poster.jpg",
            numberOfSeasons = 8
        )

        val GENRES = listOf(
            Genre(
                id = 1L,
                name = "Action",
                type = RecentlyViewedDto.ContentType.TV_SHOW.name
            ),
            Genre(
                id = 2L,
                name = "Adventure",
                type = RecentlyViewedDto.ContentType.TV_SHOW.name
            )
        )


    }

}