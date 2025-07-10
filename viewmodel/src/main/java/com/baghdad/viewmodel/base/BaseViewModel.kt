package com.baghdad.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
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

    protected val currentState: UI_STATE
        get() = _uiState.value

    protected fun updateState(reducer: (UI_STATE) -> UI_STATE) {
        _uiState.update(reducer)
    }

    protected fun emitEffect(effect: UI_EFFECT) {
        viewModelScope.launch {
            _uiEffect.emit(effect)
        }
    }

    protected fun <T> tryToExecute(
        function: suspend () -> T,
        onSuccess: ((T) -> Unit)? = null,
        onError: (BaseErrorState) -> Unit = ::handleError,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onStart: () -> Unit = {},
        onFinally: () -> Unit = {},
    ) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            val errorState = mapThrowableToErrorState(throwable)
            onError(errorState)
        }
        viewModelScope.launch(dispatcher + handler) {
            onStart()
            try {
                val result = function()
                onSuccess?.invoke(result)
            } catch (exception: Exception) {
                val errorState = when (exception) {
                    // TODO: Replace these exceptions with most common domain exceptions
//                    is NoInternetDomainException -> BaseErrorState.NoInternet
//                    is AuthorizationException -> BaseErrorState.UnAuthorized
//                    is ServerException -> BaseErrorState.ServerError
                    else -> mapThrowableToErrorState(exception)
                }
                onError(errorState)
            } finally {
                onFinally()
            }
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

    protected abstract fun handleError(baseErrorState: BaseErrorState)
    protected abstract fun mapThrowableToErrorState(throwable: Throwable): BaseErrorState

}
