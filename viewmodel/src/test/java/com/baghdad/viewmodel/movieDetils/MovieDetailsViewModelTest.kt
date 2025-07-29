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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var getCastsInfoUseCase: GetMovieCastMembersUseCase
    private lateinit var getMovieImagesUseCase: GetMovieGalleryUseCase
    private lateinit var getMoreLikeThisPosterImageUseCase: GetSimilarMoviesUseCase
    private lateinit var addContinueWatchingUseCase: AddContinueWatchingUseCase
    private lateinit var viewModel: MovieDetailsViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val movieId = 123L

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        setupMocks()
        createViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun setupMocks() {
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
    }

    private fun createViewModel() {
        viewModel = MovieDetailsViewModel(
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            getCastsInfoUseCase = getCastsInfoUseCase,
            getMovieImagesUseCase = getMovieImagesUseCase,
            getMoreLikeThisPosterImageUseCase = getMoreLikeThisPosterImageUseCase,
            addContinueWatchingUseCase = addContinueWatchingUseCase,
            movieId = movieId
        )
    }

    @Test
    fun `given successful initialization when viewModel is created then all use cases are called and state is updated`() = runTest {
        // When
        advanceUntilIdle()

        // Then
        coVerify {
            getMovieDetailsUseCase(movieId)
            getCastsInfoUseCase(movieId)
            getMovieImagesUseCase(movieId)
            getMoreLikeThisPosterImageUseCase(movieId)
            addContinueWatchingUseCase(any(), any(), any(), any())
        }

        val state = viewModel.uiState.first()
        assertEquals(movieId, state.movieId)
        assertEquals("Test Movie", state.movieName)
        assertEquals("Test movie overview", state.overView)
        assertEquals(8.0, state.rating)
        assertTrue(state.duration == "2 hr 0 min" || state.duration == "2h 0min")
        assertEquals("/movie_poster.jpg", state.posterImageURL)
        assertEquals(3, state.movieImages.size)
        assertEquals(2, state.castMembers.size)
        assertEquals(2, state.moreLikeThisMovie.size)
        assertEquals(2, state.categories.size)
        assertFalse(state.isLoading)
    }

    @Test
    fun `given viewModel is initialized when onExtendOverviewClick is called then isExtendText is toggled`() = runTest {
        // Given
        advanceUntilIdle()
        val initialState = viewModel.uiState.first().isExtendText

        // When
        viewModel.onExtendOverviewClick()

        // Then
        val newState = viewModel.uiState.first().isExtendText
        assertEquals(!initialState, newState)
    }

    @Test
    fun `given viewModel is initialized when onCategoryClick is called then NavigateToCategory effect is emitted`() = runTest {
        // Given
        advanceUntilIdle()
        val categoryId = 28L
        val effects = mutableListOf<MovieDetailsEffect>()

        val job = launch {
            viewModel.uiEffect.toList(effects)
        }

        // When
        viewModel.onCategoryClick(categoryId)
        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailsEffect.NavigateToCategory(categoryId), effects.last())
        job.cancel()
    }

    @Test
    fun `given viewModel is initialized when onActorClick is called then NavigateToActorDetails effect is emitted`() = runTest {
        // Given
        advanceUntilIdle()
        val actorId = 456L
        val effects = mutableListOf<MovieDetailsEffect>()

        val job = launch {
            viewModel.uiEffect.toList(effects)
        }

        // When
        viewModel.onActorClick(actorId)
        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailsEffect.NavigateToActorDetails(actorId), effects.last())
        job.cancel()
    }

    @Test
    fun `given viewModel is initialized when onMovieClick is called then NavigateToMovie effect is emitted`() = runTest {
        // Given
        advanceUntilIdle()
        val clickedMovieId = 321L
        val effects = mutableListOf<MovieDetailsEffect>()

        val job = launch {
            viewModel.uiEffect.toList(effects)
        }

        // When
        viewModel.onMovieClick(clickedMovieId)
        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailsEffect.NavigateToMovie(clickedMovieId), effects.last())
        job.cancel()
    }

    @Test
    fun `given viewModel is initialized when onTrailerClick is called then OpenYoutubeLink effect is emitted with correct URL`() = runTest {
        // Given
        advanceUntilIdle()
        val expectedUrl = viewModel.uiState.first().movieTrailerURL
        val effects = mutableListOf<MovieDetailsEffect>()

        val job = launch {
            viewModel.uiEffect.toList(effects)
        }

        // When
        viewModel.onTrailerClick()
        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailsEffect.OpenYoutubeLink(expectedUrl), effects.last())
        job.cancel()
    }

    @Test
    fun `given getMovieDetails fails when viewModel is initialized then error state is handled`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { getMovieDetailsUseCase(any()) } throws exception

        // When
        createViewModel() // Recreate with failing mock
        advanceUntilIdle()

        // Then
        coVerify { getMovieDetailsUseCase(movieId) }
        // Add assertions for error handling in state if applicable
    }

    @Test
    fun `given viewModel is initialized when onStarMovieClick is called then loading state is updated correctly`() = runTest {
        // Given
        advanceUntilIdle()

        // When
        viewModel.onStarMovieClick()
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.first().isLoading)
    }

    @Test
    fun `given viewModel is initialized when onSaveCurrentMovieClick is called then loading state is updated correctly`() = runTest {
        // Given
        advanceUntilIdle()

        // When
        viewModel.onSaveCurrentMovieClick()
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.first().isLoading)
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