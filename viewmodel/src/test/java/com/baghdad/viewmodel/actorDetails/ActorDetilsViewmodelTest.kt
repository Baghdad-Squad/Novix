package com.baghdad.viewmodel.actorDetails

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.domain.usecase.actor.GetActorInfoUseCase
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.dummyData.DummyDataFactory.createMockGallery
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
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
class ActorDetailsViewModelTest {

    private lateinit var getActorInfoUseCase: GetActorInfoUseCase
    private lateinit var getActorMoviesUseCase: GetActorMoviesUseCase
    private lateinit var getActorTvShowUseCase: GetActorTvShowUseCase
    private lateinit var getActorGalleryUseCase: GetActorGalleryUseCase
    private lateinit var viewModel: ActorDetailsViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val actorId = 123L

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getActorInfoUseCase = mockk()
        getActorMoviesUseCase = mockk()
        getActorTvShowUseCase = mockk()
        getActorGalleryUseCase = mockk()
        coEvery { getActorInfoUseCase(actorId) } returns createMockActor()
        coEvery { getActorMoviesUseCase(actorId) } returns createMockMovies()
        coEvery { getActorTvShowUseCase(actorId) } returns createMockTvShows()
        coEvery { getActorGalleryUseCase(actorId) } returns createMockGallery()
        viewModel = ActorDetailsViewModel(
            actorId = actorId,
            getActorInfoUseCase = getActorInfoUseCase,
            getActorMoviesUseCase = getActorMoviesUseCase,
            getActorTvShowUseCase = getActorTvShowUseCase,
            getActorGalleryUseCase = getActorGalleryUseCase,
            ioDispatcher = testDispatcher
        )
    }


    @Test
    fun `onBackIconClick should send NavigateBack when it is clicked`() = runTest {
        // Given
        var receivedEffect: ActorDetailsScreenEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        viewModel.onBackIconClick()
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect is ActorDetailsScreenEffect.NavigateBack).isTrue()
        job.cancel()
    }

    @Test
    fun `onReadMoreBiographyClick should toggle isTextExpanded state when it is clicked`() =
        runTest {
            // When
            val initialState = viewModel.uiState.value.isTextExpanded
            viewModel.onReadMoreBiographyClick()
            val newState = viewModel.uiState.value.isTextExpanded
            // Then
            assertThat(newState != initialState).isTrue()
        }

    @Test
    fun `onViewAllGalleryClick should Navigate To ActorGallery screen when it is clicked`() =
        runTest {
            // Given
            var receivedEffect: ActorDetailsScreenEffect? = null
            val job = launch {
                viewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            viewModel.onViewAllGalleryClick()
            advanceUntilIdle()
            // Then
            assertThat(receivedEffect is ActorDetailsScreenEffect.NavigateToActorGallery).isTrue()
            job.cancel()
        }

    @Test
    fun `onViewAllTopMoviesPicksClick should Navigate To ActorTopMoviePicks when it is clicked`() =
        runTest {
            // Given
            var receivedEffect: ActorDetailsScreenEffect? = null
            val job = launch {
                viewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            viewModel.onViewAllTopMoviesPicksClick()
            advanceUntilIdle()
            // Then
            assertThat(receivedEffect is ActorDetailsScreenEffect.NavigateToActorTopMoviePicks).isTrue()
            job.cancel()
        }

    @Test
    fun `onViewAllTopTvShowsClick should Navigate To ActorTopTvShowPicks when it clicked`() =
        runTest {
            // Given
            var receivedEffect: ActorDetailsScreenEffect? = null
            val job = launch {
                viewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            viewModel.onViewAllTopTvShowsClick()
            advanceUntilIdle()
            // Then
            assertThat(receivedEffect is ActorDetailsScreenEffect.NavigateToActorTopTvShowPicks).isTrue()
            job.cancel()
        }

    @Test
    fun `onMovieCardClick should Navigate To MovieDetails screen when clicked and it has the correct movieId`() =
        runTest {
            // Given
            var receivedEffect: ActorDetailsScreenEffect? = null
            val job = launch {
                viewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            val movieId = 456L
            viewModel.onMovieCardClick(movieId)
            advanceUntilIdle()
            // Then
            assertThat(receivedEffect is ActorDetailsScreenEffect.NavigateToMovieDetails).isTrue()
            job.cancel()
        }

    @Test
    fun `onTvShowCardClick should Navigate To TvShow Details screen when clicked and it has the correct tvShowId`() =
        runTest {
            // Given
            var receivedEffect: ActorDetailsScreenEffect? = null
            val job = launch {
                viewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            val tvShowId = 789L
            viewModel.onTvShowCardClick(tvShowId)
            advanceUntilIdle()
            // Then
            assertThat(receivedEffect is ActorDetailsScreenEffect.NavigateToTvShowDetails).isTrue()
            job.cancel()
        }

    @Test
    fun `onSnackBarActionLabelClick should load data when it is clicked`() = runTest {
        // Given
        val expectedActor = createMockActor()
        coEvery { getActorInfoUseCase(actorId) } returns expectedActor
        val effects = mutableListOf<ActorDetailsScreenEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onSnackBarActionLabelClick()
        advanceUntilIdle()
        job.cancel()

        // Then
        val actualState = viewModel.uiState.value
        assertThat(actualState.actorInfo).isEqualTo(expectedActor.toActorInfoUI())
        assertThat(actualState.isLoading).isFalse()
    }

    @Test
    fun `getActorInfo should show no internet snackBar when NoInternetException is thrown`() = runTest {
        // Given
        coEvery { getActorInfoUseCase(actorId) } throws NoInternetException()
        val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()

        val job = launch {
            viewModel.snackBarState.collect {
                emittedSnackBarMessages.add(it.message)
            }
        }

        // When
        viewModel.onSnackBarActionLabelClick()
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NetworkError)
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
            Movie(
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
            )
        }

        private fun createMockTvShows() = createMockTvShowsWithSize(12)

        private fun createMockTvShowsWithSize(size: Int) = (1..size).map { index ->
            TvShow(
                id = index.toLong(),
                title = "TV Show $index",
                genres = listOf(Genre(1L, "Drama")),
                averageRating = 7.9,
                userRating = 8.1,
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