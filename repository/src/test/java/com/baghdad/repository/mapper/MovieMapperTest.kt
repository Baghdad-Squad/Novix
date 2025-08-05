//package com.baghdad.repository.mapper
//
//import com.baghdad.entity.media.Movie
//import com.baghdad.repository.dummyData.DummyDataFactory.createMockGenre
//import com.baghdad.repository.dummyData.DummyDataFactory.createMockGenreDto
//import com.baghdad.repository.model.GenreDto
//import com.baghdad.repository.model.MovieDto
//import com.baghdad.repository.model.SearchQueryDto
//import com.google.common.truth.Truth.assertThat
//import kotlinx.datetime.LocalDate
//import org.junit.jupiter.api.Test
//
//class MovieMapperTest {
//
//    @Test
//    fun `should map correctly to entity when MovieDto has valid data`() {
//        // Given
//        val movieDto = createMockMovieDto()
//
//        // When
//        val result = movieDto.toEntity()
//
//        // Then
//        assertThat(result.id).isEqualTo(456L)
//        assertThat(result.title).isEqualTo("Test Movie")
//        assertThat(result.genres.size).isEqualTo(1)
//        assertThat(result.genres[0].id).isEqualTo(28L)
//        assertThat(result.genres[0].name).isEqualTo("Action")
//        assertThat(result.averageRating).isEqualTo(8.0)
//        assertThat(result.userRating).isEqualTo(7.5)
//        assertThat(result.releaseDate).isEqualTo(LocalDate.parse("2023-01-01"))
//        assertThat(result.overview).isEqualTo("Test movie overview")
//        assertThat(result.posterImageURL).isEqualTo("/movie_poster.jpg")
//        assertThat(result.trailerURL).isEqualTo(" ")
//        assertThat(result.runtimeMinutes).isEqualTo(120)
//    }
//
//    @Test
//    fun `should keep user rating null when MovieDto has null user rating`() {
//        // Given
//        val movieDto = createMockMovieDto().copy(userRating = null)
//
//        // When
//        val result = movieDto.toEntity()
//
//        // Then
//        assertThat(result.userRating).isNull()
//    }
//
//    @Test
//    fun `MovieDto toSearchQueryDto should map correctly when it is provided`() {
//        // Given
//        val movieDto = createMockMovieDto()
//        val query = "test query"
//
//        // When
//        val result = movieDto.toSearchQueryDto(query)
//
//        // Then
//        assertThat(result.queryName).isEqualTo(query)
//        assertThat(result.mediaId).isEqualTo(movieDto.id)
//        assertThat(result.mediaType).isEqualTo(SearchQueryDto.MediaType.MOVIE)
//    }
//
//    @Test
//    fun `Movie toDto should map correctly when it is provided`() {
//        // Given
//        val movie = createMockMovie()
//
//        // When
//        val result = movie.toDto()
//
//        // Then
//        assertThat(result.id).isEqualTo(456L)
//        assertThat(result.title).isEqualTo("Test Movie")
//        assertThat(result.genres.size).isEqualTo(1)
//        assertThat(result.genres[0].id).isEqualTo(28L)
//        assertThat(result.genres[0].name).isEqualTo("Action")
//        assertThat(result.genres[0].type).isEqualTo(GenreDto.GenreType.MOVIE)
//        assertThat(result.imdbRating).isEqualTo(8.0)
//        assertThat(result.userRating).isEqualTo(7.5)
//        assertThat(result.releaseDate).isEqualTo("2023-01-01")
//        assertThat(result.overview).isEqualTo("Test movie overview")
//        assertThat(result.posterPictureURL).isEqualTo("/movie_poster.jpg")
//        assertThat(result.trailerURL).isEqualTo(" ")
//        assertThat(result.runtimeMinutes).isEqualTo(120)
//    }
//
//    @Test
//    fun `Genre toDto should map correctly`() {
//        // Given
//        val genre = createMockGenre(28L, "Action")
//
//        // When
//        val result = genre.toDto()
//
//        // Then
//        assertThat(result.id).isEqualTo(28L)
//        assertThat(result.name).isEqualTo("Action")
//        assertThat(result.type).isEqualTo(GenreDto.GenreType.MOVIE)
//    }
//
//    companion object {
//        private fun createMockMovieDto() = MovieDto(
//            id = 456L,
//            title = "Test Movie",
//            genres = listOf(createMockGenreDto(28L, "Action")),
//            imdbRating = 8.0,
//            userRating = 7.5,
//            releaseDate = "2023-01-01",
//            overview = "Test movie overview",
//            posterPictureURL = "/movie_poster.jpg",
//            runtimeMinutes = 120,
//            trailerURL = " "
//        )
//
//        private fun createMockMovie() = Movie(
//            id = 456L,
//            title = "Test Movie",
//            genres = listOf(createMockGenre(28L, "Action")),
//            averageRating = 8.0,
//            userRating = 7.5,
//            releaseDate = LocalDate.parse("2023-01-01"),
//            overview = "Test movie overview",
//            posterImageURL = "/movie_poster.jpg",
//            runtimeMinutes = 120,
//            trailerURL = " "
//        )
//    }
//}