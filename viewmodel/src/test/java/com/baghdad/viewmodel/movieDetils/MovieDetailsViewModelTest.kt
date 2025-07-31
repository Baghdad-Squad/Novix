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
    fun `initialize() should call all required use cases and update state correctly when successful`() =
        runTest {
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
            assertThat(state.movieId).isEqualTo(movieId)
            assertThat(state.movieName).isEqualTo("Test Movie")
            assertThat(state.overView).isEqualTo("Test movie overview")
            assertThat(state.rating).isWithin(0.01).of(8.0)
            assertThat(state.duration).isAnyOf("2 hr 0 min", "2h 0min")
            assertThat(state.posterImageURL).isEqualTo("/movie_poster.jpg")
            assertThat(state.movieImages).hasSize(3)
            assertThat(state.castMembers).hasSize(2)
            assertThat(state.moreLikeThisMovie).hasSize(2)
            assertThat(state.categories).hasSize(2)
            assertThat(state.isLoading).isFalse()
        }

    @Test
    fun `onExtendOverviewClick() should toggle isExtendText state when called`() = runTest {
        // Given
        advanceUntilIdle()
        val initialState = viewModel.uiState.first().isExtendText

        // When
        viewModel.onExtendOverviewClick()

        // Then
        val newState = viewModel.uiState.first().isExtendText
        assertThat(newState).isNotEqualTo(initialState)
    }

    @Test
    fun `onCategoryClick() should emit NavigateToCategory effect with correct categoryId when called`() =
        runTest {
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
            assertThat(effects.last()).isEqualTo(MovieDetailsEffect.NavigateToCategory(categoryId))
            job.cancel()
        }

    @Test
    fun `onActorClick() should emit NavigateToActorDetails effect with correct actorId when called`() =
        runTest {
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
            assertThat(effects.last()).isEqualTo(MovieDetailsEffect.NavigateToActorDetails(actorId))
            job.cancel()
        }

    @Test
    fun `onMovieClick() should emit NavigateToMovie effect with correct movieId when called`() =
        runTest {
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
            assertThat(effects.last()).isEqualTo(MovieDetailsEffect.NavigateToMovie(clickedMovieId))
            job.cancel()
        }

    @Test
    fun `onTrailerClick() should emit OpenYoutubeLink effect with correct URL when called`() =
        runTest {
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
            assertThat(effects.last()).isEqualTo(MovieDetailsEffect.OpenYoutubeLink(expectedUrl))
            job.cancel()
        }

    @Test
    fun `initialize() should handle errors gracefully when getMovieDetails fails`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { getMovieDetailsUseCase(any()) } throws exception

        // When
        createViewModel()
        advanceUntilIdle()

        // Then
        coVerify { getMovieDetailsUseCase(movieId) }
    }

    @Test
    fun `onStarMovieClick() should update loading state correctly when called`() = runTest {
        // Given
        advanceUntilIdle()

        // When
        viewModel.onStarMovieClick()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.first().isLoading).isFalse()
    }

    @Test
    fun `onSaveCurrentMovieClick() should update loading state correctly when called`() = runTest {
        // Given
        advanceUntilIdle()

        // When
        viewModel.onSaveCurrentMovieClick()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.first().isLoading).isFalse()
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