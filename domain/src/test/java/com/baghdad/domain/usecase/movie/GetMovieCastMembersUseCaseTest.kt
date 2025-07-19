package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
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
    fun `should get movies detail from when function run successfully`() = runTest {
        val movieId = 1L
        coEvery { movieRepository.getMovieCastMembers(movieId) } returns castMembers
        val result = getMovieCastMembersUseCase.invoke(movieId)
        assertThat(result).isEqualTo(castMembers)
    }


    @Test
    fun `should return cast with valid but minimal actor data`() = runTest {
        val movieId = 2L
        val minimalCast = listOf(
            CastMember(
                actor = Actor(
                    id = 104L,
                    name = "Unknown Actor",
                    profilePictureURL = "",
                    birthDate = LocalDate(2000, 1, 1),
                    placeOfBirth = "",
                    deathDate = null,
                    biography = "",
                    headerPictures = emptyList(),
                    department = "Unknown"
                ),
                characterName = "Background Character"
            )
        )

        coEvery { movieRepository.getMovieCastMembers(movieId) } returns minimalCast

        val result = getMovieCastMembersUseCase.invoke(movieId)

        assertThat(result).isEqualTo(minimalCast)
    }

    private companion object {
        val minimalCast = listOf(
            CastMember(
                actor = Actor(
                    id = 104L,
                    name = "Unknown Actor",
                    profilePictureURL = "",
                    birthDate = LocalDate(2000, 1, 1),
                    placeOfBirth = "",
                    deathDate = null,
                    biography = "",
                    headerPictures = emptyList(),
                    department = "Unknown"
                ),
                characterName = "Background Character"
            )
        )
        val castMembers = listOf(
            CastMember(
                actor = Actor(
                    id = 101L,
                    name = "Leonardo DiCaprio",
                    profilePictureURL = "https://example.com/images/leonardo.jpg",
                    birthDate = LocalDate(1974, 11, 11),
                    placeOfBirth = "Los Angeles, California, USA",
                    deathDate = null,
                    biography = "An American actor known for roles in Titanic, Inception, and The Revenant.",
                    headerPictures = listOf(
                        "https://example.com/images/leonardo-header1.jpg",
                        "https://example.com/images/leonardo-header2.jpg"
                    ),
                    department = "Acting"
                ),
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

    }
}