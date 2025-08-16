package com.baghdad.viewmodel.topTvShowPicks

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowPicksEffect.NavigateBack
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowPicksEffect.NavigateToTvShowDetails
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopTvShowPicksViewModelTest {
    private var getActorTvShowUseCase = mockk<GetActorTvShowUseCase>()
    private val testDispatcher = StandardTestDispatcher()
    lateinit var viewModel: TopTvShowViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onTvShowDetailsClick should send NavigateToTvShowDetails effect with correct tvShowId`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onTvShowDetailsClick(TV_SHOW_ID)

            viewModel.uiEffect.test {
                assertThat(awaitItem()).isEqualTo(NavigateToTvShowDetails(TV_SHOW_ID))
                cancelAndIgnoreRemainingEvents()
            }
        }


    @Test
    fun `onBackClick should Navigate Back when clicked `() = runTest {
        viewModel = createViewModel()

        viewModel.onBackClick()

        viewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should show no internet snackBar when NoInternetException is thrown`() = runTest {
        coEvery { getActorTvShowUseCase(ACTOR_ID) } throws NoInternetException()
        viewModel = createViewModel()

        viewModel.onSnackBarActionLabelClick()
        advanceUntilIdle()

        viewModel.snackBarState.test {
            val state = awaitItem()
            assertThat(state.message).isEqualTo(BaseSnackBarMessage.NetworkError)
        }
    }

    companion object {
        private const val ACTOR_ID = 123L
        private const val TV_SHOW_ID = 1L

        val savedStateHandle = SavedStateHandle(mapOf("actorId" to ACTOR_ID))
    }

    private fun createViewModel(): TopTvShowViewModel {
        return TopTvShowViewModel(
            savedStateHandle = savedStateHandle,
            getActorTvShowUseCase = getActorTvShowUseCase,
            ioDispatcher = testDispatcher
        )
    }
}