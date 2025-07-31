package com.baghdad.repository.mapper

import com.baghdad.repository.model.SearchResultDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SearchResultMapperTest {

    @Test
    fun `SearchResultDto toEntity should map correctly with valid data`() {
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
    fun `SearchResultDto toEntity should handle empty actors list`() {
        // Given
        val searchResultDto = createMockSearchResultDto().copy(actors = emptyList())

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.actors).isEmpty()
        assertThat(result.movies.size).isEqualTo(2)
        assertThat(result.tvShows.size).isEqualTo(2)
    }

    @Test
    fun `SearchResultDto toEntity should handle empty movies list`() {
        // Given
        val searchResultDto = createMockSearchResultDto().copy(movies = emptyList())

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.actors.size).isEqualTo(2)
        assertThat(result.movies).isEmpty()
        assertThat(result.tvShows.size).isEqualTo(2)
    }

    @Test
    fun `SearchResultDto toEntity should handle empty tvShows list`() {
        // Given
        val searchResultDto = createMockSearchResultDto().copy(tvShows = emptyList())

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.actors.size).isEqualTo(2)
        assertThat(result.movies.size).isEqualTo(2)
        assertThat(result.tvShows).isEmpty()
    }

    @Test
    fun `SearchResultDto toEntity should handle all empty lists`() {
        // Given
        val searchResultDto = createMockSearchResultDto().copy(
            actors = emptyList(),
            movies = emptyList(),
            tvShows = emptyList()
        )

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.actors).isEmpty()
        assertThat(result.movies).isEmpty()
        assertThat(result.tvShows).isEmpty()
    }

    @Test
    fun `SearchResultDto toEntity should handle single actor`() {
        // Given
        val searchResultDto = createMockSearchResultDto().copy(
            actors = listOf(createMockActorDto(789L, "Single Actor"))
        )

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.actors.size).isEqualTo(1)
        assertThat(result.actors[0].id).isEqualTo(789L)
        assertThat(result.actors[0].name).isEqualTo("Single Actor")
    }

    @Test
    fun `SearchResultDto toEntity should handle single movie`() {
        // Given
        val searchResultDto = createMockSearchResultDto().copy(
            movies = listOf(createMockMovieDto(456L, "Single Movie"))
        )

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.movies.size).isEqualTo(1)
        assertThat(result.movies[0].id).isEqualTo(456L)
        assertThat(result.movies[0].title).isEqualTo("Single Movie")
    }

    @Test
    fun `SearchResultDto toEntity should handle single tvShow`() {
        // Given
        val searchResultDto = createMockSearchResultDto().copy(
            tvShows = listOf(createMockTvShowDto(123L, "Single TV Show"))
        )

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.tvShows.size).isEqualTo(1)
        assertThat(result.tvShows[0].id).isEqualTo(123L)
        assertThat(result.tvShows[0].title).isEqualTo("Single TV Show")
    }

    @Test
    fun `SearchResultDto toEntity should handle multiple actors`() {
        // Given
        val searchResultDto = createMockSearchResultDto().copy(
            actors = listOf(
                createMockActorDto(1L, "Actor 1"),
                createMockActorDto(2L, "Actor 2"),
                createMockActorDto(3L, "Actor 3"),
                createMockActorDto(4L, "Actor 4"),
                createMockActorDto(5L, "Actor 5")
            )
        )

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.actors.size).isEqualTo(5)
        assertThat(result.actors[0].id).isEqualTo(1L)
        assertThat(result.actors[0].name).isEqualTo("Actor 1")
        assertThat(result.actors[1].id).isEqualTo(2L)
        assertThat(result.actors[1].name).isEqualTo("Actor 2")
        assertThat(result.actors[2].id).isEqualTo(3L)
        assertThat(result.actors[2].name).isEqualTo("Actor 3")
        assertThat(result.actors[3].id).isEqualTo(4L)
        assertThat(result.actors[3].name).isEqualTo("Actor 4")
        assertThat(result.actors[4].id).isEqualTo(5L)
        assertThat(result.actors[4].name).isEqualTo("Actor 5")
    }

    @Test
    fun `SearchResultDto toEntity should handle multiple movies`() {
        // Given
        val searchResultDto = createMockSearchResultDto().copy(
            movies = listOf(
                createMockMovieDto(1L, "Movie 1"),
                createMockMovieDto(2L, "Movie 2"),
                createMockMovieDto(3L, "Movie 3")
            )
        )

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.movies.size).isEqualTo(3)
        assertThat(result.movies[0].id).isEqualTo(1L)
        assertThat(result.movies[0].title).isEqualTo("Movie 1")
        assertThat(result.movies[1].id).isEqualTo(2L)
        assertThat(result.movies[1].title).isEqualTo("Movie 2")
        assertThat(result.movies[2].id).isEqualTo(3L)
        assertThat(result.movies[2].title).isEqualTo("Movie 3")
    }

    @Test
    fun `SearchResultDto toEntity should handle multiple tvShows`() {
        // Given
        val searchResultDto = createMockSearchResultDto().copy(
            tvShows = listOf(
                createMockTvShowDto(1L, "TV Show 1"),
                createMockTvShowDto(2L, "TV Show 2"),
                createMockTvShowDto(3L, "TV Show 3"),
                createMockTvShowDto(4L, "TV Show 4")
            )
        )

        // When
        val result = searchResultDto.toEntity()

        // Then
        assertThat(result.tvShows.size).isEqualTo(4)
        assertThat(result.tvShows[0].id).isEqualTo(1L)
        assertThat(result.tvShows[0].title).isEqualTo("TV Show 1")
        assertThat(result.tvShows[1].id).isEqualTo(2L)
        assertThat(result.tvShows[1].title).isEqualTo("TV Show 2")
        assertThat(result.tvShows[2].id).isEqualTo(3L)
        assertThat(result.tvShows[2].title).isEqualTo("TV Show 3")
        assertThat(result.tvShows[3].id).isEqualTo(4L)
        assertThat(result.tvShows[3].title).isEqualTo("TV Show 4")
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
                userRating = 8.0,
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