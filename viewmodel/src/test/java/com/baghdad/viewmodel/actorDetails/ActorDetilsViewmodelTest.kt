package com.baghdad.viewmodel.actorDetails

import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.domain.usecase.actor.GetActorInfoUseCase
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    }

    private fun createViewModel() {
        viewModel = ActorDetailsViewModel(
            actorId = actorId,
            getActorInfoUseCase = getActorInfoUseCase,
            getActorMoviesUseCase = getActorMoviesUseCase,
            getActorTvShowUseCase = getActorTvShowUseCase,
            getActorGalleryUseCase = getActorGalleryUseCase
        )
    }

    @Test
    fun `uiState should have empty gallery when getActorGalleryUseCase returns empty list`() =
        runTest {
            // Given
            coEvery { getActorInfoUseCase(actorId) } returns createMockActor()
            coEvery { getActorMoviesUseCase(actorId) } returns createMockMovies()
            coEvery { getActorTvShowUseCase(actorId) } returns createMockTvShows()
            coEvery { getActorGalleryUseCase(actorId) } returns emptyList()

            // When
            createViewModel()
            advanceUntilIdle()

            // Then
            assertThat(viewModel.uiState.value.gallery).isEmpty()
            assertThat(viewModel.uiState.value.isGalleryMoreThanTen).isFalse()
        }

    @Test
    fun `onBackIconClick should send NavigateBack effect when back icon is clicked`() = runTest {
        // Given
        setupSuccessfulMocks()
        createViewModel()
        advanceUntilIdle()

        // When
        viewModel.onBackIconClick()

    }

    @Test
    fun `isTextExpanded should be toggled when onReadMoreBiographyClick is called`() = runTest {
        // Given
        setupSuccessfulMocks()
        createViewModel()
        advanceUntilIdle()
        val initialState = viewModel.uiState.value.isTextExpanded

        // When
        viewModel.onReadMoreBiographyClick()

        // Then
        assertThat(viewModel.uiState.value.isTextExpanded).isNotEqualTo(initialState)
    }

    @Test
    fun `NavigateToActorGallery effect should be sent when onViewAllGalleryClick is called`() =
        runTest {
            // Given
            setupSuccessfulMocks()
            createViewModel()
            advanceUntilIdle()

            // When
            viewModel.onViewAllGalleryClick()

        }

    @Test
    fun `NavigateToActorTopMoviePicks effect should be sent when onViewAllTopMoviesPicksClick is called`() =
        runTest {
            // Given
            setupSuccessfulMocks()
            createViewModel()
            advanceUntilIdle()

            // When
            viewModel.onViewAllTopMoviesPicksClick()
        }

    @Test
    fun `NavigateToActorTopTvShowPicks effect should be sent when onViewAllTopTvShowsClick is called`() =
        runTest {
            // Given
            setupSuccessfulMocks()
            createViewModel()
            advanceUntilIdle()

            // When
            viewModel.onViewAllTopTvShowsClick()
        }

    @Test
    fun `NavigateToMovieDetails effect should be sent with correct movieId when onMovieCardClick is called`() =
        runTest {
            // Given
            setupSuccessfulMocks()
            createViewModel()
            advanceUntilIdle()
            val movieId = 456L

            // When
            viewModel.onMovieCardClick(movieId)
        }

    @Test
    fun `NavigateToTvShowDetails effect should be sent with correct tvShowId when onTvShowCardClick is called`() =
        runTest {
            // Given
            setupSuccessfulMocks()
            createViewModel()
            advanceUntilIdle()
            val tvShowId = 789L

            // When
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