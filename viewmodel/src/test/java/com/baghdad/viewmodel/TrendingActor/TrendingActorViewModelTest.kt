package com.baghdad.viewmodel.TrendingActor

import com.baghdad.domain.usecase.actor.GetTrendingActorsUseCase
import com.baghdad.viewmodel.trendingActors.TrendingActorViewModel
import com.baghdad.viewmodel.trendingActors.TrendingActorsUiEffect
import com.google.common.truth.Truth
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingActorViewModelTest {

    lateinit var viewModel: TrendingActorViewModel
    lateinit var getTrendingActorsUseCase: GetTrendingActorsUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getTrendingActorsUseCase = mockk()
        viewModel = TrendingActorViewModel(getTrendingActorsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should send NavigateBack when onBackClick called`() = runTest {
        // Given
        val effects = mutableListOf<TrendingActorsUiEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onBackClick()
        advanceUntilIdle()

        // Then
        Truth.assertThat(effects.first())
            .isInstanceOf(TrendingActorsUiEffect.OnBackClick::class.java)

        job.cancel()
    }

    @Test
    fun `should send NavigateToActorsDetails when onTrendingActorClick called`() = runTest {
        // Given
        val effects = mutableListOf<TrendingActorsUiEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onTrendingActorClick(123L)
        advanceUntilIdle()

        // Then
        Truth.assertThat(effects.first())
            .isInstanceOf(TrendingActorsUiEffect.NavigateToActorsDetails::class.java)

        job.cancel()
    }
}