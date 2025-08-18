package com.baghdad.viewmodel.trendingActor

import app.cash.turbine.test
import com.baghdad.domain.usecase.actor.GetTrendingActorsUseCase
import com.baghdad.viewmodel.trendingActors.TrendingActorViewModel
import com.baghdad.viewmodel.trendingActors.TrendingActorsUiEffect
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingActorViewModelTest {

    lateinit var viewModel: TrendingActorViewModel
    private val testDispatcher = StandardTestDispatcher()
    private var getTrendingActorsUseCase = mockk<GetTrendingActorsUseCase>()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should send NavigateBack when onBackClick called`() = runTest {
        viewModel = createViewModel()

        viewModel.onBackClick()

        viewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(TrendingActorsUiEffect.OnBackClick)
        }
    }

    @Test
    fun `should send NavigateToActorsDetails when onTrendingActorClick called`() = runTest {
        viewModel = createViewModel()
        val actorId = 123L

        viewModel.onTrendingActorClick(actorId)

        viewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(TrendingActorsUiEffect.NavigateToActorsDetails(actorId))
        }
    }

    private fun createViewModel() = TrendingActorViewModel(getTrendingActorsUseCase)
}