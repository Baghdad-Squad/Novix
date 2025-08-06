package com.baghdad.viewmodel.movieDetails

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.usecase.continueWatching.AddContinueWatchingUseCase
import com.baghdad.domain.usecase.login.IsLoggedInUseCase
import com.baghdad.domain.usecase.movie.AddMovieRateUseCase
import com.baghdad.domain.usecase.movie.GetMovieAccountStatesUseCase
import com.baghdad.domain.usecase.movie.GetMovieCastMembersUseCase
import com.baghdad.domain.usecase.movie.GetMovieDetailsUseCase
import com.baghdad.domain.usecase.movie.GetMovieGalleryUseCase
import com.baghdad.domain.usecase.movie.GetSimilarMoviesUseCase
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.shared.BottomSheetType
import com.baghdad.viewmodel.util.roundToFirstDecimal
import com.baghdad.viewmodel.util.toDDMMYYYYFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getCastsInfoUseCase: GetMovieCastMembersUseCase,
    private val getMovieImagesUseCase: GetMovieGalleryUseCase,
    private val getMoreLikeThisPosterImageUseCase: GetSimilarMoviesUseCase,
    private val addContinueWatchingUseCase: AddContinueWatchingUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val addMovieRateUseCase: AddMovieRateUseCase,
    private val getMovieAccountStatesUseCase: GetMovieAccountStatesUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<MovieDetailsState, MovieDetailsEffect>(MovieDetailsState()),
    MovieDetailsInteractionListener {

    private val movieId: Long = checkNotNull(savedStateHandle["movieId"])

    init {
        loadInitData()
    }

    private fun loadInitData() {
        getMovieGallery()
        getMovieDetails()
        getCastMembers()
        getMoreLikeThisShow()
        getMovieAccountStates()
        isUserLoggedIn()
    }

    override fun onSaveCurrentMovieClick() {
        // TODO: save logic
    }

    override fun onSaveMoreLikeThisMedia(id: Long) {
        tryToExecute(
            callee = { currentState.moreLikeThisMovie.firstOrNull { it.id == id }?.id ?: 1L },
            onSuccess = ::onSaveMoreLikeThisMediaSuccess,
            onStart = ::onMoreLikeThisStarted,
            dispatcher = ioDispatcher,
            onFinally = ::onMoreLikeThisFinished
        )
    }

    private fun onMoreLikeThisStarted() {
        updateState { state -> state.copy(isMoreLikeThisMovieLoading = true) }
    }

    private fun onMoreLikeThisFinished() {
        updateState { state -> state.copy(isMoreLikeThisMovieLoading = false) }
    }

    private fun onSaveMoreLikeThisMediaSuccess(id: Long) {
        updateState { state ->
            val updatedMovies = state.moreLikeThisMovie.map {
                if (it.id == id) {
                    it.copy(isSaved = !it.isSaved)
                } else {
                    it
                }
            }
            state.copy(
                moreLikeThisMovie = updatedMovies,
            )
        }
    }

    override fun onExtendOverviewClick() {
        updateState { state ->
            state.copy(
                isExtendText = !state.isExtendText
            )
        }
    }

    override fun onCategoryClick(categoryId: Long) {
        sendEffect(MovieDetailsEffect.NavigateToCategory(categoryId))
    }

    override fun onBackClicked() {
        sendEffect(MovieDetailsEffect.NavigateBack)
    }


    override fun onActorClick(id: Long) {
        sendEffect(MovieDetailsEffect.NavigateToActorDetails(id))
    }

    override fun onReviewClick() {
        sendEffect(MovieDetailsEffect.NavigateToReviewDetails(movieId))
    }

    override fun onMovieClick(id: Long) {
        sendEffect(MovieDetailsEffect.NavigateToMovie(id))
    }

    override fun onBackClick() {
        sendEffect(MovieDetailsEffect.NavigateBack)
    }

    override fun onClickPlayTrailer() {
        sendEffect(MovieDetailsEffect.OpenYoutubeLink(currentState.movieTrailerURL))
    }

    private fun onError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    private fun showNoInternetSnackBar() {
        updateState {
            it.copy(
                isMovieDetailsLoading = true,
                isMovieGalleryLoading = true,
                isCastMemberLoading = true,
                isMoreLikeThisMovieLoading = true,
            )
        }
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }

    override fun onClickStarButton() {
        updateState {
            it.copy(
                ratingStatus = it.ratingStatus.copy(
                    isBottomSheetVisible = true,
                )
            )
        }
    }

    private fun isUserLoggedIn() {
        tryToExecute(
            callee = { isLoggedInUseCase() },
            dispatcher = ioDispatcher,
            onSuccess = ::onIsUserLoggedInSuccess,
            onError = ::onError
        )
    }


    private fun onIsUserLoggedInSuccess(isLoggedIn: Boolean) {
        val newBottomSheetType = if (isLoggedIn) {
            BottomSheetType.ShowRating
        } else {
            BottomSheetType.RequireLogin
        }

        updateState {
            it.copy(
                ratingStatus = it.ratingStatus.copy(
                    bottomSheetType = newBottomSheetType
                )
            )
        }
    }

    override fun onRatingChanged(rating: Int) {
        updateState {
            it.copy(
                userRating = rating
            )
        }
    }

    override fun onDismissRatingBottomSheet() {
        updateState {
            it.copy(
                ratingStatus = it.ratingStatus.copy(
                    isBottomSheetVisible = false,
                    bottomSheetType = BottomSheetType.Hidden
                )
            )
        }
    }

    override fun onLoginClick() {
        sendEffect(MovieDetailsEffect.NavigateToLogin)
    }


    override fun onClickSubmitRating(rating: Int) {
        tryToExecute(
            callee = { addMovieRateUseCase(movieId, rating) },
            onSuccess = { onSubmitRatingSuccess() },
            dispatcher = ioDispatcher,
            onError = ::onError
        )
    }

    private fun onSubmitRatingSuccess() {
        updateState {
            it.copy(
                ratingStatus = it.ratingStatus.copy(
                    isBottomSheetVisible = false,
                    bottomSheetType = BottomSheetType.Hidden,
                ),
                isRated = true
            )
        }
        showSnackBar(
            message = BaseSnackBarMessage.ItemRateSuccessfully,
            isSuccess = true
        )
    }

    private fun getMovieAccountStates() {
        tryToExecute(
            callee = { getMovieAccountStatesUseCase(movieId) },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetMovieAccountStatesSuccess,
            onError = ::onError
        )
    }

    private fun onGetMovieAccountStatesSuccess(accountStates: MediaAccountStates) {
        updateState {
            it.copy(
                isRated = accountStates.isMediaRated,
            )
        }
    }


    override fun onSnackBarActionLabelClick() {
        loadInitData()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

    private fun getMovieGallery() {
        tryToExecute(
            callee = {
                getMovieImagesUseCase(movieId = movieId)
            },
            onSuccess = ::onGetMovieGallerySuccess,
            onStart = ::onGetMovieGalleryStarted,
            dispatcher = ioDispatcher,
            onFinally = ::onGetMovieGalleryFinished,
            onError = ::onError
        )
    }

    private fun onGetMovieGalleryStarted() {
        updateState { state -> state.copy(isMovieGalleryLoading = true) }
    }

    private fun onGetMovieGalleryFinished() {
        updateState { state -> state.copy(isMovieGalleryLoading = false) }
    }

    private fun onGetMovieGallerySuccess(images: List<String>) {
        updateState { state ->
            state.copy(
                movieImages = images,
            )
        }
    }

    private fun getMovieDetails() {
        tryToExecute(
            callee = { getMovieDetailsUseCase(movieId) },
            onSuccess = ::onGetMovieDetailsSuccess,
            dispatcher = ioDispatcher,
            onStart = ::onGetMovieDetailsStarted,
            onFinally = ::onFinallyAndAddToContinueWatching,
            onError = ::onError
        )
    }

    private fun onGetMovieDetailsStarted() {
        updateState { state -> state.copy(isMovieDetailsLoading = true) }
    }

    private fun onGetMovieDetailsSuccess(details: Movie) {
        updateState { state ->
            state.copy(
                movieName = details.title,
                movieTrailerURL = details.trailerURL,
                overView = details.overview,
                rating = details.averageRating.roundToFirstDecimal(),
                duration = details.runtimeMinutes,
                posterImageURL = details.posterImageURL,
                date = details.releaseDate.toDDMMYYYYFormat(),
                isSaved = state.isSaved,
                categories = details.genres.map {
                    MovieDetailsState.CategoryUiState(
                        id = it.id,
                        name = it.name
                    )
                }
            )
        }
    }


    private fun getCastMembers() {
        tryToExecute(
            callee = { getCastsInfoUseCase(movieId) },
            onSuccess = { actors ->
                onGetMovieCastSuccess(
                    actors = actors.map { actor ->
                        MovieDetailsState.ActorCardInfo(
                            name = actor.actor.name,
                            imageUrl = actor.actor.profilePictureURL,
                            characterName = actor.characterName,
                            id = actor.actor.id.toInt()
                        )
                    }
                )
            },
            onStart = ::onGetCastMembersStarted,
            onFinally = ::onGetCastMembersFinally,
            onError = ::onError
        )
    }

    private fun onGetCastMembersStarted() {
        updateState { state -> state.copy(isCastMemberLoading = true) }
    }

    private fun onGetCastMembersFinally() {
        updateState { state -> state.copy(isCastMemberLoading = false) }
    }


    private fun getMoreLikeThisShow() {
        tryToExecute(
            callee = { getMoreLikeThisPosterImageUseCase(movieId) },
            onSuccess = ::onGetMovieMoreLikeThisSuccess,
            onStart = ::onGetMovieMoreLikeThisStarted,
            onFinally = ::onGetMovieMoreLikeThisFinished,
            onError = ::onError,
            dispatcher = ioDispatcher,
        )
    }

    private fun onGetMovieMoreLikeThisStarted() {
        updateState { state -> state.copy(isMoreLikeThisMovieLoading = true) }
    }

    private fun onGetMovieMoreLikeThisFinished() {
        updateState { state -> state.copy(isMoreLikeThisMovieLoading = false) }
    }

    private fun onGetMovieMoreLikeThisSuccess(movies: List<Movie>) {
        hideSnackBar()
        updateState { state ->
            state.copy(
                moreLikeThisMovie = movies.map { movie ->
                    MovieDetailsState.MoreLikeThisMovie(
                        imageUrl = movie.posterImageURL,
                        id = movie.id,
                        isSaved = false
                    )
                },
            )
        }
    }

    private fun onGetMovieCastSuccess(actors: List<MovieDetailsState.ActorCardInfo>) {
        updateState { state ->
            state.copy(
                castMembers = actors,
            )
        }
    }


    private fun onFinallyAndAddToContinueWatching() {
        updateState { state -> state.copy(isMovieDetailsLoading = false) }
        addToContinueWatching()
    }


    private fun addToContinueWatching() {
        tryToExecute(
            dispatcher = ioDispatcher,
            callee = {
                addContinueWatchingUseCase(
                    movieId, currentState.categories.map { it.id },
                    contentImageUrl = currentState.posterImageURL,
                    contentType = ContinueWatching.ContentType.MOVIE,
                )
            },
        )
    }
}


