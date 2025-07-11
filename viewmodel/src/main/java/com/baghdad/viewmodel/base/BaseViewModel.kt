package com.baghdad.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baghdad.viewmodel.errorStates.BaseErrorState
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
        message: String,
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
                SnackBarState(
                    isVisible = false,
                )
            }
        }
    }

    protected fun <T> tryToExecute(
        callee: suspend () -> T,
        onSuccess: ((T) -> Unit)? = null,
        onError: (BaseErrorState) -> Unit = ::handleError,
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

    private suspend fun <T> runWithErrorCheck(
        callee: suspend () -> T,
        onSuccess: ((T) -> Unit)?,
        onError: (BaseErrorState) -> Unit,
        onStart: suspend () -> Unit,
        onFinally: () -> Unit,
    ) {
        onStart()
        try {
            val result = callee()
            onSuccess?.invoke(result)
        } catch (exception: Exception) {
            val errorState = mapExceptionToErrorState(exception)
            onError(errorState)
        } finally {
            onFinally()
        }
    }

    protected fun <T> tryToCollect(
        flowProvider: suspend () -> Flow<T>,
        onNewValue: suspend (T) -> Unit,
        onError: (BaseErrorState) -> Unit = ::handleError,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            val errorState = mapThrowableToErrorState(throwable)
            onError(errorState)
        }
        viewModelScope.launch(dispatcher + handler) {
            flowProvider().distinctUntilChanged().collectLatest {
                onNewValue(it)
            }
        }
    }


    private fun mapExceptionToErrorState(exception: Exception): BaseErrorState {
        return when (exception) {
            // TODO: Replace these exceptions with most common domain exceptions
            // is NoInternetDomainException -> BaseErrorState.NoInternet
            // is AuthorizationException -> BaseErrorState.UnAuthorized
            // is ServerException -> BaseErrorState.ServerError
            else -> mapThrowableToErrorState(exception)
        }
    }

    private fun createExceptionHandler(onError: (BaseErrorState) -> Unit): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            val errorState = mapThrowableToErrorState(throwable)
            onError(errorState)
        }
    }

    protected abstract fun handleError(baseErrorState: BaseErrorState)
    protected abstract fun mapThrowableToErrorState(throwable: Throwable): BaseErrorState
}
