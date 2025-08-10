import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.domain.usecase.continueWatching.ObserveContinueWatchingUseCase
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.movie.GetPopularMoviesUseCase
import com.baghdad.domain.usecase.movie.GetUpcomingMoviesUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.domain.usecase.topRated.GetMovieTopRatingUseCase
import com.baghdad.domain.usecase.tvShow.GetPopularTvShowsUseCase
import com.baghdad.domain.usecase.userPreferences.GetAppLanguageUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.home.FakeHomeScreenData
import com.baghdad.viewmodel.home.HomeScreenEffect
import com.baghdad.viewmodel.home.HomeScreenState
import com.baghdad.viewmodel.home.HomeViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class HomeViewModelTest {

    private lateinit var getGenresUseCase: GetGenresUseCase
    private lateinit var observeContinueWatchingUseCase: ObserveContinueWatchingUseCase
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase
    private lateinit var getPopularTvShowsUseCase: GetPopularTvShowsUseCase
    private lateinit var getMovieTopRatingUseCase: GetMovieTopRatingUseCase
    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase
    private lateinit var getSavedListsUseCase: GetSavedListsUseCase
    private lateinit var addMovieToSavedListUseCase: AddMovieToSavedListUseCase
    private lateinit var createSavedListUseCase: CreateSavedListUseCase
    private lateinit var removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase
    private lateinit var getAppLanguageUseCase: GetAppLanguageUseCase

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getGenresUseCase = mockk(relaxed = true)
        getPopularMoviesUseCase = mockk(relaxed = true)
        getPopularTvShowsUseCase = mockk(relaxed = true)
        getMovieTopRatingUseCase = mockk(relaxed = true)
        getUpcomingMoviesUseCase = mockk(relaxed = true)
        isUserLoggedInUseCase = mockk(relaxed = true)
        getSavedListsUseCase = mockk(relaxed = true)
        addMovieToSavedListUseCase = mockk(relaxed = true)
        createSavedListUseCase = mockk(relaxed = true)
        removeMovieFromSavedListUseCase = mockk(relaxed = true)
        getAppLanguageUseCase = mockk(relaxed = true)
        observeContinueWatchingUseCase = mockk(relaxed = true)

        coEvery { getGenresUseCase.getMovieGenres() } returns FakeHomeScreenData.genres
        coEvery { getPopularMoviesUseCase.invoke() } returns FakeHomeScreenData.savableMovies
        coEvery { getPopularTvShowsUseCase.invoke() } returns FakeHomeScreenData.tvShows
        coEvery {
            getMovieTopRatingUseCase.invoke(any(), any()).data
        } returns FakeHomeScreenData.savableMovies
        coEvery {
            observeContinueWatchingUseCase.invoke()
        } returns flowOf(FakeHomeScreenData.continueWatchingItems)

        viewModel = createViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): HomeViewModel {
        return HomeViewModel(
            getGenresUseCase,
            observeContinueWatchingUseCase,
            getPopularMoviesUseCase,
            getPopularTvShowsUseCase,
            getMovieTopRatingUseCase,
            getUpcomingMoviesUseCase,
            isUserLoggedInUseCase,
            getSavedListsUseCase,
            addMovieToSavedListUseCase,
            createSavedListUseCase,
            removeMovieFromSavedListUseCase,
            getAppLanguageUseCase,
            defaultDispatcher = testDispatcher
        )
    }

    @Test
    fun `should have correct initial state when created`() {
        // Then
        val state = viewModel.uiState.value
        assertThat(state.popularItems).isEmpty()
        assertThat(state.topRatingItems).isEmpty()
        assertThat(state.upcomingItems).isEmpty()
        assertThat(state.continueWatchingItems).isEmpty()
        assertThat(state.isPopularLoading).isTrue()
        assertThat(state.isTopRatingLoading).isTrue()
        assertThat(state.isUpcomingMoviesLoading).isTrue()
        assertThat(state.isUpcomingGenresLoading).isTrue()
        assertThat(state.selectedUpcomingGenreId).isNull()
    }

    @Test
    fun `should have empty popular items when fetching with empty popular movies and tv shows`() =
        runTest {
            // Given
            coEvery { getPopularMoviesUseCase.invoke() } returns emptyList()
            coEvery { getPopularTvShowsUseCase.invoke() } returns emptyList()

            // When
            viewModel = createViewModel()
            advanceUntilIdle()

            // Then
            assertThat(viewModel.uiState.value.popularItems).isEmpty()
        }

    @Test
    fun `should have empty top rating items when fetching with empty top rating movies`() =
        runTest {
            // Given
            coEvery { getMovieTopRatingUseCase.invoke(any(), any()).data } returns emptyList<SavableMovie>()

            // When
            viewModel = createViewModel()
            advanceUntilIdle()

            // Then
            assertThat(viewModel.uiState.value.topRatingItems).isEmpty()
        }

    @Test
    fun `should have empty continue watching when no items observed`() =
        runTest {
            // Given
            coEvery { observeContinueWatchingUseCase.invoke() } returns flowOf(emptyList())

            // When
            viewModel = createViewModel()
            advanceUntilIdle()

            // Then
            assertThat(viewModel.uiState.value.continueWatchingItems).isEmpty()
        }

    @Test
    fun `should set popular loading false when popular items finished loading`() = runTest {
        // Given
        coEvery { getPopularMoviesUseCase.invoke() } returns emptyList()
        coEvery { getPopularTvShowsUseCase.invoke() } returns emptyList()

        val states = mutableListOf<HomeScreenState>()
        viewModel = createViewModel()

        // When
        val job = launch { viewModel.uiState.collect { states.add(it) } }
        advanceUntilIdle()

        // Then
        assertThat(states.last().isPopularLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `should update upcoming movies when genre is selected`() = runTest {
        // Given
        val savableMovies = listOf(
            SavableMovie(
                movie = Movie(
                    id = 10,
                    title = "Test",
                    genres = listOf(Genre(1, "Drama")),
                    averageRating = 1.4,
                    userRating = 1.5,
                    releaseDate = LocalDate.parse("2023-01-01"),
                    overview = "Test",
                    posterImageURL = "Test",
                    trailerURL = "Test",
                    runtimeMinutes = 5
                ),
                isSaved = false,
                listId = null
            )
        )
        coEvery { getUpcomingMoviesUseCase.invoke(any()) } returns savableMovies

        // When
        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.onUpcomingGenreSelected(HomeScreenState.GenreUiState(1, "Drama"))
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.upcomingItems).hasSize(1)
        assertThat(state.upcomingItems.first().id).isEqualTo(10)
    }

    @Test
    fun `should not reload upcoming items when same genre is selected`() = runTest {
        // Given
        viewModel = createViewModel()
        advanceUntilIdle()
        val initialState = viewModel.uiState.value
        val genreId = initialState.selectedUpcomingGenreId ?: 1L

        // When
        viewModel.onUpcomingGenreSelected(HomeScreenState.GenreUiState(genreId, "Drama"))
        advanceUntilIdle()

        // Then
        val newState = viewModel.uiState.value
        assertThat(newState.upcomingItems).isEqualTo(initialState.upcomingItems)
        assertThat(newState.selectedUpcomingGenreId).isEqualTo(genreId)
    }

    @Test
    fun `should emit NavigateToMovieDetails when popular movie item clicked`() = runTest {
        // Given
        val effects = mutableListOf<HomeScreenEffect>()
        viewModel = createViewModel()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onPopularItemClicked(
            HomeScreenState.PopularItemUiState(
                id = 1,
                type = HomeScreenState.PopularItemUiState.Type.MOVIE
            )
        )
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(HomeScreenEffect.NavigateToMovieDetails(1))
        job.cancel()
    }

    @Test
    fun `should emit NavigateToTvShowDetails when popular tv show item clicked`() = runTest {
        // Given
        val effects = mutableListOf<HomeScreenEffect>()
        viewModel = createViewModel()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onPopularItemClicked(
            HomeScreenState.PopularItemUiState(
                id = 2,
                type = HomeScreenState.PopularItemUiState.Type.TV_SHOW
            )
        )
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(HomeScreenEffect.NavigateToTvShowDetails(2))
        job.cancel()
    }

    @Test
    fun `should emit NavigateToMovies when onMoviesClicked`() = runTest {
        // Given
        val effects = mutableListOf<HomeScreenEffect>()
        viewModel = createViewModel()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onMoviesClicked()
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(HomeScreenEffect.NavigateToMovies)
        job.cancel()
    }

    @Test
    fun `should emit NavigateToTvShows when onTvShowsClicked`() = runTest {
        // Given
        val effects = mutableListOf<HomeScreenEffect>()
        viewModel = createViewModel()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onTvShowsClicked()
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(HomeScreenEffect.NavigateToTvShows)
        job.cancel()
    }

    @Test
    fun `should emit NavigateToActors when onActorsClicked`() = runTest {
        // Given
        val effects = mutableListOf<HomeScreenEffect>()
        viewModel = createViewModel()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onActorsClicked()
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(HomeScreenEffect.NavigateToActors)
        job.cancel()
    }

    @Test
    fun `should emit NavigateToMovieDetails when top rating item clicked`() = runTest {
        // Given
        val effects = mutableListOf<HomeScreenEffect>()
        viewModel = createViewModel()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onTopRatingItemClicked(HomeScreenState.TopRatingItemUiState(id = 3))
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(HomeScreenEffect.NavigateToMovieDetails(3))
        job.cancel()
    }

    @Test
    fun `should emit NavigateToTopRating when onViewAllTopRatingClicked`() = runTest {
        // Given
        val effects = mutableListOf<HomeScreenEffect>()
        viewModel = createViewModel()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onViewAllTopRatingClicked()
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(HomeScreenEffect.NavigateToTopRating)
        job.cancel()
    }

    @Test
    fun `should emit NavigateToMovieDetails when continue watching item clicked`() = runTest {
        // Given
        val effects = mutableListOf<HomeScreenEffect>()
        viewModel = createViewModel()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onContinueWatchingItemClicked(
            HomeScreenState.ContinueWatchingItemUiState(
                id = 5,
                contentType = HomeScreenState.ContinueWatchingItemUiState.ContentType.MOVIE
            )
        )
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(HomeScreenEffect.NavigateToMovieDetails(5))
        job.cancel()
    }

    @Test
    fun `should emit NavigateToContinueWatching when onViewAllContinueWatchingClicked`() = runTest {
        // Given
        val effects = mutableListOf<HomeScreenEffect>()
        viewModel = createViewModel()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onViewAllContinueWatchingClicked()
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(HomeScreenEffect.NavigateToContinueWatching)
        job.cancel()
    }

    @Test
    fun `should emit NavigateToMovieDetails when upcoming item clicked`() = runTest {
        // Given
        val effects = mutableListOf<HomeScreenEffect>()
        viewModel = createViewModel()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        // When
        viewModel.onUpcomingItemClicked(HomeScreenState.UpcomingItemUiState(id = 9))
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(HomeScreenEffect.NavigateToMovieDetails(9))
        job.cancel()
    }

    @Test
    fun `should update upcomingGenres when getMovieGenres succeeds`() = runTest {
        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.upcomingGenres).isNotEmpty()
    }

    @Test
    fun `should set top rating loading false when top rating movies finished loading`() =
        runTest {
            // Given
            coEvery { getMovieTopRatingUseCase.invoke(any(), any()).data } returns emptyList<SavableMovie>()

            // When
            viewModel = createViewModel()
            advanceUntilIdle()

            // Then
            assertThat(viewModel.uiState.value.isTopRatingLoading).isFalse()
        }

    @Test
    fun `should set upcoming loading false when upcoming movies finished loading`() = runTest {
        // Given
        coEvery { getUpcomingMoviesUseCase.invoke(any()) } returns emptyList()

        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.isUpcomingMoviesLoading).isFalse()
    }

    @Test
    fun `should set upcoming genres loading false when movie genres finished loading`() = runTest {
        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.isUpcomingGenresLoading).isFalse()
    }

    @Test
    fun `should not crash when onPopularItemSaveClicked called`() = runTest {
        // When
        viewModel.onPopularItemSaveClicked(
            HomeScreenState.PopularItemUiState(
                id = 1,
                type = HomeScreenState.PopularItemUiState.Type.MOVIE
            )
        )
        advanceUntilIdle()
    }

    @Test
    fun `should not crash when onTopRatingItemSaveClicked called`() = runTest {
        // When
        viewModel.onTopRatingItemSaveClicked(HomeScreenState.TopRatingItemUiState(id = 2))
        advanceUntilIdle()
    }

    @Test
    fun `should not crash when onContinueWatchingItemSaveClicked called`() = runTest {
        // When
        viewModel.onContinueWatchingItemSaveClicked(
            HomeScreenState.ContinueWatchingItemUiState(
                id = 3,
                contentType = HomeScreenState.ContinueWatchingItemUiState.ContentType.MOVIE
            )
        )
        advanceUntilIdle()
    }

    @Test
    fun `should not crash when onUpcomingItemSaveClicked called`() = runTest(testDispatcher) {
        // When
        viewModel.onUpcomingItemSaveClicked(HomeScreenState.UpcomingItemUiState(id = 4))
        advanceUntilIdle()
    }

    @Test
    fun `should return UnknownError using reflection`() {
        val method = HomeViewModel::class.java.getDeclaredMethod(
            "mapThrowableToErrorMessage",
            Throwable::class.java
        )

        method.isAccessible = true

        val viewModel = createViewModel()

        // When
        val result = method.invoke(viewModel, Throwable("test")) as BaseSnackBarMessage

        // Then
        assertThat(result).isEqualTo(BaseSnackBarMessage.UnknownError)
    }
}