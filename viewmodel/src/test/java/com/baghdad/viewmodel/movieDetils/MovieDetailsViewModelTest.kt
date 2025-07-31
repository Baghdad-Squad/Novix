package com.baghdad.viewmodel.movieDetils

import com.baghdad.domain.usecase.continueWatching.AddContinueWatchingUseCase
import com.baghdad.domain.usecase.movie.GetMovieCastMembersUseCase
import com.baghdad.domain.usecase.movie.GetMovieDetailsUseCase
import com.baghdad.domain.usecase.movie.GetMovieGalleryUseCase
import com.baghdad.domain.usecase.movie.GetSimilarMoviesUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.movieDetails.MovieDetailsEffect
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

        coEvery { getMovieDetailsUseCase(any()) } returns createMockMovie()
        coEvery { getCastsInfoUseCase(any()) } returns createMockCastMembers()
        coEvery { getMovieImagesUseCase(any()) } returns createMockImages()
        coEvery { getMoreLikeThisPosterImageUseCase(any()) } returns createMockSimilarMovies()
        coEvery { addContinueWatchingUseCase(any(), any(), any(), any()) } returns Unit
        movieDetailsViewModel = MovieDetailsViewModel(
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            getCastsInfoUseCase = getCastsInfoUseCase,
            getMovieImagesUseCase = getMovieImagesUseCase,
            getMoreLikeThisPosterImageUseCase = getMoreLikeThisPosterImageUseCase,
            addContinueWatchingUseCase = addContinueWatchingUseCase,
            movieId = movieId,
            defaultDispatcher = testDispatcher,
        )
    }

    @Test
    fun `onExtendOverviewClick should toggle isExtendText state successfully when clicked`() =
        runTest {
            // Given
            val initialState = movieDetailsViewModel.uiState.value.isExtendText
            // When
            movieDetailsViewModel.onExtendOverviewClick()
            val finalState = movieDetailsViewModel.uiState.value.isExtendText
            // Then
            assertThat(!initialState == finalState).isTrue()
        }

    @Test
    fun `onCategoryClick should Navigate To Category when clicked`() = runTest {
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
        assertThat(MovieDetailsEffect.NavigateToCategory(categoryId) == receivedEffect).isTrue()
        job.cancel()
    }

    @Test
    fun `onSaveMoreLikeThisMedia should toggle isSaved state for the movie when clicked`() =
        runTest {
            // Given
            val initialState = movieDetailsViewModel.uiState.value.moreLikeThisMovie.first().isSaved
            // When
            movieDetailsViewModel.onSaveMoreLikeThisMedia(456L)
            advanceUntilIdle()
            val finalState = movieDetailsViewModel.uiState.value.moreLikeThisMovie.first().isSaved
            // Then
            assertThat(!initialState == finalState).isTrue()
        }

    @Test
    fun `onBackClicked should Navigate Back when clicked`() = runTest {
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
        assertThat(MovieDetailsEffect.NavigateBack == receivedEffect).isTrue()
        job.cancel()
    }

    @Test
    fun `onActorClick should send Navigate To ActorDetails when clicked`() = runTest {
        // Given
        val actorId = 456L
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
        assertThat(MovieDetailsEffect.NavigateToActorDetails(actorId) == receivedEffect).isTrue()
        job.cancel()
    }

    @Test
    fun `onReviewClick should send Navigate To ReviewDetails when clicked`() = runTest {
        // Given
        val reviewId = 789L
        var receivedEffect: MovieDetailsEffect? = null
        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        movieDetailsViewModel.onReviewClick(reviewId)
        advanceUntilIdle()
        // Then
        assertThat(MovieDetailsEffect.NavigateToReviewDetails(reviewId) == receivedEffect).isTrue()
        job.cancel()
    }

    @Test
    fun `onMovieClick should Navigate To Movie when clicked`() = runTest {
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
        assertThat(MovieDetailsEffect.NavigateToMovie(movieId) == receivedEffect).isTrue()
        job.cancel()
    }

    @Test
    fun `onBackClick should send Navigate Back when clicked`() = runTest {
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
        assertThat(MovieDetailsEffect.NavigateBack == receivedEffect).isTrue()
        job.cancel()
    }

    @Test
    fun `onTrailerClick should Open Youtube Link with correct URL when clicked`() = runTest {
        // Given
        var receivedEffect: MovieDetailsEffect? = null
        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        movieDetailsViewModel.onTrailerClick()
        advanceUntilIdle()
        // Then
        val expectedUrl = movieDetailsViewModel.uiState.value.movieTrailerURL
        assertThat(MovieDetailsEffect.OpenYoutubeLink(expectedUrl) == receivedEffect).isTrue()
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

//    @Test
//    fun `mapThrowableToErrorMessage should return UnknownError when mapping throwable to error message`() {
//        // Given
//        val throwable = RuntimeException("Test error")
//        // When
//        val result = movieDetailsViewModel.mapThrowableToErrorMessage(throwable)
//        // Then
//        assertThat(BaseSnackBarMessage.UnknownError == result).isTrue()
//    }


    companion object {
        private fun createMockMovie() = Movie(
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

        private fun createMockSimilarMovies() = listOf(
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
            )
        )
    }
}