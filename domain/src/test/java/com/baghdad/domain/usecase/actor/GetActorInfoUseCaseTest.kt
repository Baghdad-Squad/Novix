package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.domain.testHelper.getMinimalActor
import com.baghdad.domain.testHelper.getSampleActor
import com.baghdad.entity.person.Actor
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorInfoUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var getActorInfoUseCase: GetActorInfoUseCase
    val sampleActor = getSampleActor()
    val minimalActor = getMinimalActor()

    @BeforeEach
    fun setUp() {
        actorRepository = mockk(relaxed = true)
        getActorInfoUseCase = GetActorInfoUseCase(actorRepository)
    }

    @Test
    fun `getActorInfoUseCase() should return actor info when called with valid id`() = runTest {
        coEvery { actorRepository.getActorDetails(ACTOR_ID_1) } returns sampleActor

        val result = getActorInfoUseCase(ACTOR_ID_1)

        assertThat(result).isEqualTo(sampleActor)
    }

    @Test
    fun `should return minimal actor when data is sparse`() = runTest {
        coEvery { actorRepository.getActorDetails(ACTOR_ID_2) } returns minimalActor

        val result = getActorInfoUseCase(ACTOR_ID_2)

        assertThat(result).isEqualTo(minimalActor)
    }

    @Test
    fun `should have empty biography and header pictures when actor data is sparse`() = runTest {
        coEvery { actorRepository.getActorDetails(ACTOR_ID_2) } returns minimalActor

        val result = getActorInfoUseCase(ACTOR_ID_2)

        assertThat(result.biography).isEmpty()
        assertThat(result.headerPictures).isEmpty()
    }


    @Test
    fun `should return actor with multiple header pictures when available`() =
        runTest {
            val actorWithManyHeaders = sampleActor.copy(
                headerPictures = List(10) { "https://example.com/header$it.jpg" }
            )

            coEvery { actorRepository.getActorDetails(ACTOR_ID_3) } returns actorWithManyHeaders

            val result = getActorInfoUseCase(ACTOR_ID_3)

            assertThat(result.headerPictures).hasSize(10)
        }

    @Test
    fun `should return actor with birth date when place of birth is missing`() =
        runTest {
            val actorWithBirthDateOnly = minimalActor.copy(
                birthDate = LocalDate(1985, 10, 15)
            )

            coEvery { actorRepository.getActorDetails(ACTOR_ID_3) } returns actorWithBirthDateOnly

            val result = getActorInfoUseCase(ACTOR_ID_3)

            assertThat(result.birthDate).isNotNull()
        }

    @Test
    fun `should return actor with department when biography is missing`() =
        runTest {
            val actorWithDepartmentOnly = minimalActor.copy(
                department = "Production"
            )

            coEvery { actorRepository.getActorDetails(ACTOR_ID_4) } returns actorWithDepartmentOnly

            val result = getActorInfoUseCase(ACTOR_ID_4)

            assertThat(result.department).isEqualTo("Production")
        }

    @Test
    fun `should return actor with profile picture when header pictures are missing`() =
        runTest {
            val actorWithProfileOnly = minimalActor.copy(
                profilePictureURL = "https://example.com/profile2.jpg"
            )

            coEvery { actorRepository.getActorDetails(ACTOR_ID_5) } returns actorWithProfileOnly

            val result = getActorInfoUseCase(ACTOR_ID_5)

            assertThat(result.profilePictureURL).isNotEmpty()
        }

    @Test
    fun `getActorInfoUseCase() should make exactly one repository call when invoked`() = runTest {
        val sampleActor = getSampleActor()
        coEvery { actorRepository.getActorDetails(ACTOR_ID_6) } returns sampleActor

        getActorInfoUseCase(ACTOR_ID_6)

        coVerify(exactly = 1) { actorRepository.getActorDetails(ACTOR_ID_6) }
    }

    companion object {
        private const val ACTOR_ID_1 = 1L
        private const val ACTOR_ID_2 = 2L
        private const val ACTOR_ID_3 = 3L
        private const val ACTOR_ID_4 = 4L
        private const val ACTOR_ID_5 = 5L
        private const val ACTOR_ID_6 = 6L
    }
}