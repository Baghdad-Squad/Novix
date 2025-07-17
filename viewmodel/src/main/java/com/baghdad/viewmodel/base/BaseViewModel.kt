package com.baghdad.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.baghdad.domain.exception.LocalDataBaseException
import com.baghdad.domain.exception.NetworkException
import com.baghdad.domain.exception.UnAuthorizedException
import com.baghdad.domain.exception.UnknownException
import com.baghdad.domain.model.PagedResult
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<UI_STATE : BaseUiState, UI_EFFECT : BaseUiEffect>(
    initialState: UI_STATE
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UI_EFFECT>()
    val uiEffect = _uiEffect.asSharedFlow()

    private val _snackBarState = MutableStateFlow(SnackBarState())
    val snackBarState = _snackBarState.asStateFlow()

    protected val currentState: UI_STATE
        get() = _uiState.value

    protected fun updateState(updater: (UI_STATE) -> UI_STATE) {
        _uiState.update(updater)
    }

    protected fun sendEffect(effect: UI_EFFECT) {
        viewModelScope.launch {
            _uiEffect.emit(effect)
        }
    }

    fun showSnackBar(
        message: BaseSnackBarMessage,
        isSuccess: Boolean,
        durationMillis: Long = 3000L,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            _snackBarState.update {
                SnackBarState(
                    message = message,
                    isSuccess = isSuccess,
                    isVisible = true
                )
            }
            delay(durationMillis)
            _snackBarState.update {
                it.copy(
                    isVisible = false
                )
            }
        }
    }

    protected fun <T> tryToExecute(
        callee: suspend () -> T,
        onSuccess: ((T) -> Unit)? = null,
        onError: (Throwable) -> Unit = ::handleError,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onStart: suspend () -> Unit = {},
        onFinally: () -> Unit = {},
    ): Job {
        val handler = createExceptionHandler(onError)
        return viewModelScope.launch(dispatcher + handler) {
            runWithErrorCheck(
                callee = callee,
                onSuccess = onSuccess,
                onError = onError,
                onStart = onStart,
                onFinally = onFinally
            )
        }
    }

    protected fun <Entity : Any, UiState : Any> collectPagingFlow(
        loadData: suspend (page: Int) -> PagedResult<Entity>,
        onInitialLoadFinished: suspend () -> Unit,
        pageSize: Int = 20,
        mapEntityToUiState: (Entity) -> UiState,
        onFlowCreated: (Flow<PagingData<UiState>>) -> Unit
    ) {
        val flow = createPagedResultPager(
            pageSize = pageSize,
            loadData = loadData,
            onInitialLoadFinished = onInitialLoadFinished
        ).map { pagingData ->
            pagingData.map { entity -> mapEntityToUiState(entity) }
        }
        onFlowCreated(flow)
    }

    private suspend fun <T> runWithErrorCheck(
        callee: suspend () -> T,
        onSuccess: ((T) -> Unit)?,
        onError: (Throwable) -> Unit,
        onStart: suspend () -> Unit,
        onFinally: () -> Unit,
    ) {
        onStart()
        try {
            val result = callee()
            onSuccess?.invoke(result)
        } catch (throwable: Throwable) {
            handleError(throwable)
            onError(throwable)
        } finally {
            onFinally()
        }
    }

    protected fun <T> tryToCollect(
        flowProvider: suspend () -> Flow<T>,
        onNewValue: suspend (T) -> Unit,
        onError: (Throwable) -> Unit = ::handleError,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Job {
        val handler = CoroutineExceptionHandler { _, throwable ->
            mapThrowableToErrorMessage(throwable)
            onError(throwable)
        }
        return viewModelScope.launch(dispatcher + handler) {
            flowProvider().distinctUntilChanged().collectLatest {
                onNewValue(it)
            }
        }
    }

    private fun handleError(throwable: Throwable) {
        val errorMessage = when (throwable) {
            is LocalDataBaseException -> BaseSnackBarMessage.DataBaseError
            is UnknownException -> BaseSnackBarMessage.UnknownError
            is UnAuthorizedException -> BaseSnackBarMessage.UnAuthorizedError
            is NetworkException -> BaseSnackBarMessage.NetworkError
            else -> mapThrowableToErrorMessage(throwable)
        }
        showSnackBar(message = errorMessage, isSuccess = false)
    }

    private fun createExceptionHandler(onError: (Throwable) -> Unit): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            mapThrowableToErrorMessage(throwable)
            onError(throwable)
        }
    }

    protected abstract fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage
}
