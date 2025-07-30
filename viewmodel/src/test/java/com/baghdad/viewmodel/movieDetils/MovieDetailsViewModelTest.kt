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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
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

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization should call all use cases and update state successfully`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        val currentState = movieDetailsViewModel.uiState.value
        coVerify { getMovieDetailsUseCase(movieId) }
        coVerify { getCastsInfoUseCase(movieId) }
        coVerify { getMovieImagesUseCase(movieId) }
        coVerify { getMoreLikeThisPosterImageUseCase(movieId) }
        coVerify { addContinueWatchingUseCase(any(), any(), any(), any()) }

        assertEquals(movieId, currentState.movieId)
        assertEquals("Test Movie", currentState.movieName)
        assertEquals("Test movie overview", currentState.overView)
        assertEquals(8.0, currentState.rating)
        assertEquals("/movie_poster.jpg", currentState.posterImageURL)
        assertEquals(3, currentState.movieImages.size)
        assertEquals(2, currentState.castMembers.size)
        assertEquals(2, currentState.moreLikeThisMovie.size)
        assertEquals(2, currentState.categories.size)
        assertFalse(currentState.isLoading)
    }

    @Test
    fun `onExtendOverviewClick should toggle isExtendText state successfully`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val initialState = movieDetailsViewModel.uiState.value.isExtendText

        movieDetailsViewModel.onExtendOverviewClick()

        val finalState = movieDetailsViewModel.uiState.value.isExtendText
        assertEquals(!initialState, finalState)
    }

    @Test
    fun `onCategoryClick should send NavigateToCategory effect`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val categoryId = 28L
        var receivedEffect: MovieDetailsEffect? = null

        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        movieDetailsViewModel.onCategoryClick(categoryId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(MovieDetailsEffect.NavigateToCategory(categoryId), receivedEffect)
        job.cancel()
    }

    @Test
    fun `onBackClicked should send NavigateBack effect`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        var receivedEffect: MovieDetailsEffect? = null

        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        movieDetailsViewModel.onBackClicked()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(MovieDetailsEffect.NavigateBack, receivedEffect)
        job.cancel()
    }

    @Test
    fun `onActorClick should send NavigateToActorDetails effect`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val actorId = 456L
        var receivedEffect: MovieDetailsEffect? = null

        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        movieDetailsViewModel.onActorClick(actorId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(MovieDetailsEffect.NavigateToActorDetails(actorId), receivedEffect)
        job.cancel()
    }

    @Test
    fun `onReviewClick should send NavigateToReviewDetails effect`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val reviewId = 789L
        var receivedEffect: MovieDetailsEffect? = null

        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        movieDetailsViewModel.onReviewClick(reviewId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(MovieDetailsEffect.NavigateToReviewDetails(reviewId), receivedEffect)
        job.cancel()
    }

    @Test
    fun `onMovieClick should send NavigateToMovie effect`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val movieId = 321L
        var receivedEffect: MovieDetailsEffect? = null

        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        movieDetailsViewModel.onMovieClick(movieId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(MovieDetailsEffect.NavigateToMovie(movieId), receivedEffect)
        job.cancel()
    }

    @Test
    fun `onBackClick should send NavigateBack effect`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        var receivedEffect: MovieDetailsEffect? = null

        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        movieDetailsViewModel.onBackClick()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(MovieDetailsEffect.NavigateBack, receivedEffect)
        job.cancel()
    }

    @Test
    fun `onTrailerClick should send OpenYoutubeLink effect with correct URL`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        var receivedEffect: MovieDetailsEffect? = null

        val job = launch {
            movieDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        movieDetailsViewModel.onTrailerClick()
        testDispatcher.scheduler.advanceUntilIdle()

        val expectedUrl = movieDetailsViewModel.uiState.value.movieTrailerURL
        assertEquals(MovieDetailsEffect.OpenYoutubeLink(expectedUrl), receivedEffect)
        job.cancel()
    }

    @Test
    fun `getMovieDetails failure should handle error gracefully`() = runTest {
        val exception = RuntimeException()
        coEvery { getMovieDetailsUseCase(any()) } throws exception
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { getMovieDetailsUseCase(movieId) }
    }

    @Test
    fun `onStarMovieClick should set loading state correctly during execution`() = runTest {

        testDispatcher.scheduler.advanceUntilIdle()


        movieDetailsViewModel.onStarMovieClick()

        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = movieDetailsViewModel.uiState.value
        assertFalse(finalState.isLoading)
    }


    @Test
    fun `onSaveCurrentMovieClick should set loading state correctly during execution`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        movieDetailsViewModel.onSaveCurrentMovieClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val finalState = movieDetailsViewModel.uiState.value
        assertFalse(finalState.isLoading)
    }


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