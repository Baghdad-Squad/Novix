package com.baghdad.viewmodel.main

import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.onBoarding.IsFirstTimeLaunchAppUseCase
import com.baghdad.domain.usecase.userPreferences.GetAppLanguageUseCase
import com.baghdad.domain.usecase.userPreferences.GetAppThemeUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val isFirstTimeLaunchAppUseCase: IsFirstTimeLaunchAppUseCase

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
    }

    private fun getAppTheme() {
        tryToCollect(
            flowProvider = { getAppThemeUseCase() },
            onNewValue = { isDarkTheme ->
                updateState { it.copy(isAppInDarkTheme = isDarkTheme) }
            }
        )
    }

    private fun getAppLanguage() {
        tryToCollect(
            flowProvider = { getAppLanguageUseCase() },
            onNewValue = { appLanguage ->
                updateState { it.copy(appLanguage = appLanguage) }
            }
        )
    }

    override fun checkIsLoggedIn() {
        tryToExecute(callee = {
            isUserLoggedInUseCase.invoke()
        }, onSuccess = {
            onSuccessLoggedIn(result = it)
        }, onError = {
            onError(it)
        }, onFinally = {
            updateState { it.copy(isLoading = false) }
        })
    }

    override fun checkIsFirstTimeUser() {
        tryToExecute(
            callee = {
                isFirstTimeLaunchAppUseCase()
            },
            onSuccess = ::onSuccessFirstTimeLaunch,
            onError = ::onError
        )

    }
    private fun onSuccessFirstTimeLaunch(isFirstTime: Boolean) {
        updateState {
            it.copy(
                isFirstTimeUser = isFirstTime,
                isLoading = false
            )
        }
    }

    private fun onSuccessLoggedIn(result: Boolean) {
        updateState {
            it.copy(
                isLoggedIn = result,
                isLoading = false,
            )
        }
    }

    private fun onError(throwable: Throwable) {
        updateState {
            it.copy(
                isLoading = false,
            )
        }
    }

}