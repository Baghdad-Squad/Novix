package com.baghdad.viewmodel.movieDetils

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.domain.usecase.continueWatching.AddContinueWatchingUseCase
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
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.movieDetails.MovieDetailsEffect
import com.baghdad.viewmodel.movieDetails.MovieDetailsState
import com.baghdad.viewmodel.movieDetails.MovieDetailsViewModel
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
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var getCastsInfoUseCase: GetMovieCastMembersUseCase
    private lateinit var getMovieImagesUseCase: GetMovieGalleryUseCase
    private lateinit var getMoreLikeThisPosterImageUseCase: GetSimilarMoviesUseCase
    private lateinit var addContinueWatchingUseCase: AddContinueWatchingUseCase
    private lateinit var getMovieAccountStatesUseCase: GetMovieAccountStatesUseCase
    private lateinit var addMovieRateUseCase: AddMovieRateUseCase
    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase
    private lateinit var addMovieToSavedListUseCase: AddMovieToSavedListUseCase
    private lateinit var createSavedListUseCase: CreateSavedListUseCase
    private lateinit var getSavedListsUseCase: GetSavedListsUseCase
    private lateinit var removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase
    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val movieId = 123L

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getMovieDetailsUseCase = mockk()
        getCastsInfoUseCase = mockk()
        getMovieImagesUseCase = mockk()
        getMoreLikeThisPosterImageUseCase = mockk()
        addContinueWatchingUseCase = mockk()
        getMovieAccountStatesUseCase = mockk()
        addMovieRateUseCase = mockk()
        isUserLoggedInUseCase = mockk()
        addMovieToSavedListUseCase = mockk()
        createSavedListUseCase = mockk()
        getSavedListsUseCase = mockk()
        removeMovieFromSavedListUseCase = mockk()

        coEvery { getMovieDetailsUseCase(any()) } returns createMockMovie()
        coEvery { getCastsInfoUseCase(any()) } returns createMockCastMembers()
        coEvery { getMovieImagesUseCase(any()) } returns createMockImages()
        coEvery { getMoreLikeThisPosterImageUseCase(any()) } returns createMockSimilarMovies()
        coEvery { addContinueWatchingUseCase(any(), any(), any(), any()) } returns Unit
        coEvery { getMovieAccountStatesUseCase(any()) } returns createMockAccountStates()
        coEvery { addMovieRateUseCase(any(), any()) } returns Unit
        coEvery { isUserLoggedInUseCase() } returns true
        coEvery { addMovieToSavedListUseCase(any(), any()) } returns Unit
        coEvery { createSavedListUseCase(any()) } returns Unit
        coEvery { removeMovieFromSavedListUseCase(any(), any()) } returns Unit

        val savedStateHandle = SavedStateHandle(mapOf("movieId" to movieId))

        movieDetailsViewModel = MovieDetailsViewModel(
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            getCastsInfoUseCase = getCastsInfoUseCase,
            getMovieImagesUseCase = getMovieImagesUseCase,
            getMoreLikeThisPosterImageUseCase = getMoreLikeThisPosterImageUseCase,
            addContinueWatchingUseCase = addContinueWatchingUseCase,
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

    @Test
    fun `onExtendOverviewClick should toggle isExtendText state successfully when clicked`() =
        runTest {
            // Given
            val initialState = movieDetailsViewModel.uiState.value.isExtendText
            // When
            movieDetailsViewModel.onExtendOverviewClick()
            advanceUntilIdle()
            val finalState = movieDetailsViewModel.uiState.value.isExtendText
            // Then
            assertThat(!initialState == finalState).isTrue()
        }

    @Test
    fun `onCategoryClick should navigate to category when clicked`() = runTest {
        // Given
        val categoryId = 28L
        var receivedEffect: MovieDetailsEffect? = null
        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        movieDetailsViewModel.onCategoryClick(categoryId)
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect).isEqualTo(MovieDetailsEffect.NavigateToCategory(categoryId))
        job.cancel()
    }

    @Test
    fun `onSaveMoreLikeThisMedia should toggle isSaved state for the movie when clicked`() =
        runTest {
            // Given
            val moreLikeThisMovie = MovieDetailsState.MoreLikeThisMovie(
                imageUrl = "/similar_movie1.jpg",
                id = 456L,
                isSaved = true,
            )
            // When
            movieDetailsViewModel.onSaveMoreLikeThisMedia(moreLikeThisMovie)
            advanceUntilIdle()
            val finalState =
                movieDetailsViewModel.uiState.value.moreLikeThisMovie.find { it.id == moreLikeThisMovie.id }?.isSaved
            // Then
            assertThat(finalState).isFalse() // Expect isSaved to be false after toggling
        }

    @Test
    fun `onBackClicked should navigate back when clicked`() = runTest {
        // Given
        var receivedEffect: MovieDetailsEffect? = null
        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        movieDetailsViewModel.onBackClicked()
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect).isEqualTo(MovieDetailsEffect.NavigateBack)
        job.cancel()
    }

    @Test
    fun `onActorClick should navigate to actor details when clicked`() = runTest {
        // Given
        val actorId = 1L
        var receivedEffect: MovieDetailsEffect? = null
        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        movieDetailsViewModel.onActorClick(actorId)
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect).isEqualTo(MovieDetailsEffect.NavigateToActorDetails(actorId))
        job.cancel()
    }

    @Test
    fun `onReviewClick should navigate to review details when clicked`() = runTest {
        // Given
        var receivedEffect: MovieDetailsEffect? = null
        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        movieDetailsViewModel.onReviewClick()
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect).isEqualTo(MovieDetailsEffect.NavigateToReviewDetails(movieId))
        job.cancel()
    }

    @Test
    fun `onMovieClick should navigate to movie when clicked`() = runTest {
        // Given
        val movieId = 321L
        var receivedEffect: MovieDetailsEffect? = null
        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        movieDetailsViewModel.onMovieClick(movieId)
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect).isEqualTo(MovieDetailsEffect.NavigateToMovie(movieId))
        job.cancel()
    }

    @Test
    fun `should navigate back when clicked onBack`() = runTest {
        // Given
        var receivedEffect: MovieDetailsEffect? = null
        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        movieDetailsViewModel.onBackClick()
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect).isEqualTo(MovieDetailsEffect.NavigateBack)
        job.cancel()
    }

    @Test
    fun `onTrailerClick should open YouTube link with correct URL when clicked`() = runTest {
        // Given
        var receivedEffect: MovieDetailsEffect? = null
        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        movieDetailsViewModel.onClickPlayTrailer()
        advanceUntilIdle()
        // Then
        val expectedUrl = movieDetailsViewModel.uiState.value.movieTrailerURL
        assertThat(receivedEffect).isEqualTo(MovieDetailsEffect.OpenYoutubeLink(expectedUrl))
        job.cancel()
    }

    @Test
    fun `onSaveCurrentMovieClick should set loading state correctly during execution when clicked`() =
        runTest {
            // When
            movieDetailsViewModel.onSaveCurrentMovieClick()
            advanceUntilIdle()
            // Then
            val finalState = movieDetailsViewModel.uiState.value
            assertThat(finalState.isLoading).isFalse()
        }

    @Test
    fun `onSnackBarActionLabelClick should show no internet snackBar when NoInternetException is thrown`() =
        runTest {
            // Given
            coEvery { getMovieDetailsUseCase(any()) } throws NoInternetException()
            val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()
            val job = launch {
                movieDetailsViewModel.snackBarState.collect {
                    emittedSnackBarMessages.add(it.message)
                }
            }
            // When
            movieDetailsViewModel = MovieDetailsViewModel(
                getMovieDetailsUseCase = getMovieDetailsUseCase,
                getCastsInfoUseCase = getCastsInfoUseCase,
                getMovieImagesUseCase = getMovieImagesUseCase,
                getMoreLikeThisPosterImageUseCase = getMoreLikeThisPosterImageUseCase,
                addContinueWatchingUseCase = addContinueWatchingUseCase,
                savedStateHandle = SavedStateHandle(mapOf("movieId" to movieId)),
                getMovieAccountStatesUseCase = getMovieAccountStatesUseCase,
                addMovieRateUseCase = addMovieRateUseCase,
                isUserLoggedInUseCase = isUserLoggedInUseCase,
                addMovieToSavedListUseCase = addMovieToSavedListUseCase,
                getSavedListsUseCase = getSavedListsUseCase,
                removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
                createSavedListUseCase = createSavedListUseCase,
                ioDispatcher = testDispatcher
            )
            movieDetailsViewModel.onSnackBarActionLabelClick()
            advanceUntilIdle()
            // Then
            assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NetworkError)
            job.cancel()
        }

    companion object {
        private fun createMockMovie() =
            SavableMovie(
                Movie(
                    id = 123L,
                    title = "Test Movie",
                    genres = listOf(
                        Genre(28L, "Action"),
                        Genre(35L, "Comedy")
                    ),
                    averageRating = 8.0,
                    userRating = 7.5,
                    releaseDate = LocalDate.parse("2023-01-01"),
                    overview = "Test movie overview",
                    posterImageURL = "/movie_poster.jpg",
                    runtimeMinutes = 120,
                    trailerURL = "https://youtube.com/watch?v=test"
                ),
                isSaved = false,
                listId = null
            )

        private fun createMockCastMembers() = listOf(
            CastMember(
                actor = Actor(
                    id = 1L,
                    name = "John Doe",
                    profilePictureURL = "/john_doe.jpg",
                    birthDate = LocalDate.parse("1980-01-01"),
                    placeOfBirth = "New York, USA",
                    deathDate = null,
                    biography = "Famous actor",
                    headerPictures = listOf("/header1.jpg"),
                    department = "Acting"
                ),
                characterName = "Hero"
            ),
            CastMember(
                actor = Actor(
                    id = 2L,
                    name = "Jane Smith",
                    profilePictureURL = "/jane_smith.jpg",
                    birthDate = LocalDate.parse("1985-01-01"),
                    placeOfBirth = "Los Angeles, USA",
                    deathDate = null,
                    biography = "Talented actress",
                    headerPictures = listOf("/header2.jpg"),
                    department = "Acting"
                ),
                characterName = "Villain"
            )
        )

        private fun createMockImages() = listOf(
            "/image1.jpg",
            "/image2.jpg",
            "/image3.jpg"
        )

        private fun createMockSimilarMovies(): List<SavableMovie> = listOf(
            SavableMovie(
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
                ),
                isSaved = false,
                listId = null
            ),
            SavableMovie(
                Movie(
                    id = 789L,
                    title = "Similar Movie 2",
                    genres = listOf(Genre(35L, "Comedy")),
                    averageRating = 6.8,
                    userRating = 6.5,
                    releaseDate = LocalDate.parse("2023-03-01"),
                    overview = "Similar movie overview 2",
                    posterImageURL = "/similar_movie2.jpg",
                    runtimeMinutes = 95,
                    trailerURL = "https://youtube.com/watch?v=similar2"
                ),
                isSaved = false,
                listId = null
            )
        )

        private fun createMockAccountStates() = MediaAccountStates(isMediaRated = true)
    }
}