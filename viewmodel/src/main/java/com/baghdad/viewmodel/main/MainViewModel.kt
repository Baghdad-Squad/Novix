package com.baghdad.viewmodel.main

import com.baghdad.domain.usecase.appConfigurations.GetAppLanguageUseCase
import com.baghdad.domain.usecase.appConfigurations.GetAppThemeUseCase
import com.baghdad.domain.usecase.appConfigurations.GetContentRestrictionUseCase
import com.baghdad.domain.usecase.appConfigurations.IsFirstTimeLaunchAppUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.SyncSavedMoviesUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.profile.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val isFirstTimeLaunchAppUseCase: IsFirstTimeLaunchAppUseCase,
    private val syncSavedMoviesUseCase: SyncSavedMoviesUseCase,
    private val getContentRestrictionUseCase: GetContentRestrictionUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<MainState, MainEffect>(
    MainState()
), MainInteractionListener {
    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    init {
        checkIsFirstTimeUser()
        checkIsLoggedIn()
        getAppTheme()
        getAppLanguage()
        getContentRestriction()
    }

    private fun getAppTheme() {
        tryToCollect(
            flowProvider = getAppThemeUseCase::invoke,
            onNewValue = ::onSuccessGetAppTheme,
            dispatcher = defaultDispatcher
        )
    }

    private fun getAppLanguage() {
        tryToCollect(
            flowProvider = getAppLanguageUseCase::invoke,
            onNewValue = ::onSuccessGetAppLanguage,
            dispatcher = defaultDispatcher
        )
    }

    override fun checkIsLoggedIn() {
        tryToExecute(
            callee = isUserLoggedInUseCase::invoke,
            onSuccess = ::onSuccessLoggedIn,
            onError = ::onError,
            onFinally = ::onFinally,
            dispatcher = defaultDispatcher
        )
    }

    override fun checkIsFirstTimeUser() {
        tryToExecute(
            callee = isFirstTimeLaunchAppUseCase::invoke,
            onSuccess = ::onSuccessFirstTimeLaunch,
            onError = ::onError,
            dispatcher = defaultDispatcher
        )
    }

    private fun onSuccessFirstTimeLaunch(isFirstTime: Boolean) {
        updateState { it.copy(isFirstTimeUser = isFirstTime, isLoading = false) }
    }

    private fun onSuccessLoggedIn(result: Boolean) {
        updateState { it.copy(isLoggedIn = result, isLoading = false) }

        if (result) {
            tryToExecute(
                callee = syncSavedMoviesUseCase::invoke,
                dispatcher = defaultDispatcher
            )
        }
    }

    private fun getContentRestriction() {
        tryToCollect(
            flowProvider = { getContentRestrictionUseCase() },
            onNewValue = { contentRestriction ->
                updateState { it.copy(contentRestriction = contentRestriction.toUiState()) }
            },
            dispatcher = defaultDispatcher
        )
    }

    private fun onError(throwable: Throwable) {
        updateState { it.copy(isLoading = false) }
    }

    private fun onSuccessGetAppLanguage(appLanguage: String) {
        updateState { it.copy(appLanguage = appLanguage) }
    }

    private fun onSuccessGetAppTheme(appTheme: Boolean) {
        updateState { it.copy(isAppInDarkTheme = appTheme) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}