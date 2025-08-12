package com.baghdad.repository.mapper

import com.baghdad.repository.dummyData.DummyDataFactory.RATED_MEDIA
import com.baghdad.repository.dummyData.DummyDataFactory.TV_SHOW_DTO
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowMapperTest {

    @Test
    fun `should map TvShowDto to entity correctly when data is valid`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result).isEqualTo(TV_SHOW_DTO)
    }

    @Test
    fun `should map TvShowDto id to entity id correctly`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result.id).isEqualTo(TV_SHOW_DTO.id)
    }

    @Test
    fun `should map TvShowDto title to entity title correctly`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result.title).isEqualTo(TV_SHOW_DTO.title)
    }

    @Test
    fun `should map TvShowDto genres to entity genres correctly`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result.genres).isEqualTo(TV_SHOW_DTO.genres.map { it.toEntity() }
        )
    }

    @Test
    fun `should map TvShowDto imdbRating to entity imdbRating correctly`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result.averageRating).isEqualTo(TV_SHOW_DTO.imdbRating)
    }

    @Test
    fun `should map TvShowDto userRating to entity userRating correctly`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result.userRating).isEqualTo(TV_SHOW_DTO.userRating)
    }

    @Test
    fun `should map TvShowDto releaseDate to entity releaseDate correctly`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result.releaseDate).isEqualTo(TV_SHOW_DTO.releaseDate)
    }

    @Test
    fun `should map TvShowDto overview to entity overview correctly`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result.overview).isEqualTo(TV_SHOW_DTO.overview)
    }

    @Test
    fun `should map TvShowDto posterPictureURL to entity posterPictureURL correctly`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result.posterImageURL).isEqualTo(TV_SHOW_DTO.posterPictureURL)
    }

    @Test
    fun `should map TvShowDto headerImagesURLs to entity headerImagesURLs correctly`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result.headerImagesURLs).isEqualTo(TV_SHOW_DTO.headerImagesURLs)
    }

    @Test
    fun `should map TvShowDto trailerURL to entity trailerURL correctly`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result.trailerURL).isEqualTo(TV_SHOW_DTO.trailerURL)
    }

    @Test
    fun `should map TvShowDto numberOfSeasons to entity numberOfSeasons correctly`() {
        val result = TV_SHOW_DTO.toEntity()

        assertThat(result.numberOfSeasons).isEqualTo(TV_SHOW_DTO.numberOfSeasons)
    }

    @Test
    fun `should map empty title from TvShowDto to entity correctly`() {
        val dto = TV_SHOW_DTO.copy(title = "")
        val result = dto.toEntity()
        assertThat(result.title).isEqualTo("")
    }

    @Test
    fun `should map empty genres list from TvShowDto to entity correctly`() {
        val dto = TV_SHOW_DTO.copy(genres = emptyList())
        val result = dto.toEntity()
        assertThat(result.genres).isEmpty()
    }

    @Test
    fun `should map empty releaseDate from TvShowDto to entity correctly`() {
        val dto = TV_SHOW_DTO.copy(releaseDate = "")
        val result = dto.toEntity()
        assertThat(result.releaseDate).isEqualTo("")
    }

    @Test
    fun `should map empty overview from TvShowDto to entity correctly`() {
        val dto = TV_SHOW_DTO.copy(overview = "")
        val result = dto.toEntity()
        assertThat(result.overview).isEqualTo("")
    }

    @Test
    fun `should map empty posterPictureURL from TvShowDto to entity correctly`() {
        val dto = TV_SHOW_DTO.copy(posterPictureURL = "")
        val result = dto.toEntity()
        assertThat(result.posterImageURL).isEqualTo("")
    }

    @Test
    fun `should map empty headerImagesURLs list from TvShowDto to entity correctly`() {
        val dto = TV_SHOW_DTO.copy(headerImagesURLs = emptyList())
        val result = dto.toEntity()
        assertThat(result.headerImagesURLs).isEmpty()
    }

    @Test
    fun `should map empty trailerURL from TvShowDto to entity correctly`() {
        val dto = TV_SHOW_DTO.copy(trailerURL = "")
        val result = dto.toEntity()
        assertThat(result.trailerURL).isEqualTo("")
    }

    @Test
    fun `should map TvShowDto to RatedMedia correctly`() {
        val result = TV_SHOW_DTO.toMedia()

        assertThat(result).isEqualTo(RATED_MEDIA)
    }

    @Test
    fun `should map id correctly`() {
        val result = TV_SHOW_DTO.toMedia()
        assertThat(result.id).isEqualTo(RATED_MEDIA.id)
    }

    @Test
    fun `should map posterPictureURL to posterImageURL correctly`() {
        val result = TV_SHOW_DTO.toMedia()
        assertThat(result.posterImageURL).isEqualTo(RATED_MEDIA.posterImageURL)
    }

    @Test
    fun `should set contentType to TV_SHOW`() {
        val result = TV_SHOW_DTO.toMedia()
        assertThat(result.contentType).isEqualTo(RATED_MEDIA.contentType)
    }
} 