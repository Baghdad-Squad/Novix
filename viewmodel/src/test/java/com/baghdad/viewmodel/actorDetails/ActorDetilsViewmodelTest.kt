package com.baghdad.viewmodel.actorDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.domain.usecase.actor.GetActorInfoUseCase
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.dummyData.DummyDataFactory.createMockGallery
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActorDetailsViewModelTest {

    private var getActorInfoUseCase = mockk<GetActorInfoUseCase>()
    private var getActorMoviesUseCase = mockk<GetActorMoviesUseCase>()
    private var getActorTvShowUseCase = mockk<GetActorTvShowUseCase>()
    private var getActorGalleryUseCase = mockk<GetActorGalleryUseCase>()
    private var addMovieToSavedListUseCase = mockk<AddMovieToSavedListUseCase>()
    private var isUserLoggedInUseCase = mockk<IsUserLoggedInUseCase>()
    private var getSavedListsUseCase = mockk<GetSavedListsUseCase>()
    private var createSavedListUseCase = mockk<CreateSavedListUseCase>()
    private var removeMovieFromSavedListUseCase = mockk<RemoveMovieFromSavedListUseCase>()

    private lateinit var viewModel: ActorDetailsViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val actorId = 123L

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getActorInfoUseCase(actorId) } returns createMockActor()
        coEvery { getActorMoviesUseCase(actorId) } returns createMockMovies()
        coEvery { getActorTvShowUseCase(actorId) } returns createMockTvShows()
        coEvery { getActorGalleryUseCase(actorId) } returns createMockGallery()

        viewModel = ActorDetailsViewModel(
            savedStateHandle = SavedStateHandle(mapOf("actorId" to actorId)),
            getActorInfoUseCase = getActorInfoUseCase,
            getActorMoviesUseCase = getActorMoviesUseCase,
            getActorTvShowUseCase = getActorTvShowUseCase,
            getActorGalleryUseCase = getActorGalleryUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
            createSavedListUseCase = createSavedListUseCase,
            removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `onBackIconClick should send NavigateBack when it is clicked`() = runTest {
        viewModel.onBackIconClick()

        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect is ActorDetailsScreenEffect.NavigateBack).isTrue()

        }
    }

    @Test
    fun `onReadMoreBiographyClick should toggle isTextExpanded state when it is clicked`() =
        runTest {
            val initialState = viewModel.uiState.value.isTextExpanded

            viewModel.onReadMoreBiographyClick()

            val newState = viewModel.uiState.value.isTextExpanded
            assertThat(newState != initialState).isTrue()
        }

    @Test
    fun `onViewAllGalleryClick should Navigate To ActorGallery screen when it is clicked`() =
        runTest {
            viewModel.onViewAllGalleryClick()

            viewModel.uiEffect.test {
                val effect = awaitItem()
                assertThat(effect is ActorDetailsScreenEffect.NavigateToActorGallery).isTrue()
            }
        }

    @Test
    fun `onViewAllTopMoviesPicksClick should Navigate To ActorTopMoviePicks when it is clicked`() =
        runTest {
            viewModel.onViewAllTopMoviesPicksClick()

            viewModel.uiEffect.test {
                val effect = awaitItem()
                assertThat(effect is ActorDetailsScreenEffect.NavigateToActorTopMoviePicks).isTrue()
            }
        }

    @Test
    fun `onViewAllTopTvShowsClick should Navigate To ActorTopTvShowPicks when it clicked`() =
        runTest {
            viewModel.onViewAllTopTvShowsClick()

            viewModel.uiEffect.test {
                val effect = awaitItem()
                assertThat(effect is ActorDetailsScreenEffect.NavigateToActorTopTvShowPicks).isTrue()
            }
        }

    @Test
    fun `onMovieCardClick should Navigate To MovieDetails screen when clicked and it has the correct movieId`() =
        runTest {

            val movieId = 456L
            viewModel.onMovieCardClick(movieId)

            viewModel.uiEffect.test {
                val effect = awaitItem()
                assertThat(effect is ActorDetailsScreenEffect.NavigateToMovieDetails).isTrue()
            }
        }

    @Test
    fun `onTvShowCardClick should Navigate To TvShow Details screen when clicked and it has the correct tvShowId`() =
        runTest {
            val tvShowId = 789L
            viewModel.onTvShowCardClick(tvShowId)

            viewModel.uiEffect.test {
                val effect = awaitItem()
                assertThat(effect is ActorDetailsScreenEffect.NavigateToTvShowDetails).isTrue()
            }
        }

    @Test
    fun `onLoginClicked should Navigate To login screen when it is clicked`() =
        runTest {
            viewModel.onLoginClicked()

            viewModel.uiEffect.test {
                val effect = awaitItem()
                assertThat(effect is ActorDetailsScreenEffect.NavigateToLogin).isTrue()
            }
        }

    @Test
    fun `getActorInfo should show no internet snackBar when NoInternetException is thrown`() =
        runTest {
            coEvery { getActorInfoUseCase(actorId) } throws NoInternetException()
            val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()
            val job = launch {
                viewModel.snackBarState.collect {
                    emittedSnackBarMessages.add(it.message)
                }
            }

            viewModel.onSnackBarActionLabelClick()
            advanceUntilIdle()
            job.cancel()

            assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NetworkError)
        }

    @Test
    fun `onSaveMovieClicked should open bottom sheet when movie is not saved`() = runTest {
        val movieId = 1L
        val initialState = viewModel.uiState.value
        val movie = initialState.topMoviesPicks.find { it.id == movieId }!!

        viewModel.onSaveMovieClick(movie)
        advanceUntilIdle()

        val updatedState = viewModel.uiState.value
        assertThat(movie.isSaved).isFalse()
        assertThat(updatedState.addToListBottomSheetState.isVisible).isTrue()
        assertThat(updatedState.addToListBottomSheetState.selectedItemId).isEqualTo(movieId)
    }

    @Test
    fun `onSaveMovieClicked should remove movie from list when is saved`() = runTest {
        val movieId = 1L
        val initialState = viewModel.uiState.value
        val movie = initialState.topMoviesPicks.find { it.id == movieId }!!.copy(isSaved = true)
        coEvery { removeMovieFromSavedListUseCase(any(), any()) } returns Unit

        viewModel.onSaveMovieClick(movie)
        advanceUntilIdle()

        coVerify { removeMovieFromSavedListUseCase(any(), any()) }
    }

    @Test
    fun `onSaveItemToListClicked should add movie to saved list when list is selected`() = runTest {

        viewModel.onListSelected(123L)
        coEvery { addMovieToSavedListUseCase(any(), any()) } returns Unit

        viewModel.onSaveItemToListClicked()
        advanceUntilIdle()

        coVerify { addMovieToSavedListUseCase(any(), any()) }
    }

    @Test
    fun `onCreateNewListClicked should show add list bottom sheet when it is clicked`() =
        runTest {
            viewModel.onCreateNewListClicked()
            val state = viewModel.uiState.value
            assertThat(state.addListBottomSheetState.isVisible).isTrue()
            assertThat(state.addToListBottomSheetState.isVisible).isFalse()
        }

    @Test
    fun `onSaveItemToListClicked should not add movie to saved list when list is not selected`() =
        runTest {

            coEvery { addMovieToSavedListUseCase(any(), any()) } returns Unit

            viewModel.onSaveItemToListClicked()
            advanceUntilIdle()

            coVerify(inverse = true) { addMovieToSavedListUseCase(any(), any()) }
        }

    @Test
    fun `onCreateListBottomSheetDismiss should hide bottom sheet and show addToListBottomSheet when it is clicked`() =
        runTest {

            viewModel.onCreateListBottomSheetDismiss()
            advanceUntilIdle()

            val addListBottomSheetState = viewModel.uiState.value.addListBottomSheetState
            val addToListBottomSheetState = viewModel.uiState.value.addToListBottomSheetState
            assertThat(addListBottomSheetState.isVisible).isFalse()
            assertThat(addListBottomSheetState.listName == "").isTrue()
            assertThat(addListBottomSheetState.isLoading).isFalse()
            assertThat(addToListBottomSheetState.isVisible).isTrue()
        }

    @Test
    fun `onCreatedListNameChanged should change list name when it is clicked`() = runTest {

        val listName = "action"

        viewModel.onCreatedListNameChanged(listName)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.addListBottomSheetState.listName == listName).isTrue()
    }

    @Test
    fun `onCreateListBottomSheetAddClick should create new list when it is clicked`() = runTest {

        viewModel.onCreatedListNameChanged("action")
        coEvery { createSavedListUseCase(any()) } returns Unit

        viewModel.onCreateListBottomSheetAddClick()
        advanceUntilIdle()

        coVerify { createSavedListUseCase(any()) }
    }

    @Test
    fun `onGalleryImageClick should change image url state when it is clicked`() = runTest {

        val imageUrl = "/profile.jpg"

        viewModel.onGalleryImageClick(imageUrl)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.selectedImageUrl == imageUrl).isTrue()
    }

    @Test
    fun `onImageDialogDismiss should change image url state to empty when it is clicked`() =
        runTest {

            viewModel.onImageDialogDismiss()
            advanceUntilIdle()

            assertThat(viewModel.uiState.value.selectedImageUrl == "").isTrue()
        }

    companion object {
        private fun createMockActor() = Actor(
            id = 123L,
            name = "John Doe",
            profilePictureURL = "/profile.jpg",
            birthDate = LocalDate.parse("1980-01-01"),
            placeOfBirth = "New York, USA",
            deathDate = null,
            biography = "Famous actor biography",
            headerPictures = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg"),
            department = "Acting"
        )

        private fun createMockMovies() = createMockMoviesWithSize(15)

        private fun createMockMoviesWithSize(size: Int) = (1..size).map { index ->
            SavedMovie(
                movie = Movie(
                    id = index.toLong(),
                    title = "Movie $index",
                    genres = listOf(Genre(1L, "Action")),
                    averageRating = 8.0,
                    userRating = 7.5,
                    releaseDate = LocalDate.parse("2023-01-01"),
                    overview = "Movie overview $index",
                    posterImageURL = "/movie_poster_$index.jpg",
                    runtimeMinutes = 120,
                    trailerURL = "/trailer_$index"
                ),
                isSaved = false,
                listId = null,
            )

        }

        private fun createMockTvShows() = createMockTvShowsWithSize(12)

        private fun createMockTvShowsWithSize(size: Int) = (1..size).map { index ->
            TvShow(
                id = index.toLong(),
                title = "TV Show $index",
                genres = listOf(Genre(1L, "Drama")),
                averageRating = 7.9,
                userRating = 8,
                releaseDate = LocalDate.parse("2023-01-01"),
                overview = "TV Show overview $index",
                posterImageURL = "/tv_poster_$index.jpg",
                numberOfSeasons = 3,
                trailerURL = "/trailer_$index",
                headerImagesURLs = listOf("/header1_$index.jpg", "/header2_$index.jpg")
            )
        }
    }
}
