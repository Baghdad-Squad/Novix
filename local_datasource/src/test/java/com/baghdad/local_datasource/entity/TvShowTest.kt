//package com.baghdad.local_datasource.entity
//
//import com.baghdad.local_datasource.roomDB.entity.Genre
//import com.baghdad.local_datasource.roomDB.entity.TvShow
//import com.baghdad.local_datasource.roomDB.entity.toDto
//import com.baghdad.local_datasource.roomDB.entity.toDtos
//import com.baghdad.local_datasource.roomDB.entity.toLocalDto
//import com.baghdad.repository.model.RecentlyViewedDto
//import com.google.common.truth.Truth.assertThat
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//
//class TvShowTest {
//
//    @Test
//    fun `should create TvShow with correct fields when all values are provided`() {
//
//        // Then
//        assertThat(TV_SHOW.id).isEqualTo(1L)
//        assertThat(TV_SHOW.title).isEqualTo("IDK")
//        assertThat(TV_SHOW.genres).containsExactly(1L, 2L)
//        assertThat(TV_SHOW.imdbRating).isEqualTo(9.2)
//        assertThat(TV_SHOW.userRating).isEqualTo(8.5)
//        assertThat(TV_SHOW.releaseDate).isEqualTo("2011-04-17")
//        assertThat(TV_SHOW.overview).startsWith("Test overview")
//        assertThat(TV_SHOW.posterPictureURL).isEqualTo("Test posterPictureURL")
//        assertThat(TV_SHOW.numberOfSeasons).isEqualTo(8)
//    }
//
//    @Test
//    fun `should map TvShow to TvShowDto when calling toDto`() {
//        // Given
//        val genres = GENRES.map { it.toDto() }
//
//        // When
//        val dto = TV_SHOW.toDto(genres)
//
//        // Then
//        assertThat(dto.id).isEqualTo(TV_SHOW.id)
//        assertThat(dto.title).isEqualTo(TV_SHOW.title)
//        assertThat(dto.genres).isEqualTo(genres)
//        assertThat(dto.trailerURL).isEmpty()
//    }
//
//    @Test
//    fun `should map TvShowDto back to TvShow when calling toLocalDto`() {
//        // Given
//        val dto = TV_SHOW.toDto(GENRES.map { it.toDto() })
//
//        // When
//        val entity = dto.toLocalDto()
//
//        // Then
//        assertThat(entity).isEqualTo(TV_SHOW)
//    }
//
//    @Test
//    fun `should map list of TvShow to list of TvShowDto when genres are provided`() {
//        // Given
//        val tvShows = listOf(
//            TV_SHOW,
//            TV_SHOW.copy(id = 2L),
//            TV_SHOW.copy(id = 3L),
//            TV_SHOW.copy(id = 4L)
//        )
//        val genresMap = GENRES.associate { it.id to it }
//
//        // When
//        val dtos = tvShows.toDtos(genresMap)
//
//        // Then
//        assertThat(dtos).hasSize(tvShows.size)
//        assertThat(dtos).isEqualTo(tvShows.map { it.toDto(GENRES.map { it.toDto() }) })
//    }
//
//    @Test
//    fun `should handle missing genres gracefully when some genreIds are not in the map`() {
//        // Given
//        val tvShowWithMissingGenre = TV_SHOW.copy(genres = listOf(1L, 999L))
//        val genresMap = mapOf(1L to GENRES[0]) // 999L not provided
//
//        // When
//        val dtos = listOf(tvShowWithMissingGenre).toDtos(genresMap)
//
//        // Then
//        assertThat(dtos[0].genres).containsExactly(GENRES[0].toDto())
//    }
//
//    @Test
//    fun `should throw IllegalArgumentException when genre type is invalid`() {
//        // Given
//        val invalidGenre = GENRES[0].copy(type = "INVALID")
//
//        // When / Then
//        assertThrows<IllegalArgumentException> {
//            TV_SHOW.toDto(listOf(invalidGenre.toDto()))
//        }
//    }
//
//    companion object {
//        val TV_SHOW = TvShow(
//            id = 1L,
//            title = "IDK",
//            genres = listOf(1L, 2L),
//            imdbRating = 9.2,
//            userRating = 8.5,
//            releaseDate = "2011-04-17",
//            overview = "Test overview",
//            posterPictureURL = "Test posterPictureURL",
//            numberOfSeasons = 8
//        )
//
//        val GENRES = listOf(
//            Genre(
//                id = 1L,
//                name = "Action",
//                type = RecentlyViewedDto.ContentType.TV_SHOW.name
//            ),
//            Genre(
//                id = 2L,
//                name = "Adventure",
//                type = RecentlyViewedDto.ContentType.TV_SHOW.name
//            )
//        )
//    }
//}
