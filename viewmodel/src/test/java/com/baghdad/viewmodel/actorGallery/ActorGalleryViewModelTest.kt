package com.baghdad.viewmodel.actorGallery

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.viewmodel.dummyData.DummyDataFactory.createMockGallery
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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
        coEvery { getGalleryImagesUseCase.invoke(any()) } returns createMockGallery()
        viewModel = ActorGalleryViewModel(
            getGalleryImagesUseCase = getGalleryImagesUseCase,
            actorId = ACTOR_ID,
            defaultDispatcher = testDispatcher
        )
    }

    @Test
    fun `onBackClick should take me to the passed screen when it is clicked`() = runTest {
        //Given
        coEvery { getGalleryImagesUseCase.invoke(ACTOR_ID) } returns emptyList()
        val effects = mutableListOf<ActorGalleryScreenEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }
        //When
        viewModel.onBackClick()
        advanceUntilIdle()
        job.cancel()
        //Then
        assertThat(effects.contains(ActorGalleryScreenEffect.OnBackClick)).isTrue()
    }

    @Test
    fun `onSnackBarActionLabelClick should load data when it is clicked`() = runTest {
        // Given
        val expectedImages = createMockGallery()
        coEvery { getGalleryImagesUseCase.invoke(ACTOR_ID) } returns createMockGallery()
        val effects = mutableListOf<ActorGalleryScreenEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }
        // When
        viewModel.onSnackBarActionLabelClick()
        advanceUntilIdle()
        job.cancel()
        // Then
        val actualState = viewModel.uiState.value
        assertThat(actualState.images).isEqualTo(expectedImages)
        assertThat(actualState.isLoading).isFalse()
    }

    @Test
    fun `getActorGalleryImages should show no internet snackbar when NoInternetException is thrown`() = runTest {
        // Given
        coEvery { getGalleryImagesUseCase.invoke(ACTOR_ID) } throws NoInternetException()
        val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()

        val job = launch {
            viewModel.snackBarState.collect {
                emittedSnackBarMessages.add(it.message)
            }
        }

        // When
        viewModel.loadData()
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NetworkError)
    }

    private companion object {
        const val ACTOR_ID = 123L
    }
}
