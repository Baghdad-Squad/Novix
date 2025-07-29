package com.baghdad.domain.usecase.actor

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.person.Actor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTrendingActorsUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var useCase: GetTrendingActorsUseCase

    @BeforeEach
    fun setUp() {
        actorRepository = mockk()
        useCase = GetTrendingActorsUseCase(actorRepository)
    }

    @Test
    fun `invoke should return paged result from repository`() = runTest {
        // Given
        val page = 1
        val expectedActors = listOf(
            Actor(
                id = 101,
                name = "Keanu Reeves",
                profilePictureURL = "https://image.tmdb.org/t/p/w500/keanu.jpg",
                birthDate = LocalDate.parse("1984-11-22"),
                placeOfBirth = "Beirut, Lebanon",
                deathDate = null,
                biography = "Keanu Charles Reeves is a Canadian actor...",
                headerPictures = listOf(
                    "https://image.tmdb.org/t/p/w500/keanu1.jpg",
                    "https://image.tmdb.org/t/p/w500/keanu2.jpg"
                ),
                department = "Acting"
            ),
            Actor(
                id = 102,
                name = "Scarlett Johansson",
                profilePictureURL = "https://image.tmdb.org/t/p/w500/scarlett.jpg",
                birthDate = LocalDate.parse("1984-11-22"),
                placeOfBirth = "New York City, USA",
                deathDate = null,
                biography = "Scarlett Ingrid Johansson is an American actress...",
                headerPictures = listOf(
                    "https://image.tmdb.org/t/p/w500/scarlett1.jpg",
                    "https://image.tmdb.org/t/p/w500/scarlett2.jpg"
                ),
                department = "Acting"
            )
        )

        val expectedResult = PagedResult(
            data = expectedActors,
            nextKey = 2,
            prevKey = null
        )

        coEvery { actorRepository.getTrendingActors(page) } returns expectedResult

        // When
        val result = useCase(page)

        // Then
        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { actorRepository.getTrendingActors(page) }
    }
}