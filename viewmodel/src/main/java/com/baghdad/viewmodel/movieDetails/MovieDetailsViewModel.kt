package com.baghdad.viewmodel.movieDetails

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.savedList.SavedMovie
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
import com.baghdad.domain.usecase.userWatchedMedia.AddUserWatchedMediaUseCase
import com.baghdad.entity.savedList.SavedList
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.shared.AddToListBottomSheetState
import com.baghdad.viewmodel.shared.BottomSheetType
import com.baghdad.viewmodel.shared.SavedListUiState
import com.baghdad.viewmodel.shared.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getCastsInfoUseCase: GetMovieCastMembersUseCase,
    private val getMovieImagesUseCase: GetMovieGalleryUseCase,
    private val getMoreLikeThisPosterImageUseCase: GetSimilarMoviesUseCase,
    private val addUserWatchedMediaUseCase: AddUserWatchedMediaUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val addMovieRateUseCase: AddMovieRateUseCase,
    private val getMovieAccountStatesUseCase: GetMovieAccountStatesUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val addMovieToSavedListUseCase: AddMovieToSavedListUseCase,
    private val getSavedListsUseCase: GetSavedListsUseCase,
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase,
    private val createSavedListUseCase: CreateSavedListUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<MovieDetailsState, MovieDetailsEffect>(MovieDetailsState()),
    MovieDetailsInteractionListener {

    private val movieId: Long = checkNotNull(savedStateHandle["movieId"])

    init {
        loadInitData()
    }

    private fun loadInitData() {
            checkIfUserIsLoggedIn()
            getMovieGallery()
            getMovieDetails()
            getCastMembers()
            getMoreLikeThisShow()
            isUserLoggedIn()
    }

    override fun onSaveCurrentMovieClick() {
        onSaveButtonClicked(
            listId = currentState.savedListId,
            itemId = movieId,
            isSaved = uiState.value.isSaved
        )
    }


    private fun onAddItemToListSuccess() {
        refreshSavedItems()
        onSaveToListBottomSheetDismiss()
        showItemSavedSuccessfullySnackBar()

    }

    private fun refreshSavedItems() {
        getMovieDetails()
        getMoreLikeThisShow()
        getUserSavedLists()
    }

    private fun onAddItemToListStart() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isLoading = true,
                    ),
            )
        }
    }

    private fun onAddItemToListFinished() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isLoading = false,
                    ),
            )
        }
    }

    override fun onSaveMoreLikeThisMedia(movie: MovieDetailsState.MoreLikeThisMovie) {
        onSaveButtonClicked(listId = movie.savedListId, itemId = movie.id, isSaved = movie.isSaved)
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
            callee = { isUserLoggedInUseCase() },
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
                    bottomSheetType = newBottomSheetType,
                ),
                isRated = it.isRated && isLoggedIn,
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
                ),
                userRating = 0
            )
        }
    }

    override fun onLoginClick() {
        sendEffect(MovieDetailsEffect.NavigateToLogin)
    }

    private fun showItemSavedSuccessfullySnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.SavedItemSuccessfully,
            isSuccess = true,
        )
    }

    private fun onSaveButtonClicked(
        listId: Long,
        itemId: Long,
        isSaved: Boolean,
    ) {
        if (isSaved) {
            removeSavedItem(listId = listId, itemId = itemId)
        } else {
            updateState {
                it.copy(
                    addToListBottomSheetState =
                        it.addToListBottomSheetState.copy(
                            isVisible = true,
                            selectedItemId = itemId,
                            selectedListId = null,
                        )
                )
            }
        }
    }

    private fun removeSavedItem(
        listId: Long,
        itemId: Long,
    ) {
        tryToExecute(
            callee = { removeMovieFromSavedListUseCase(listId = listId, movieId = itemId) },
            onSuccess = { onRemoveSavedItemSuccess() },
            dispatcher = ioDispatcher,
            onFinally = ::onRemoveSavedItemFinished,
        )
    }

    private fun onRemoveSavedItemSuccess() {
        refreshSavedItems()
        showItemRemovedSuccessfullySnackBar()
    }

    private fun showItemRemovedSuccessfullySnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.RemovedItemSuccessfully,
            isSuccess = true,
        )
    }

    private fun onRemoveSavedItemFinished() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isVisible = false
                    )
            )
        }
    }

    override fun onCreateNewListClicked() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isVisible = true,
                    ),
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isVisible = false,
                    ),
            )
        }
    }

    override fun onSaveToListBottomSheetDismiss() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    AddToListBottomSheetState(
                        savedLists = it.addToListBottomSheetState.savedLists,
                    ),
            )
        }
    }

    override fun onListSelected(listId: Long) {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        selectedListId = listId,
                    ),
            )
        }
    }

    override fun onSaveItemToListClicked() {
        tryToExecute(
            callee = {
                addMovieToSavedListUseCase(
                    listId =
                        currentState.addToListBottomSheetState.selectedListId
                            ?: return@tryToExecute,
                    movieId = currentState.addToListBottomSheetState.selectedItemId,
                )
            },
            onError = { onAddItemToListError() },
            onSuccess = { onAddItemToListSuccess() },
            dispatcher = ioDispatcher,
            onStart = ::onAddItemToListStart,
            onFinally = ::onAddItemToListFinished,
        )
    }

    private fun onAddItemToListError() {
        showNoInternetSnackBarWithoutRetry()
    }

    private fun showNoInternetSnackBarWithoutRetry() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            isSuccess = false,
        )
    }

    override fun onCreatedListNameChanged(name: String) {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        listName = name,
                    ),
            )
        }
    }

    private fun onCreateListSuccess() {
        onCreateListBottomSheetDismiss()
        getUserSavedLists()
    }

    private fun onCreateListStart() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isLoading = true,
                    ),
            )
        }
    }

    private fun onCreateListFinished() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isLoading = false,
                    ),
            )
        }
    }

    override fun onCreateListBottomSheetDismiss() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isVisible = false,
                        listName = "",
                        isLoading = false,
                    ),
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isVisible = true,
                    ),
            )
        }
    }

    override fun onCreateListBottomSheetAddClick() {
        tryToExecute(
            callee = {
                createSavedListUseCase(
                    title = currentState.addListBottomSheetState.listName.trim(),
                )
            },
            onSuccess = { onCreateListSuccess() },
            dispatcher = ioDispatcher,
            onStart = ::onCreateListStart,
            onFinally = ::onCreateListFinished,
        )
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

    private fun onGetMovieAccountStatesSuccess(isMovieRated: Boolean) {
        updateState {
            it.copy(
                isRated = isMovieRated,
            )
        }
    }

    override fun onSnackBarActionLabelClick() {
        hideSnackBar()
        loadInitData()
    }

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

    private fun checkIfUserIsLoggedIn() {
        tryToExecute(
            callee = { isUserLoggedInUseCase() },
            onSuccess = ::onCheckIfUserIsLoggedInSuccess,
            dispatcher = ioDispatcher,
        )
    }

    private fun onCheckIfUserIsLoggedInSuccess(isLoggedIn: Boolean) {
        updateState {
            it.copy(isUserLoggedIn = isLoggedIn)
        }
        if (isLoggedIn) {
            getUserSavedLists()
        }
    }

    private fun getUserSavedLists() {
        collectPagingFlow(
            loadData = { page ->
                getSavedListsUseCase(
                    page = page,
                    pageSize = DEFAULT_PAGE_SIZE,
                )
            },
            onInitialLoadError = ::onError,
            pageSize = DEFAULT_PAGE_SIZE,
            mapEntityToUiState = SavedList::toUiState,
            onFlowCreated = ::onGetSavedListFlowCreated,
        )
    }

    private fun onGetSavedListFlowCreated(flow: Flow<PagingData<SavedListUiState>>) {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        savedLists = flow,
                    ),
            )
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

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

    private fun onGetMovieDetailsSuccess(details: SavedMovie) {
        updateState(details.toMovieDetailsStateUpdate())
    }

    private fun getCastMembers() {
        tryToExecute(
            callee = { getCastsInfoUseCase(movieId) },
            onSuccess = { actors ->
                onGetMovieCastSuccess(
                    actors = actors.map { it.toActorCardInfo() }
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

    private fun onGetMovieMoreLikeThisSuccess(savedMovies: List<SavedMovie>) {
        updateState { state ->
            state.copy(moreLikeThisMovie = savedMovies.map { it.toMoreLikeThisMovie() })
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
                addUserWatchedMediaUseCase(
                    movieId, currentState.categories.map { it.id },
                    contentImageUrl = currentState.posterImageURL,
                    contentType = UserWatchedMedia.ContentType.MOVIE,
                )
            },
        )
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}