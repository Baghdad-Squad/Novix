package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorMoviesUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var getActorMoviesUseCase: GetActorMoviesUseCase

    private val sampleMovies = listOf(
        Movie(
            id = 101,
            title = "Example Movie",
            genres = listOf(
                Genre(id = 1, name = "Drama"),
                Genre(id = 2, name = "Thriller")
            ),
            averageRating = 9.5,
            userRating = 9.4,
            releaseDate = LocalDate(2023, 10, 1),
            overview = "A gripping drama that explores the depths of human emotion.",
            posterImageURL = "https://example.com/poster1.jpg",
            trailerURL = "https://example.com/trailer.mp4",
            runtimeMinutes = 120
        ),
        Movie(
            id = 102,
            title = "Another Movie",
            genres = listOf(Genre(id = 3, name = "Action")),
            averageRating = 8.0,
            userRating = 7.5,
            releaseDate = LocalDate(2022, 5, 15),
            overview = "An action-packed adventure.",
            posterImageURL = "https://example.com/poster2.jpg",
            trailerURL = "https://example.com/trailer2.mp4",
            runtimeMinutes = 150
        )
    )

    private val minimalMovie = Movie(
        id = 103,
        title = "Minimal Movie",
        genres = emptyList(),
        averageRating = 0.0,
        userRating = null,
        releaseDate = LocalDate(2021, 1, 1),
        overview = "",
        posterImageURL = "",
        trailerURL = "",
        runtimeMinutes = 0
    )

    @BeforeEach
    fun setUp() {
        actorRepository = mockk(relaxed = true)
        getActorMoviesUseCase = GetActorMoviesUseCase(actorRepository)
    }

    @Test
    fun `getActorMoviesUseCase should return actor movies`() = runTest {
        // Given
        val actorId = 1L
        coEvery { actorRepository.getActorMovies(actorId) } returns sampleMovies

        // When
        val result = getActorMoviesUseCase(actorId)

        // Then
        assertThat(result).isEqualTo(sampleMovies)
    }

    @Test
    fun `getActorMoviesUseCase returns empty list when no movies found`() = runTest {
        // Given
        val actorId = 2L
        coEvery { actorRepository.getActorMovies(actorId) } returns emptyList()

        // When
        val result = getActorMoviesUseCase(actorId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getActorMoviesUseCase returns movies with minimal fields`() = runTest {
        // Given
        val actorId = 3L
        coEvery { actorRepository.getActorMovies(actorId) } returns listOf(minimalMovie)

        // When
        val result = getActorMoviesUseCase(actorId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Minimal Movie")
        assertThat(result[0].genres).isEmpty()
        assertThat(result[0].posterImageURL).isEmpty()
    }

    @Test
    fun `getActorMoviesUseCase returns movies with special characters in titles`() = runTest {
        // Given
        val actorId = 6L
        val specialTitleMovie = listOf(
            sampleMovies[0].copy(title = "The Art of War: 戦争の藝術")
        )
        coEvery { actorRepository.getActorMovies(actorId) } returns specialTitleMovie

        // When
        val result = getActorMoviesUseCase(actorId)

        // Then
        assertThat(result[0].title).isEqualTo("The Art of War: 戦争の藝術")
    }

    @Test
    fun `getActorMoviesUseCase makes exactly one repository call`() = runTest {
        // Given
        val actorId = 7L
        coEvery { actorRepository.getActorMovies(actorId) } returns sampleMovies

        // When
        getActorMoviesUseCase(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorMovies(actorId) }
    }


    @Test
    fun `getActorMoviesUseCase returns movies with multiple genres`() = runTest {
        // Given
        val actorId = 9L
        val multiGenreMovie = listOf(
            sampleMovies[0].copy(
                genres = listOf(
                    Genre(id = 1, name = "Drama"),
                    Genre(id = 2, name = "Thriller"),
                    Genre(id = 3, name = "Mystery")
                )
            )
        )
        coEvery { actorRepository.getActorMovies(actorId) } returns multiGenreMovie

        // When
        val result = getActorMoviesUseCase(actorId)

        // Then
        assertThat(result[0].genres).hasSize(3)
    }
}