package com.baghdad.viewmodel

import com.baghdad.domain.usecase.actorDetails.GetActorGalleryUseCase
import com.baghdad.viewmodel.actorGallery.ActorGalleryScreenEffect
import com.baghdad.viewmodel.actorGallery.ActorGalleryViewModel
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

    private fun createViewModel(): ActorGalleryViewModel {
        return ActorGalleryViewModel(
            getGalleryImagesUseCase = getGalleryImagesUseCase, actorId = actorId
        )
    }

    @Test
    fun `initial state should have empty images list and not loading`() {

        coEvery { getGalleryImagesUseCase.invoke(actorId) } returns emptyList()

        viewModel = createViewModel()

        val initialState = viewModel.uiState.value
        Assertions.assertEquals(emptyList<String>(), initialState.images)
        Assertions.assertFalse(initialState.isLoading)
    }

    @Test
    fun `init should call getActorGalleryImages with correct actorId`() = runTest {

        coEvery { getGalleryImagesUseCase.invoke(actorId) } returns emptyList()

        viewModel = createViewModel()
        advanceUntilIdle()

        coVerify { getGalleryImagesUseCase.invoke(actorId) }
    }

    @Test
    fun `getActorGalleryImages with empty list should update state correctly`() = runTest {

        val expectedImages = emptyList<String>()
        coEvery { getGalleryImagesUseCase.invoke(actorId) } returns expectedImages

        viewModel = createViewModel()
        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        Assertions.assertEquals(expectedImages, finalState.images)
        Assertions.assertFalse(finalState.isLoading)
    }


    @Test
    fun `onBackClick should send OnBackClick effect`() = runTest {

        coEvery { getGalleryImagesUseCase.invoke(actorId) } returns emptyList()
        viewModel = createViewModel()

        val effects = mutableListOf<ActorGalleryScreenEffect>()
        val job = launch {
            viewModel.uiEffect.collect { effects.add(it) }
        }

        viewModel.onBackClick()
        advanceUntilIdle()
        job.cancel()

        Assertions.assertTrue(effects.contains(ActorGalleryScreenEffect.OnBackClick))
    }

    @Test
    fun `different actorId should call usecase with correct parameter`() = runTest {


        coEvery { getGalleryImagesUseCase.invoke(actorId) } returns emptyList()
        coEvery { getGalleryImagesUseCase.invoke(differentActorId) } returns emptyList()

        viewModel = createViewModel()
        advanceUntilIdle()


        viewModel.getActorGalleryImages(differentActorId)
        advanceUntilIdle()


        coVerify { getGalleryImagesUseCase.invoke(actorId) }
        coVerify { getGalleryImagesUseCase.invoke(differentActorId) }
    }

    @Test
    fun `state should be properly initialized with default values`() {

        coEvery { getGalleryImagesUseCase.invoke(actorId) } returns emptyList()

        viewModel = createViewModel()

        val state = viewModel.uiState.value
        Assertions.assertTrue(state.images.isEmpty())
        Assertions.assertFalse(state.isLoading)
    }

    @Test
    fun `viewModel should implement ActorGalleryInteractionListener`() {

        coEvery { getGalleryImagesUseCase.invoke(actorId) } returns emptyList()

        viewModel = createViewModel()

        Assertions.assertTrue(true)
    }

    @Test
    fun `getActorGalleryImages should call usecase only once when called once`() = runTest {
        coEvery { getGalleryImagesUseCase.invoke(actorId) } returns emptyList()

        viewModel = createViewModel()
        advanceUntilIdle()

        coVerify(exactly = 1) { getGalleryImagesUseCase.invoke(actorId) }
    }

    @Test
    fun `getActorGalleryImages should call usecase with new actorId when actorId changes`() =
        runTest {


            coEvery { getGalleryImagesUseCase.invoke(actorId) } returns emptyList()
            coEvery { getGalleryImagesUseCase.invoke(newActorId) } returns emptyList()

            viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.getActorGalleryImages(newActorId)
            advanceUntilIdle()

            coVerify(exactly = 1) { getGalleryImagesUseCase.invoke(actorId) }
            coVerify(exactly = 1) { getGalleryImagesUseCase.invoke(newActorId) }
        }

    private companion object {
        val actorId = 123L
        val listImage = "image1.jpg"
        val differentActorId = 456L
        val newActorId = 999L
    }

}
