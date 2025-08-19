package com.baghdad.viewmodel.movieDetils

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.movie.AddMovieRateUseCase
import com.baghdad.domain.usecase.movie.GetMovieAccountStatesUseCase
import com.baghdad.domain.usecase.movie.GetMovieCastMembersUseCase
import com.baghdad.domain.usecase.movie.GetMovieDetailsUseCase
import com.baghdad.domain.usecase.movie.GetMovieGalleryUseCase
import com.baghdad.domain.usecase.movie.GetSimilarMoviesUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.domain.usecase.userWatchedMedia.AddUserWatchedMediaUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.movieDetails.MovieDetailsState
import com.baghdad.viewmodel.movieDetails.MovieDetailsViewModel
import com.baghdad.viewmodel.shared.BottomSheetType
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val movieId = 123L

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        setupHappyPathMocks()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `when viewModel is initialized, should load all movie data`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.castMembers).isNotEmpty()
            assertThat(state.movieImages).isNotEmpty()
            assertThat(state.moreLikeThisMovie).isNotEmpty()
        }
    }


    @Test
    fun `when extend overview is clicked, should toggle isExtendText state`() = runTest {
        viewModel = createViewModel()
        val initialState = viewModel.uiState.value.isExtendText

        viewModel.onExtendOverviewClick()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isExtendText).isEqualTo(!initialState)
        }
    }

    @Test
    fun `when save more like this movie is clicked, should toggle saved state`() = runTest {
        viewModel = createViewModel()
        val moreLikeThisMovie = MovieDetailsState.MoreLikeThisMovie(
            imageUrl = "/similar_movie1.jpg",
            id = similarMovieId,
            isSaved = true,
        )

        viewModel.onSaveMoreLikeThisMedia(moreLikeThisMovie)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            val updatedMovie = state.moreLikeThisMovie.find { it.id == similarMovieId }
            assertThat(updatedMovie?.isSaved).isFalse()
        }
        coVerify { removeMovieFromSavedListUseCase.invoke(any(), similarMovieId) }
    }


    @Test
    fun `when star button is clicked and user is logged in, should show rating bottom sheet`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onClickStarButton()
            advanceUntilIdle()

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.ratingStatus.isBottomSheetVisible).isTrue()
                assertThat(state.ratingStatus.bottomSheetType).isEqualTo(BottomSheetType.ShowRating)
            }
        }

    @Test
    fun `when star button is clicked and user is not logged in, should show login bottom sheet`() =
        runTest {
            coEvery { isUserLoggedInUseCase() } returns false
            viewModel = createViewModel()

            viewModel.onClickStarButton()
            advanceUntilIdle()

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.ratingStatus.isBottomSheetVisible).isTrue()
                assertThat(state.ratingStatus.bottomSheetType).isEqualTo(BottomSheetType.RequireLogin)
            }
        }

    @Test
    fun `when create new list is clicked, should show create list bottom sheet`() = runTest {
        viewModel = createViewModel()

        viewModel.onCreateNewListClicked()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addListBottomSheetState.isVisible).isTrue()
            assertThat(state.addToListBottomSheetState.isVisible).isFalse()
        }
    }

    @Test
    fun `when list is created successfully, should refresh saved lists`() = runTest {
        viewModel = createViewModel()
        val listName = "My New List"

        viewModel.onCreatedListNameChanged(listName)
        viewModel.onCreateListBottomSheetAddClick()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addListBottomSheetState.isVisible).isFalse()
        }
        coVerify { createSavedListUseCase.invoke(listName.trim()) }
    }

    private fun createViewModel(): MovieDetailsViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("movieId" to movieId))
        return MovieDetailsViewModel(
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            getCastsInfoUseCase = getCastsInfoUseCase,
            getMovieImagesUseCase = getMovieImagesUseCase,
            getMoreLikeThisPosterImageUseCase = getMoreLikeThisPosterImageUseCase,
            addUserWatchedMediaUseCase = addUserWatchedMediaUseCase,
            savedStateHandle = savedStateHandle,
            getMovieAccountStatesUseCase = getMovieAccountStatesUseCase,
            addMovieRateUseCase = addMovieRateUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
            removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
            createSavedListUseCase = createSavedListUseCase,
            ioDispatcher = testDispatcher
        )
    }

    private object TestData {
        const val TEST_MOVIE_TITLE = "Test Movie"
        const val TEST_MOVIE_OVERVIEW = "Test movie overview"
        const val TEST_MOVIE_POSTER_URL = "/movie_poster.jpg"
        const val TEST_MOVIE_TRAILER_URL = "https://youtube.com/watch?v=test"
        const val TEST_ACTOR_NAME = "John Doe"
        const val TEST_ACTOR_PROFILE_URL = "/john_doe.jpg"

        fun createMockMovie() = SavedMovie(
            Movie(
                id = 123L,
                title = TEST_MOVIE_TITLE,
                genres = listOf(Genre(28L, "Action"), Genre(35L, "Comedy")),
                averageRating = 8.0,
                userRating = 7.5,
                releaseDate = LocalDate.parse("2023-01-01"),
                overview = TEST_MOVIE_OVERVIEW,
                posterImageURL = TEST_MOVIE_POSTER_URL,
                runtimeMinutes = 120,
                trailerURL = TEST_MOVIE_TRAILER_URL
            ), isSaved = false, listId = null
        )

        fun createMockCastMembers() = listOf(
            CastMember(
                actor = Actor(
                    id = 1L,
                    name = TEST_ACTOR_NAME,
                    profilePictureURL = TEST_ACTOR_PROFILE_URL,
                    birthDate = LocalDate.parse("1980-01-01"),
                    placeOfBirth = "New York, USA",
                    deathDate = null,
                    biography = "Famous actor",
                    headerPictures = listOf("/header1.jpg"),
                    department = "Acting"
                ), characterName = "Hero"
            )
        )

        fun createMockImages() = listOf(
            "/image1.jpg", "/image2.jpg", "/image3.jpg"
        )

        fun createMockSimilarMovies() = listOf(
            SavedMovie(
                Movie(
                    id = 456L,
                    title = "Similar Movie 1",
                    genres = listOf(Genre(28L, "Action")),
                    averageRating = 7.5,
                    userRating = 7.0,
                    releaseDate = LocalDate.parse("2023-02-01"),
                    overview = "Similar movie overview 1",
                    posterImageURL = "/similar_movie1.jpg",
                    runtimeMinutes = 110,
                    trailerURL = "https://youtube.com/watch?v=similar1"
                ), isSaved = false, listId = null
            )
        )
    }

    private val getMovieDetailsUseCase = mockk<GetMovieDetailsUseCase>()
    private val getCastsInfoUseCase = mockk<GetMovieCastMembersUseCase>()
    private val getMovieImagesUseCase = mockk<GetMovieGalleryUseCase>()
    private val getMoreLikeThisPosterImageUseCase = mockk<GetSimilarMoviesUseCase>()
    private val addUserWatchedMediaUseCase = mockk<AddUserWatchedMediaUseCase>()
    private val getMovieAccountStatesUseCase = mockk<GetMovieAccountStatesUseCase>()
    private val addMovieRateUseCase = mockk<AddMovieRateUseCase>()
    private val isUserLoggedInUseCase = mockk<IsUserLoggedInUseCase>()
    private val addMovieToSavedListUseCase = mockk<AddMovieToSavedListUseCase>()
    private val createSavedListUseCase = mockk<CreateSavedListUseCase>()
    private val getSavedListsUseCase = mockk<GetSavedListsUseCase>()
    private val removeMovieFromSavedListUseCase = mockk<RemoveMovieFromSavedListUseCase>()

    private lateinit var viewModel: MovieDetailsViewModel

    private val similarMovieId = 456L

    private fun setupHappyPathMocks() {
        coEvery { getMovieDetailsUseCase(any()) } returns TestData.createMockMovie()
        coEvery { getCastsInfoUseCase(any()) } returns TestData.createMockCastMembers()
        coEvery { getMovieImagesUseCase(any()) } returns TestData.createMockImages()
        coEvery { getMoreLikeThisPosterImageUseCase(any()) } returns TestData.createMockSimilarMovies()
        coEvery { getMovieAccountStatesUseCase(any()) } returns true
        coEvery { addMovieRateUseCase(any(), any()) } returns Unit
        coEvery { isUserLoggedInUseCase() } returns true
        coEvery { addMovieToSavedListUseCase(any(), any()) } returns Unit
        coEvery { createSavedListUseCase(any()) } returns Unit
        coEvery { removeMovieFromSavedListUseCase(any(), any()) } returns Unit
    }
}