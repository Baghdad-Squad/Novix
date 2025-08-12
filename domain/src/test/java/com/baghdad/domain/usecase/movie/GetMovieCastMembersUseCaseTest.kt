package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.testHelper.getMinimalActor
import com.baghdad.domain.testHelper.getSampleActor
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMovieCastMembersUseCaseTest {
    lateinit var getMovieCastMembersUseCase: GetMovieCastMembersUseCase
    lateinit var movieRepository: MovieRepository

    @BeforeEach
    fun setup() {
        movieRepository = mockk()
        getMovieCastMembersUseCase = GetMovieCastMembersUseCase(movieRepository)
    }

    @Test
    fun `getMovieCastMembersUseCase() should return cast members when repository returns data`() =
        runTest {
            coEvery { movieRepository.getMovieCastMembers(MOVIE_ID_1) } returns CAST_MEMBERS

            val result = getMovieCastMembersUseCase.invoke(MOVIE_ID_1)

            assertThat(result).isEqualTo(CAST_MEMBERS)
        }

    @Test
    fun `getMovieCastMembersUseCase() should return cast with minimal actor data when repository returns minimal data`() =
        runTest {
            coEvery { movieRepository.getMovieCastMembers(MOVIE_ID_2) } returns MINIMAL_CAST

            val result = getMovieCastMembersUseCase.invoke(MOVIE_ID_2)

            assertThat(result).isEqualTo(MINIMAL_CAST)
        }

    private companion object {
        private const val MOVIE_ID_1 = 1L
        private const val MOVIE_ID_2 = 2L

        private val CAST_MEMBERS = listOf(
            CastMember(
                actor = getSampleActor(),
                characterName = "Cobb"
            ),
            CastMember(
                actor = Actor(
                    id = 102L,
                    name = "Joseph Gordon-Levitt",
                    profilePictureURL = "https://example.com/images/joseph.jpg",
                    birthDate = LocalDate(1981, 2, 17),
                    placeOfBirth = "Los Angeles, California, USA",
                    deathDate = null,
                    biography = "Known for his performances in Inception, 500 Days of Summer, and Looper.",
                    headerPictures = listOf(
                        "https://example.com/images/joseph-header1.jpg"
                    ),
                    department = "Acting"
                ),
                characterName = "Arthur"
            ),
            CastMember(
                actor = Actor(
                    id = 103L,
                    name = "Elliot Page",
                    profilePictureURL = "https://example.com/images/elliot.jpg",
                    birthDate = LocalDate(1987, 2, 21),
                    placeOfBirth = "Halifax, Nova Scotia, Canada",
                    deathDate = null,
                    biography = "Elliot Page is an actor and producer known for Juno, Inception, and Umbrella Academy.",
                    headerPictures = listOf(),
                    department = "Acting"
                ),
                characterName = "Ariadne"
            )
        )

        private val MINIMAL_CAST = listOf(
            CastMember(actor = getMinimalActor(), characterName = "Background Character")
        )
    }
}
