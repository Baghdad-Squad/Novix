package com.baghdad.viewmodel.actorDetails

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.domain.usecase.actor.GetActorInfoUseCase
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.savedList.SavedList
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.shared.AddToListBottomSheetState
import com.baghdad.viewmodel.shared.SavedListUiState
import com.baghdad.viewmodel.shared.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ActorDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getActorInfoUseCase: GetActorInfoUseCase,
    private val getActorMoviesUseCase: GetActorMoviesUseCase,
    private val getActorTvShowUseCase: GetActorTvShowUseCase,
    private val getActorGalleryUseCase: GetActorGalleryUseCase,
    private val addMovieToSavedListUseCase: AddMovieToSavedListUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val getSavedListsUseCase: GetSavedListsUseCase,
    private val createSavedListUseCase: CreateSavedListUseCase,
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) :
    BaseViewModel<ActorDetailsScreenState, ActorDetailsScreenEffect>(ActorDetailsScreenState()),
    ActorDetailsInteractionListener {

    private val actorId: Long = checkNotNull(savedStateHandle["actorId"])

    init {
        loadData()
    }

    private fun loadData() {
        checkIfUserIsLoggedIn()
        getActorInfo(actorId)
        getActorGallery(actorId)
        getActorMovies(actorId)
        getActorTvShows(actorId)
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

    private fun getActorInfo(actorId: Long) {
        tryToExecute(
            callee = {
                getActorInfoUseCase(actorId)
            },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetActorInfoSuccess,
            onStart = ::onGetActorInfoStart,
            onError = ::onError,
            onFinally = ::onGetActorInfoFinally,
        )
    }

    private fun onGetActorInfoSuccess(actor: Actor) {
        updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                actorInfo = actor.toActorInfoUI(),
            )
        }
    }

    private fun onGetActorInfoStart() {
        updateState { it.copy(isActorInfoLoading = true) }
    }

    private fun onGetActorInfoFinally() {
        updateState { it.copy(isActorInfoLoading = false) }
    }

    private fun getActorGallery(actorId: Long) {
        tryToExecute(
            callee = { getActorGalleryUseCase(actorId) },
            onSuccess = ::onGetActorGallerySuccess,
            onStart = ::onGetActorGalleryStart,
            onError = ::onError,
            onFinally = ::onGetActorGalleryFinally,
        )
    }

    private fun onGetActorGallerySuccess(gallery: List<String>) {
        updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                gallery = gallery.take(MAX_GALLERY_IMAGES),
            )
        }
    }

    private fun onGetActorGalleryStart() {
        updateState { it.copy(isGalleryLoading = true) }
    }

    private fun onGetActorGalleryFinally() {
        updateState { it.copy(isGalleryLoading = false) }
    }

    private fun getActorMovies(actorId: Long) {
        tryToExecute(
            callee = { getActorMoviesUseCase(actorId) },
            onSuccess = ::onGetActorMoviesSuccess,
            onStart = ::onGetActorMoviesStart,
            onError = ::onError,
            onFinally = ::onGetActorMoviesFinally,
        )
    }

        private fun onGetActorMoviesSuccess(movies: List<SavableMovie>) {
            updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                topMoviesPicks = movies.take(MAX_TOP_MOVIE_PICKS).map { it.toMovieUI() },
            )
        }
    }

    private fun onGetActorMoviesStart() {
        updateState { it.copy(isTopMoviePicksLoading = true) }
    }

    private fun onGetActorMoviesFinally() {
        updateState { it.copy(isTopMoviePicksLoading = false) }
    }

    private fun getActorTvShows(actorId: Long) {
        tryToExecute(
            callee = { getActorTvShowUseCase(actorId) },
            onSuccess = ::onGetActorTvShowsSuccess,
            onStart = ::onGetActorTvShowsStart,
            onError = ::onError,
            onFinally = ::onGetActorTvShowsFinally,
        )
    }

    private fun onGetActorTvShowsSuccess(tvShows: List<TvShow>) {
        hideSnackBar()
        updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                topTvShowsPicks = tvShows.take(MAX_TOP_TV_SHOW_PICKS).map { it.toTvShowUI() },
            )
        }
    }

    private fun onGetActorTvShowsStart() {
        updateState { it.copy(isTopTvShowPicksLoading = true) }
    }

    private fun onGetActorTvShowsFinally() {
        updateState { it.copy(isTopTvShowPicksLoading = false) }
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

    override fun onBackIconClick() {
        sendEffect(ActorDetailsScreenEffect.NavigateBack)
    }

    override fun onReadMoreBiographyClick() {
        updateState { it.copy(isTextExpanded = !it.isTextExpanded) }
    }

    override fun onViewAllGalleryClick() {
        sendEffect(ActorDetailsScreenEffect.NavigateToActorGallery(actorId = actorId))
    }

    override fun onViewAllTopMoviesPicksClick() {
        sendEffect(ActorDetailsScreenEffect.NavigateToActorTopMoviePicks(actorId = actorId))
    }

    override fun onViewAllTopTvShowsClick() {
        sendEffect(ActorDetailsScreenEffect.NavigateToActorTopTvShowPicks(actorId = actorId))
    }

    override fun onMovieCardClick(movieId: Long) {
        sendEffect(ActorDetailsScreenEffect.NavigateToMovieDetails(movieId))
    }

    override fun onTvShowCardClick(tvShowId: Long) {
        sendEffect(ActorDetailsScreenEffect.NavigateToTvShowDetails(tvShowId))
    }

    override fun onSaveMovieClick(movie: ActorDetailsScreenState.MovieUiState) {

        onSaveButtonClicked(listId = movie.savedListId, itemId = movie.id, isSaved = movie.isSaved)

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
            onSuccess = { onAddItemToListSuccess() },
            dispatcher = ioDispatcher,
            onStart = ::onAddItemToListStart,
            onFinally = ::onAddItemToListFinished,
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

    private fun onAddItemToListSuccess() {
        onSaveToListBottomSheetDismiss()
            refreshSavedItems()
        showItemSavedSuccessfullySnackBar()
        }

        private fun refreshSavedItems() {
            getActorMovies(actorId)
            getUserSavedLists()
    }

    private fun showItemSavedSuccessfullySnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.SavedItemSuccessfully,
            isSuccess = true,
        )
    }

    override fun onSnackBarActionLabelClick() {
        hideSnackBar()
        loadData()
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

    override fun onLoginClicked() {
        sendEffect(
            ActorDetailsScreenEffect.NavigateToLogin,
        )
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
                    title = currentState.addListBottomSheetState.listName,
                )
            },
            onSuccess = { onCreateListSuccess() },
            dispatcher = ioDispatcher,
            onStart = ::onCreateListStart,
            onFinally = ::onCreateListFinished,
        )
    }

    private fun onCreateListSuccess() {
        onCreateListBottomSheetDismiss()
        getUserSavedLists()
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

    companion object {
        private const val MAX_GALLERY_IMAGES = 10
        private const val MAX_TOP_MOVIE_PICKS = 10
        private const val MAX_TOP_TV_SHOW_PICKS = 10
        private const val DEFAULT_PAGE_SIZE = 20
    }
}
