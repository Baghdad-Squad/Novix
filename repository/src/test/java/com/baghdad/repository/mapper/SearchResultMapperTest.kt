package com.baghdad.repository.mapper

import com.baghdad.repository.model.SearchResultDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SearchResultMapperTest {

    @Test
    fun `should map correctly to entity when SearchResultDto contains valid data`() {
        // Given
        val searchResultDto = createMockSearchResultDto()

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.actors.size).isEqualTo(2)
        assertThat(result.actors[0].id).isEqualTo(789L)
        assertThat(result.actors[0].name).isEqualTo("Test Actor 1")
        assertThat(result.actors[1].id).isEqualTo(790L)
        assertThat(result.actors[1].name).isEqualTo("Test Actor 2")

        assertThat(result.movies.size).isEqualTo(2)
        assertThat(result.movies[0].id).isEqualTo(456L)
        assertThat(result.movies[0].title).isEqualTo("Test Movie 1")
        assertThat(result.movies[1].id).isEqualTo(457L)
        assertThat(result.movies[1].title).isEqualTo("Test Movie 2")

        assertThat(result.tvShows.size).isEqualTo(2)
        assertThat(result.tvShows[0].id).isEqualTo(123L)
        assertThat(result.tvShows[0].title).isEqualTo("Test TV Show 1")
        assertThat(result.tvShows[1].id).isEqualTo(124L)
        assertThat(result.tvShows[1].title).isEqualTo("Test TV Show 2")
    }

    @Test
    fun `should map to entity with empty actors list when SearchResultDto has no actors`() {
        // Given
        val searchResultDto = createMockSearchResultDto().copy(actors = emptyList())

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.actors).isEmpty()
        assertThat(result.movies.size).isEqualTo(2)
        assertThat(result.tvShows.size).isEqualTo(2)
    }

    companion object {
        private fun createMockSearchResultDto() = SearchResultDto(
            actors = listOf(
                createMockActorDto(789L, "Test Actor 1"),
                createMockActorDto(790L, "Test Actor 2")
            ),
            movies = listOf(
                createMockMovieDto(456L, "Test Movie 1"),
                createMockMovieDto(457L, "Test Movie 2")
            ),
            tvShows = listOf(
                createMockTvShowDto(123L, "Test TV Show 1"),
                createMockTvShowDto(124L, "Test TV Show 2")
            )
        )

        private fun createMockActorDto(id: Long, name: String) =
            com.baghdad.repository.model.ActorDto(
                id = id,
                name = name,
                imageUrl = "/actor_profile.jpg",
                biography = "Test actor biography",
                birthdayDate = "1985-03-10",
                deathDate = null,
                placeOfBirth = "Los Angeles, USA",
                headerPictures = listOf("/actor_header1.jpg", "/actor_header2.jpg"),
                department = "Acting"
            )

        private fun createMockMovieDto(id: Long, title: String) =
            com.baghdad.repository.model.MovieDto(
                id = id,
                title = title,
                genres = listOf(createMockGenreDto(28L, "Action")),
                imdbRating = 8.0,
                userRating = 7.5,
                releaseDate = "2023-01-01",
                overview = "Test movie overview",
                posterPictureURL = "/movie_poster.jpg",
                runtimeMinutes = 120,
                trailerURL = " "
            )

        private fun createMockTvShowDto(id: Long, title: String) =
            com.baghdad.repository.model.TvShowDto(
                id = id,
                title = title,
                genres = listOf(createMockGenreDto(35L, "Comedy")),
                imdbRating = 8.5,
                userRating = 8,
                releaseDate = "2023-01-01",
                overview = "Test TV show overview",
                posterPictureURL = "/tv_show_poster.jpg",
                headerImagesURLs = listOf("/header1.jpg", "/header2.jpg"),
                trailerURL = " ",
                numberOfSeasons = 3
            )

        private fun createMockGenreDto(id: Long, name: String) =
            com.baghdad.repository.model.GenreDto(
                id = id,
                name = name,
                type = com.baghdad.repository.model.GenreDto.GenreType.MOVIE
            )
    }
} 