package com.baghdad.viewmodel.topTvShowPicks

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopTvShowPicksViewModelTest {
    private lateinit var getActorTvShowUseCase: GetActorTvShowUseCase
    private lateinit var topTvShowPicksViewModel: TopTvShowViewModel
    private val actorId = 123L
    private val tvShowId = 1L
    private val testDispatcher = StandardTestDispatcher()

    val savedStateHandle = SavedStateHandle(mapOf("actorId" to actorId))

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getActorTvShowUseCase = mockk(relaxed = true)
        coEvery { getActorTvShowUseCase(actorId) } returns MockTvShow.TV_SHOWS
        topTvShowPicksViewModel =
            TopTvShowViewModel(
                savedStateHandle = savedStateHandle,
                getActorTvShowUseCase = getActorTvShowUseCase,
                ioDispatcher = testDispatcher
            )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.scheduler.cancel()
        TestScope().cancel()
    }


    @Test
    fun `should send NavigateToTvShowDetails effect with correct tvShowId when clicked`() =
        runTest {
            // Given
            var receivedEffect: TopTvShowPicksEffect? = null
            val job = launch { topTvShowPicksViewModel.uiEffect.collect { receivedEffect = it } }

            // When
            topTvShowPicksViewModel.onTvShowDetailsClick(tvShowId)
            advanceUntilIdle()

            // Then
            assertThat(receivedEffect)
                .isInstanceOf(TopTvShowPicksEffect.NavigateToTvShowDetails::class.java)

            val effect = receivedEffect as TopTvShowPicksEffect.NavigateToTvShowDetails
            assertThat(effect.tvShowId).isEqualTo(tvShowId)

            job.cancel()
        }


    @Test
    fun `should Navigate Back when onBackClick `() = runTest {
        // Given
        var receivedEffect: TopTvShowPicksEffect? = null
        val job = launch { topTvShowPicksViewModel.uiEffect.collect { receivedEffect = it } }

        // When
        topTvShowPicksViewModel.onBackClick()
        advanceUntilIdle()

        // Then
        assertThat(receivedEffect).isInstanceOf(TopTvShowPicksEffect.NavigateBack::class.java)
        job.cancel()
    }

    @Test
    fun `should show no internet snackBar when NoInternetException is thrown`() = runTest {
        // Given
        coEvery { getActorTvShowUseCase(actorId) } throws NoInternetException()
        val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()

        val job = launch {
            topTvShowPicksViewModel.snackBarState.collect {
                emittedSnackBarMessages.add(it.message)
            }
        }

        // When
        topTvShowPicksViewModel.onSnackBarActionLabelClick()
        advanceUntilIdle()

        // Then
        assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NetworkError)
        job.cancel()
    }
}