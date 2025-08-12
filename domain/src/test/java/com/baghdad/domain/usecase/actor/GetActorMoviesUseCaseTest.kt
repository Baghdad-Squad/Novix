package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.domain.testHelper.getMinimalSavedMovie
import com.baghdad.domain.testHelper.getSampleSavedMovie
import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorMoviesUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var getActorMoviesUseCase: GetActorMoviesUseCase
    val sampleSavedMovie = getSampleSavedMovie()
    val minimalSavedMovie = getMinimalSavedMovie()


    @BeforeEach
    fun setUp() {
        actorRepository = mockk(relaxed = true)
        getActorMoviesUseCase = GetActorMoviesUseCase(actorRepository)
    }

    @Test
    fun `getActorMoviesUseCase() should return actor movies when called with valid actorId`() =
        runTest {
            coEvery { actorRepository.getActorMovies(ACTOR_ID) } returns listOf(sampleSavedMovie)

            val result = getActorMoviesUseCase(ACTOR_ID)

            assertThat(result).isEqualTo(listOf(sampleSavedMovie))
        }

    @Test
    fun `getActorMoviesUseCase() should return empty list when no movies found for actor`() =
        runTest {
            coEvery { actorRepository.getActorMovies(ACTOR_ID) } returns emptyList()

            val result = getActorMoviesUseCase(ACTOR_ID)

            assertThat(result).isEmpty()
        }

    @Test
    fun `getActorMoviesUseCase() should return movies with minimal fields when data is sparse`() =
        runTest {
            coEvery { actorRepository.getActorMovies(ACTOR_ID) } returns listOf(minimalSavedMovie)

            val result = getActorMoviesUseCase(ACTOR_ID)

            assertThat(result[0].movie.title).isEqualTo("Minimal Movie")
            assertThat(result[0].movie.genres).isEmpty()
            assertThat(result[0].movie.posterImageURL).isEmpty()
        }

    @Test
    fun `getActorMoviesUseCase() should return movies with special characters in titles when titles contain unicode chars`() =
        runTest {
            val specialTitleMovie = listOf(
                sampleSavedMovie.copy(
                    movie = sampleSavedMovie.movie.copy(title = MOVIE_TITLE_WITH_SPECIAL_CHARACTERS)
                )
            )

            coEvery { actorRepository.getActorMovies(ACTOR_ID) } returns specialTitleMovie

            val result = getActorMoviesUseCase(ACTOR_ID)

            assertThat(result[0].movie.title).isEqualTo(MOVIE_TITLE_WITH_SPECIAL_CHARACTERS)
        }

    @Test
    fun `getActorMoviesUseCase() should return movies with multiple genres when movie has many genres`() =
        runTest {
            val multiGenreMovie = listOf(
                sampleSavedMovie.copy(
                    movie = sampleSavedMovie.movie.copy(
                        genres = listOf(
                            Genre(id = 1, name = "Drama"),
                            Genre(id = 2, name = "Thriller"),
                            Genre(id = 3, name = "Mystery")
                        )
                    )
                )
            )

            coEvery { actorRepository.getActorMovies(ACTOR_ID) } returns multiGenreMovie

            val result = getActorMoviesUseCase(ACTOR_ID)

            assertThat(result[0].movie.genres).hasSize(3)
        }

    companion object {
        private const val ACTOR_ID = 1L
        private const val MOVIE_TITLE_WITH_SPECIAL_CHARACTERS = "The Art of War: 戦争の藝術"
    }
}
