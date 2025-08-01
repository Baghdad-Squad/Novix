package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.person.Actor
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorInfoUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var getActorInfoUseCase: GetActorInfoUseCase

    @BeforeEach
    fun setUp() {
        actorRepository = mockk(relaxed = true)
        getActorInfoUseCase = GetActorInfoUseCase(actorRepository)
    }

    private val sampleActor = Actor(
        id = 1L,
        name = "John Doe",
        biography = "An actor",
        profilePictureURL = "https://example.com/profile.jpg",
        birthDate = kotlinx.datetime.LocalDate(1980, 1, 1),
        placeOfBirth = "New York",
        deathDate = null,
        headerPictures = listOf(
            "https://example.com/header.jpg",
            "https://example.com/header2.jpg"
        ),
        department = "Acting"
    )

    private val minimalActor = Actor(
        id = 2L,
        name = "Jane Smith",
        biography = "",
        profilePictureURL = "",
        birthDate = kotlinx.datetime.LocalDate(1990, 5, 20),
        placeOfBirth = "",
        deathDate = null,
        headerPictures = emptyList(),
        department = ""
    )

    @Test
    fun `getActorInfoUseCase() should return actor info when called with valid id`() = runTest {
        // Given
        val actorId = 1L
        val expectedActor = Actor(
            id = actorId, name = "John Doe",
            biography = "An actor",
            profilePictureURL = "https://example.com/profile.jpg",
            birthDate = kotlinx.datetime.LocalDate(1980, 1, 1),
            placeOfBirth = "New York",
            deathDate = null,
            headerPictures = listOf(
                "https://example.com/header.jpg",
                "https://example.com/header2.jpg"
            ),
            department = "Acting",
        )

        coEvery { actorRepository.getActorInfo(actorId) } returns expectedActor

        // When
        val result = getActorInfoUseCase(actorId)

        // Then
        assertThat(result).isEqualTo(expectedActor)
    }

    // Edge cases
    @Test
    fun `getActorInfoUseCase() should return actor with minimal fields when data is sparse`() = runTest {
        // Given
        val actorId = 2L
        coEvery { actorRepository.getActorInfo(actorId) } returns minimalActor

        // When
        val result = getActorInfoUseCase(actorId)

        // Then
        assertThat(result).isEqualTo(minimalActor)
        assertThat(result.biography).isEmpty()
        assertThat(result.headerPictures).isEmpty()
    }

    @Test
    fun `getActorInfoUseCase() should return actor with multiple header pictures when available`() = runTest {
        // Given
        val actorId = 3L
        val actorWithManyHeaders = sampleActor.copy(
            headerPictures = List(10) { "https://example.com/header$it.jpg" }
        )
        coEvery { actorRepository.getActorInfo(actorId) } returns actorWithManyHeaders

        // When
        val result = getActorInfoUseCase(actorId)

        // Then
        assertThat(result.headerPictures).hasSize(10)
    }

    @Test
    fun `getActorInfoUseCase() should return actor with birth date when place of birth is missing`() = runTest {
        // Given
        val actorId = 3L
        val actorWithBirthDateOnly = minimalActor.copy(
            birthDate = kotlinx.datetime.LocalDate(1985, 10, 15)
        )
        coEvery { actorRepository.getActorInfo(actorId) } returns actorWithBirthDateOnly

        // When
        val result = getActorInfoUseCase(actorId)

        // Then
        assertThat(result.birthDate).isNotNull()
        assertThat(result.placeOfBirth).isEmpty()
    }

    @Test
    fun `getActorInfoUseCase() should return actor with department when biography is missing`() = runTest {
        // Given
        val actorId = 4L
        val actorWithDepartmentOnly = minimalActor.copy(
            department = "Production"
        )
        coEvery { actorRepository.getActorInfo(actorId) } returns actorWithDepartmentOnly

        // When
        val result = getActorInfoUseCase(actorId)

        // Then
        assertThat(result.department).isEqualTo("Production")
        assertThat(result.biography).isEmpty()
    }

    @Test
    fun `getActorInfoUseCase() should return actor with profile picture when header pictures are missing`() =
        runTest {
            // Given
            val actorId = 5L
            val actorWithProfileOnly = minimalActor.copy(
                profilePictureURL = "https://example.com/profile2.jpg"
            )
            coEvery { actorRepository.getActorInfo(actorId) } returns actorWithProfileOnly

            // When
            val result = getActorInfoUseCase(actorId)

            // Then
            assertThat(result.profilePictureURL).isNotEmpty()
            assertThat(result.headerPictures).isEmpty()
        }

    @Test
    fun `getActorInfoUseCase() should make exactly one repository call when invoked`() = runTest {
        // Given
        val actorId = 6L
        coEvery { actorRepository.getActorInfo(actorId) } returns sampleActor

        // When
        getActorInfoUseCase(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorInfo(actorId) }
    }
}