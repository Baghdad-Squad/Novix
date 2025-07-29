package com.baghdad.viewmodel.actorDetails

import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.domain.usecase.actor.GetActorInfoUseCase
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
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
    }

    private fun createViewModel() {
        viewModel = ActorDetailsViewModel(
            actorId = actorId,
            getActorInfoUseCase = getActorInfoUseCase,
            getActorMoviesUseCase = getActorMoviesUseCase,
            getActorTvShowUseCase = getActorTvShowUseCase,
            getActorGalleryUseCase = getActorGalleryUseCase,
            defaultDispatcher = testDispatcher
        )
    }
    @Test
    fun `init should handle empty gallery list`() = runTest {

        val mockActor = createMockActor()
        val mockMovies = createMockMovies()
        val mockTvShows = createMockTvShows()
        val emptyGallery = emptyList<String>()

        coEvery { getActorInfoUseCase(actorId) } returns mockActor
        coEvery { getActorMoviesUseCase(actorId) } returns mockMovies
        coEvery { getActorTvShowUseCase(actorId) } returns mockTvShows
        coEvery { getActorGalleryUseCase(actorId) } returns emptyGallery

        createViewModel()
        advanceUntilIdle()

        val currentState = viewModel.uiState.value
        assertEquals(0, currentState.gallery.size)
        assertFalse(currentState.isGalleryMoreThanTen)
    }

    @Test
    fun `onBackIconClick should send NavigateBack effect`() = runTest {

        setupSuccessfulMocks()
        createViewModel()
        advanceUntilIdle()

        viewModel.onBackIconClick()

    }

    @Test
    fun `onReadMoreBiographyClick should toggle isTextExpanded state`() = runTest {

        setupSuccessfulMocks()
        createViewModel()
        advanceUntilIdle()

        val initialState = viewModel.uiState.value.isTextExpanded

        viewModel.onReadMoreBiographyClick()

        val newState = viewModel.uiState.value.isTextExpanded
        assertEquals(!initialState, newState)
    }

    @Test
    fun `onViewAllGalleryClick should send NavigateToActorGallery effect`() = runTest {

        setupSuccessfulMocks()
        createViewModel()
        advanceUntilIdle()

        viewModel.onViewAllGalleryClick()

    }

    @Test
    fun `onViewAllTopMoviesPicksClick should send NavigateToActorTopMoviePicks effect`() = runTest {

        setupSuccessfulMocks()
        createViewModel()
        advanceUntilIdle()

        viewModel.onViewAllTopMoviesPicksClick()

    }

    @Test
    fun `onViewAllTopTvShowsClick should send NavigateToActorTopTvShowPicks effect`() = runTest {

        setupSuccessfulMocks()
        createViewModel()
        advanceUntilIdle()

        viewModel.onViewAllTopTvShowsClick()

    }

    @Test
    fun `onMovieCardClick should send NavigateToMovieDetails effect with correct movieId`() = runTest {

        setupSuccessfulMocks()
        createViewModel()
        advanceUntilIdle()
        val movieId = 456L

        viewModel.onMovieCardClick(movieId)

    }

    @Test
    fun `onTvShowCardClick should send NavigateToTvShowDetails effect with correct tvShowId`() = runTest {

        setupSuccessfulMocks()
        createViewModel()
        advanceUntilIdle()
        val tvShowId = 789L

        viewModel.onTvShowCardClick(tvShowId)

    }

    private fun setupSuccessfulMocks() {
        coEvery { getActorInfoUseCase(actorId) } returns createMockActor()
        coEvery { getActorMoviesUseCase(actorId) } returns createMockMovies()
        coEvery { getActorTvShowUseCase(actorId) } returns createMockTvShows()
        coEvery { getActorGalleryUseCase(actorId) } returns createMockGallery()
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

        private fun createMockGallery() = (1..15).map { index ->
            "/gallery_image_$index.jpg"
        }
    }
}