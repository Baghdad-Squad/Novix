package com.baghdad.viewmodel.myRating

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.userRating.RatedMedia
import com.baghdad.domain.model.userRating.RatedMedia.ContentType
import com.baghdad.domain.usecase.mediaRated.GetUserMediaRatedUseCase
import com.baghdad.domain.usecase.mediaRated.GetUserRatedMoviesUseCase
import com.baghdad.domain.usecase.mediaRated.GetUserRatedTvShowsUseCase
import com.baghdad.domain.usecase.movie.DeleteMovieRateUseCase
import com.baghdad.domain.usecase.tvShow.DeleteTvShowRateUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyRatingViewModelTest {
    private lateinit var getUserRatedMoviesUseCase: GetUserRatedMoviesUseCase
    private lateinit var getUserRatedTvShowsUseCase: GetUserRatedTvShowsUseCase
    private lateinit var deleteMovieRateUseCase: DeleteMovieRateUseCase
    private lateinit var deleteTvShowRateUseCase: DeleteTvShowRateUseCase
    private lateinit var getUserMediaRatedUseCase: GetUserMediaRatedUseCase

    private lateinit var myRatingViewModel: MyRatingViewModel

    private val testDispatcher = StandardTestDispatcher()


    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getUserRatedMoviesUseCase = mockk(relaxed = true)
        getUserRatedTvShowsUseCase = mockk(relaxed = true)
        deleteMovieRateUseCase = mockk(relaxed = true)
        deleteTvShowRateUseCase = mockk(relaxed = true)
        getUserMediaRatedUseCase = mockk(relaxed = true)
        coEvery { getUserMediaRatedUseCase(any(), any()) } returns mockPagedResult()
        coEvery { getUserRatedMoviesUseCase(any(), any()) } returns mockPagedResult()
        coEvery { getUserRatedTvShowsUseCase(any(), any()) } returns mockPagedResult()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = MyRatingViewModel(
        getUserRatedMoviesUseCase,
        getUserRatedTvShowsUseCase,
        deleteMovieRateUseCase,
        deleteTvShowRateUseCase,
        getUserMediaRatedUseCase
    )


    @Test
    fun `clicking movie tab should fetch movies when clicked`() = runTest {
        myRatingViewModel = createViewModel()

        coEvery { getUserRatedMoviesUseCase(1, 20) } returns mockPagedResult()


        myRatingViewModel.onMediaTabClick(MyRatingState.MediaTab.MOVIE)
        advanceUntilIdle()

        assertThat(myRatingViewModel.uiState.value.selectedMediaTab)
            .isEqualTo(MyRatingState.MediaTab.MOVIE)
    }

    @Test
    fun `clicking tv show tab should fetch tv shows when clicked`() = runTest {
        myRatingViewModel = createViewModel()
        advanceUntilIdle()
        coEvery { getUserRatedTvShowsUseCase.invoke(any(), any()) } returns mockPagedResult()

        myRatingViewModel.onMediaTabClick(MyRatingState.MediaTab.TV_SHOW)
        advanceUntilIdle()

        assertThat(myRatingViewModel.uiState.value.selectedMediaTab).isEqualTo(MyRatingState.MediaTab.TV_SHOW)
    }

    @Test
    fun `onMediaClick movie should should navigate to movie details`() = runTest {
        myRatingViewModel = createViewModel()

        myRatingViewModel.onMediaClick(10L, MyRatingState.ContentType.MOVIE)
        val effect = myRatingViewModel.uiEffect.first()

        assertThat(effect).isEqualTo(MyRatingEffect.NavigateToMovieDetails(10L))
    }

    @Test
    fun `onMediaClick tv show should navigate to tv show details`() = runTest {
        myRatingViewModel = createViewModel()

        myRatingViewModel.onMediaClick(20L, MyRatingState.ContentType.TV_SHOW)

        val effect = myRatingViewModel.uiEffect.first()

        assertThat(effect).isEqualTo(MyRatingEffect.NavigateToTvShowDetails(20L))
    }

    @Test
    fun `onBackClick should navigate back when clicked`() = runTest {
        myRatingViewModel = createViewModel()

        myRatingViewModel.onBackClick()

        val effect = myRatingViewModel.uiEffect.first()

        assertThat(effect).isEqualTo(MyRatingEffect.NavigateBack)
    }

    @Test
    fun `should delete movie rating when onDeleteClick is called`() = runTest {
        myRatingViewModel = createViewModel()
        coEvery { getUserMediaRatedUseCase(1, 20) } returns mockPagedResult()
        coEvery { deleteMovieRateUseCase(any()) } returns Unit


        myRatingViewModel.onDeleteClick(6L, MyRatingState.ContentType.MOVIE)
        advanceUntilIdle()

        coVerify { deleteMovieRateUseCase(6L) }
    }

    @Test
    fun `should delete tv show rating when onDeleteClick is called`() = runTest {
        myRatingViewModel = createViewModel()
        coEvery { getUserMediaRatedUseCase(1, 20) } returns mockPagedResult()
        coEvery { deleteTvShowRateUseCase(any()) } returns Unit


        myRatingViewModel.onDeleteClick(6L, MyRatingState.ContentType.TV_SHOW)
        advanceUntilIdle()

        coVerify { deleteTvShowRateUseCase(6L) }
    }


    companion object {

        private fun mockPagedResult(items: List<RatedMedia> = mockMediaList()) =
            PagedResult(items, prevKey = 1, nextKey = null)

        private fun mockMediaList() = listOf(
            RatedMedia(1L, 8, "/poster.jpg", ContentType.MOVIE),
            RatedMedia(2L, 9, "/poster2.jpg", ContentType.TV_SHOW)
        )
    }
}
