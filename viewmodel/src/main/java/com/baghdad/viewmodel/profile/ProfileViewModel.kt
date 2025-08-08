package com.baghdad.viewmodel.profile

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.login.GetCurrentLoggedInUserUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.login.LogOutUseCase
import com.baghdad.domain.usecase.userPreferences.GetAppLanguageUseCase
import com.baghdad.domain.usecase.userPreferences.GetAppThemeUseCase
import com.baghdad.domain.usecase.userPreferences.SetAppLanguageUseCase
import com.baghdad.domain.usecase.userPreferences.SetAppThemeUseCase
import com.baghdad.entity.User
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val setAppThemeUseCase: SetAppThemeUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<ProfileScreenState, ProfileEffect>(ProfileScreenState()),
    ProfileInteractionListener {

    init {
        loadInitData()
    }

    private fun loadInitData() {
        checkIsUserLoggedIn()
        getAppTheme()
        getAppLanguage()
    }

    private fun getAppLanguage() {
        tryToCollect(
            flowProvider = { getAppLanguageUseCase.invoke() },
            onNewValue = { language ->
                onGetAppLanguageSuccess(language)
            },
            onError = ::onError
        )
    }

    private fun onGetAppLanguageSuccess(language: String) {
        updateState { profileScreenState ->
            profileScreenState.copy(
                userSettings = profileScreenState.userSettings.copy(
                    language = LanguagePreferences.fromLanguageCode(language)
                )
            )
        }
    }

    private fun getAppTheme() {
        tryToCollect(
            flowProvider = { getAppThemeUseCase.invoke() },
            onNewValue = { isDarkTheme ->
                onGetAppThemeSuccess(isDarkTheme)
            },
            onError = ::onError
        )
    }

    private fun onGetAppThemeSuccess(isDarkTheme: Boolean) {
        updateState { profileScreenState ->
            profileScreenState.copy(
                userSettings = profileScreenState.userSettings.copy(
                    appearance = if (isDarkTheme) ThemePreferences.DARK else ThemePreferences.LIGHT
                ),
                themeBottomSheetState = profileScreenState.themeBottomSheetState.copy(
                    currentTheme = if (isDarkTheme) ThemePreferences.DARK else ThemePreferences.LIGHT
                )
            )
        }
    }

    private fun checkIsUserLoggedIn() {
        tryToExecute(
            callee = { isUserLoggedInUseCase.invoke() },
            onSuccess = ::onCheckIsUserLoggedInSuccess,
            onError = ::onError,
            dispatcher = ioDispatcher
        )
    }

    private fun onCheckIsUserLoggedInSuccess(isUserLoggedIn: Boolean) {
        getUserInfo()
        updateState { profileScreenUIState ->
            profileScreenUIState.copy(
                isUserLoggedIn = isUserLoggedIn
            )
        }
    }

    private fun getUserInfo() {
        tryToExecute(
            callee = { getCurrentLoggedInUserUseCase.invoke() },
            onSuccess = ::onSuccessLoadData,
            onError = ::onError,
            dispatcher = ioDispatcher
        )
    }

    private fun onSuccessLoadData(user: User?) {
        user?.let {
            updateState { profileScreenUIState ->
                profileScreenUIState.copy(userInfo = user.toUIState())
            }
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError


    override fun onWatchingHistoryClick() {
        sendEffect(ProfileEffect.NavigateToWatchingHistory)
    }

    override fun onMyRatingClick() {
        sendEffect(ProfileEffect.NavigateToMyRatings)
    }

    override fun onContentRestrictionClick() {
//        TODO("Not yet implemented")
    }

    override fun onChangePasswordClick() {
        sendEffect(ProfileEffect.NavigateToChangePassword)
    }

    override fun onAppearanceClick() {
        updateState {
            it.copy(
                themeBottomSheetState = it.themeBottomSheetState.copy(isVisible = true),
            )
        }
    }

    override fun onLanguageClick() {
        updateState {
            it.copy(
                languageBottomSheetState = it.languageBottomSheetState.copy(isVisible = true),
            )
        }
    }

    override fun onLoginClick() {
        sendEffect(ProfileEffect.NavigateToLogin)
    }

    override fun onAppearanceChanged(theme: ThemePreferences) {
        updateState {
            it.copy(
                themeBottomSheetState = it.themeBottomSheetState.copy(
                    currentTheme = theme,
                ),
            )
        }
    }

    override fun onAppearanceConfirmed() {
        tryToExecute(
            callee = { setAppThemeUseCase(currentState.themeBottomSheetState.currentTheme.isDark) },
            dispatcher = ioDispatcher
        )
    }


    override fun onLanguageConfirmed() {
        tryToExecute(
            callee = { setAppLanguageUseCase(currentState.languageBottomSheetState.currentLanguage.languageCode) },
            dispatcher = ioDispatcher,
        )
    }


    override fun onLanguageChanged(language: LanguagePreferences) {
        updateState {
            it.copy(
                languageBottomSheetState = it.languageBottomSheetState.copy(
                    currentLanguage = language,
                ),
            )
        }
    }

    override fun onLogOutClick() {
        updateState {
            it.copy(logoutBottomSheetState = it.logoutBottomSheetState.copy(isVisible = true))
        }
    }

    override fun onLogoutDialogDismissed() {
        updateState {
            it.copy(logoutBottomSheetState = it.logoutBottomSheetState.copy(isVisible = false))
        }
    }

    override fun onAppearanceDialogDismissed() {
        updateState {
            it.copy(
                themeBottomSheetState = it.themeBottomSheetState.copy(isVisible = false),
            )
        }
    }

    override fun onLanguageDialogDismissed() {
        updateState {
            it.copy(
                languageBottomSheetState = it.languageBottomSheetState.copy(isVisible = false)
            )
        }
    }

    override fun onLogOutConfirmed() {
        tryToExecute(
            dispatcher = ioDispatcher,
            callee = { logOutUseCase.invoke() },
            onSuccess = ::onSuccessLogOut,
            onError = ::onError
        )
    }

    private fun onSuccessLogOut(result: Boolean) {
        updateState { profileScreenUIState ->
            profileScreenUIState.copy(
                isUserLoggedIn = false
            )
        }
        sendEffect(ProfileEffect.NavigateToLogin)
    }

    private fun onError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    override fun onSnackBarActionLabelClick() {
        loadInitData()
    }

    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }
}