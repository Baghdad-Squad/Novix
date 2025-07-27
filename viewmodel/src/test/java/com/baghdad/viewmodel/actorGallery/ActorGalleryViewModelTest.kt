package com.baghdad.viewmodel.actorGallery

import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ActorGalleryViewModelTest {

    private lateinit var viewModel: ActorGalleryViewModel
    private lateinit var getGalleryImagesUseCase: GetActorGalleryUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getGalleryImagesUseCase = mockk()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(actorId: Long = ACTOR_ID): ActorGalleryViewModel {
        return ActorGalleryViewModel(
            getGalleryImagesUseCase = getGalleryImagesUseCase,
            actorId = actorId
        )
    }


    @Test
    fun `init should call getActorGalleryImages with correct actorId`() = runTest {
        coEvery { getGalleryImagesUseCase.invoke(ACTOR_ID) } returns emptyList()
        viewModel = createViewModel()
        advanceUntilIdle()
        coVerify { getGalleryImagesUseCase.invoke(ACTOR_ID) }
    }


    @Test
    fun `onBackClick should send OnBackClick effect`() = runTest {
        coEvery { getGalleryImagesUseCase.invoke(ACTOR_ID) } returns emptyList()
        viewModel = createViewModel()

        val effects = mutableListOf<ActorGalleryScreenEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        viewModel.onBackClick()
        advanceUntilIdle()
        job.cancel()

        Assertions.assertTrue(effects.contains(ActorGalleryScreenEffect.OnBackClick))
    }

    @Test
    fun `different actorId should call usecase with correct parameter`() = runTest {
        coEvery { getGalleryImagesUseCase.invoke(ACTOR_ID) } returns emptyList()
        coEvery { getGalleryImagesUseCase.invoke(DIFFERENT_ACTOR_ID) } returns emptyList()

        viewModel = createViewModel()
        advanceUntilIdle()

        coVerify(exactly = 1) { getGalleryImagesUseCase.invoke(ACTOR_ID) }

        viewModel.getActorGalleryImages(DIFFERENT_ACTOR_ID)
        advanceUntilIdle()

        coVerify(exactly = 1) { getGalleryImagesUseCase.invoke(DIFFERENT_ACTOR_ID) }
    }

    @Test
    fun `getActorGalleryImages should call usecase only once when called once`() = runTest {
        coEvery { getGalleryImagesUseCase.invoke(ACTOR_ID) } returns emptyList()

        viewModel = createViewModel()
        advanceUntilIdle()

        coVerify(exactly = 1) { getGalleryImagesUseCase.invoke(ACTOR_ID) }
    }

    @Test
    fun `getActorGalleryImages should call usecase with new actorId when actorId changes`() =
        runTest {
            coEvery { getGalleryImagesUseCase.invoke(ACTOR_ID) } returns emptyList()
            coEvery { getGalleryImagesUseCase.invoke(NEW_ACTOR_ID) } returns emptyList()

            viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.getActorGalleryImages(NEW_ACTOR_ID)
            advanceUntilIdle()

            coVerify(exactly = 1) { getGalleryImagesUseCase.invoke(ACTOR_ID) }
            coVerify(exactly = 1) { getGalleryImagesUseCase.invoke(NEW_ACTOR_ID) }
        }


    private companion object {
        const val ACTOR_ID = 123L
        const val DIFFERENT_ACTOR_ID = 456L
        const val NEW_ACTOR_ID = 789L
    }
}
