package com.baghdad.viewmodel.episodeDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.baghdad.domain.usecase.episode.AddEpisodeRateUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeAccountStatesUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeCastMembersUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeDetailsUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
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
class EpisodeDetailsViewModelTest {

    private val getEpisodeAccountStatesUseCase = mockk<GetEpisodeAccountStatesUseCase>()
    private val getEpisodeDetailsUseCase = mockk<GetEpisodeDetailsUseCase>()
    private val getEpisodeCastMembersUseCase = mockk<GetEpisodeCastMembersUseCase>()
    private val addEpisodeRateUseCase = mockk<AddEpisodeRateUseCase>()
    private val isUserLoggedInUseCase = mockk<IsUserLoggedInUseCase>()

    private val testDispatcher = StandardTestDispatcher()

    lateinit var viewModel: EpisodeDetailsViewModel


    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onBackClick should emit NavigateBack effect when icon back clicked`() = runTest {
        viewModel = createViewModel()

        viewModel.onBackClick()

        viewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(EpisodeDetailsScreenEffect.NavigateBack)
        }
    }

    @Test
    fun `onReadMoreOverviewClick should toggle isOverviewExpanded state when read more clicked`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onReadMoreOverviewClick()

            assertThat(viewModel.uiState.value.isOverviewExpanded).isTrue()
        }

    @Test
    fun `onCategoryClick should emit NavigateToCategoryTvShows with id when category selected`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onCategoryClick(categoryId = 22L)

            viewModel.uiEffect.test {
                assertThat(awaitItem())
                    .isEqualTo(EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(22L))
            }
        }

    @Test
    fun `onGuestOfHonorClick should emit NavigateToActorDetails with correct id when clicked`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onGuestOfHonorClick(guestOfHonorId = 22L)

            viewModel.uiEffect.test {
                assertThat(awaitItem())
                    .isEqualTo(EpisodeDetailsScreenEffect.NavigateToActorDetails(22L))
            }
        }

    @Test
    fun `onLoginClick should emit NavigateToLogin effect when called`() = runTest {
        viewModel = createViewModel()

        viewModel.onLoginClick()

        viewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(EpisodeDetailsScreenEffect.NavigateToLogin)
        }
    }

    @Test
    fun `onClickLoginButton should emit NavigateToLogin effect when called`() = runTest {
        viewModel = createViewModel()

        viewModel.onClickLoginButton()

        viewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(EpisodeDetailsScreenEffect.NavigateToLogin)
        }
    }

    @Test
    fun `onDismissAddToListBottomSheetClick should hide addToListBottomSheet when called`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onDismissAddToListBottomSheetClick()

            assertThat(viewModel.uiState.value.addToListBottomSheetState.isVisible).isFalse()
        }


    @Test
    fun `onDismissRatingBottomSheet should hide rateEpisodeBottomSheet when called`() = runTest {
        viewModel = createViewModel()

        viewModel.onDismissRatingBottomSheet()

        assertThat(viewModel.uiState.value.rateEpisodeBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `onClickStarButton should show rateEpisodeBottomSheet when called`() = runTest {
        viewModel = createViewModel()

        viewModel.onClickStarButton()

        assertThat(viewModel.uiState.value.ratingStatus.isBottomSheetVisible).isTrue()
    }

    @Test
    fun `onRatingChanged should update episode userRating when called`() = runTest {
        viewModel = createViewModel()
        val rating = 22

        viewModel.onRatingChanged(rating)

        assertThat(viewModel.uiState.value.episode.userRating).isEqualTo(rating)
    }

    @Test
    fun `onClickSubmitRating should call addEpisodeRateUseCase when called`() = runTest {
        viewModel = createViewModel()
        val rating = 22

        viewModel.onClickSubmitRating(rating)
        advanceUntilIdle()

        coVerify { addEpisodeRateUseCase(any(), any(), any(), any()) }
    }

    @Test
    fun `onSnackBarActionLabelClick should call loadInitData when called`() = runTest {
        viewModel = createViewModel()
        viewModel.onSnackBarActionLabelClick()
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.isEpisodeDetailsLoading).isFalse()
    }

    val savedStateHandle = SavedStateHandle(
        mapOf(
            "tvShowId" to 1L,
            "seasonNumber" to 1,
            "episodeNumber" to 1
        )
    )

    private fun createViewModel(): EpisodeDetailsViewModel {
        return EpisodeDetailsViewModel(
            getEpisodeCastMembersUseCase = getEpisodeCastMembersUseCase,
            getEpisodeDetailsUseCase = getEpisodeDetailsUseCase,
            addEpisodeRateUseCase = addEpisodeRateUseCase,
            getEpisodeAccountStatesUseCase = getEpisodeAccountStatesUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            ioDispatcher = testDispatcher,
            savedStateHandle = savedStateHandle
        )
    }
}