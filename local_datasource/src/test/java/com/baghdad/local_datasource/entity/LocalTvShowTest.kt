package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.TvShow
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toDtos
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.model.RecentlyViewedDto
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class LocalTvShowTest {

    @Test
    fun `should create TvShow and check fields`() {
        // Then
        Truth.assertThat(TV_SHOW.id).isEqualTo(1L)
        Truth.assertThat(TV_SHOW.title).isEqualTo("Game of Thrones")
        Truth.assertThat(TV_SHOW.genres).isEqualTo(listOf(1L, 2L))
        Truth.assertThat(TV_SHOW.imdbRating).isEqualTo(9.2)
        Truth.assertThat(TV_SHOW.userRating).isEqualTo(8.5)
        Truth.assertThat(TV_SHOW.releaseDate).isEqualTo("2011-04-17")
        Truth.assertThat(TV_SHOW.overview)
            .isEqualTo("Nine noble families fight for control over the lands of Westeros, while an ancient enemy returns after being dormant for millennia.")
        Truth.assertThat(TV_SHOW.posterPictureURL).isEqualTo("https://example.com/poster.jpg")
    }

    @Test
    fun `should map TvShow to TvShowDto`() {
        // When
        val tvShowDto = TV_SHOW.toDto(GENRES.map { it.toDto() })
        // Then
        Truth.assertThat(tvShowDto.id).isEqualTo(TV_SHOW.id)
        Truth.assertThat(tvShowDto.title).isEqualTo(TV_SHOW.title)
        Truth.assertThat(tvShowDto.genres).isEqualTo(GENRES.map { it.toDto() })
    }

    @Test
    fun `should map TvShowDto to TvShow`() {
        // Given
        val tvShowDto = TV_SHOW.toDto(GENRES.map { it.toDto() })

        // When
        val tvShow = tvShowDto.toLocalDto()
        // Then
        Truth.assertThat(tvShow.id).isEqualTo(tvShowDto.id)
        Truth.assertThat(tvShow.title).isEqualTo(tvShowDto.title)
        Truth.assertThat(tvShow.genres).isEqualTo(tvShowDto.genres.map { it.id })
        Truth.assertThat(tvShow.imdbRating).isEqualTo(tvShowDto.imdbRating)
        Truth.assertThat(tvShow.userRating).isEqualTo(tvShowDto.userRating)
        Truth.assertThat(tvShow.releaseDate).isEqualTo(tvShowDto.releaseDate)
        Truth.assertThat(tvShow.overview).isEqualTo(tvShowDto.overview)
        Truth.assertThat(tvShow.posterPictureURL).isEqualTo(tvShowDto.posterPictureURL)
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
        Truth.assertThat(tvShowDtos.size).isEqualTo(tvShows.size)
        Truth.assertThat(tvShowDtos).isEqualTo(tvShows.map { it.toDto(GENRES.map { it.toDto() }) })
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