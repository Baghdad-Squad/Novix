package com.baghdad.viewmodel.profile.profileBottomSheet

import com.baghdad.domain.model.AppAppearanceMode
import com.baghdad.domain.usecase.login.IsLoggedInUseCase
import com.baghdad.domain.usecase.login.LogOutUseCase
import com.baghdad.domain.usecase.profile.appearance.GetAppearanceModeUseCase
import com.baghdad.domain.usecase.profile.appearance.SetAppearanceModeUseCase
import com.baghdad.domain.usecase.profile.language.GetLanguageUseCase
import com.baghdad.domain.usecase.profile.language.SetLanguageUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.LoginSnackBarMessage
import com.baghdad.viewmodel.profile.ProfileEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val getThemeModeUseCase: GetAppearanceModeUseCase,
    private val setAppearanceModeUseCase: SetAppearanceModeUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<BottomSheetState, ProfileEffect>(BottomSheetState()),
    BottomSheetInteractionListener {

    init {
        isLoggedIn()
        getCurrentLanguage()
        getThemeMode()
    }

    private fun getCurrentLanguage() {
        tryToExecute(
            dispatcher = ioDispatcher,
            callee = { getLanguageUseCase() },
            onSuccess = ::getLanguageSuccess,
        )
    }

    private fun isLoggedIn() {
        tryToExecute(
            dispatcher = ioDispatcher,
            callee = { isLoggedInUseCase },
            onSuccess = {// TODO isLoggedInSuccess }
            }
        )
    }


    private fun isLoggedOutSuccess() {
        showSnackBar(
            message = LoginSnackBarMessage.LoginOutSuccessfully,
            isSuccess = true,
        )
        sendEffect(ProfileEffect.NavigateToLogin)
    }


    private fun getLanguageSuccess(currentLanguage: String) {
        updateState { it.copy(currentLanguage = currentLanguage) }
    }

    private fun getThemeMode() {
        tryToExecute(
            dispatcher = ioDispatcher,
            callee = { getThemeModeUseCase() },
            onSuccess = ::getThemeModeSuccess,
        )
    }

    private fun getThemeModeSuccess(theme: AppAppearanceMode) {
        updateState { it.copy(currentTheme = theme) }
    }

    override fun onCancelClick() {
        updateState { it.copy(isVisible = false) }
    }

    override fun onDarkAppearanceClick() {
        switchTheme(AppAppearanceMode.DARK)
    }

    override fun onLightAppearanceClick() {
        switchTheme(AppAppearanceMode.LIGHT)
    }

    private fun switchTheme(theme: AppAppearanceMode) {
        tryToExecute(
            dispatcher = ioDispatcher,
            callee = {
                setAppearanceModeUseCase(theme)
                theme
            },
            onSuccess = { updatedTheme ->
                updateState { it.copy(currentTheme = updatedTheme) }
            },
        )
    }

    override fun onEnglishLanguageClick() {
        tryToExecute(
            dispatcher = ioDispatcher,
            callee = {
                setLanguageUseCase("en")

            },
            onSuccess = {
                updateState { it.copy(currentLanguage = "en") }
            },
        )
    }

    override fun onArabicLanguageClick() {
        tryToExecute(
            dispatcher = ioDispatcher,
            callee = {
                setLanguageUseCase("ar")
            },
            onSuccess = {
                updateState { it.copy(currentLanguage = "ar") }
            },
        )
    }

    override fun onStrictContentRestrictionClick() {
        TODO("Not yet implemented")
    }


    override fun onModerateContentRestrictionClick() {
        TODO("Not yet implemented")

    }

    override fun onvContentRestrictionClick() {
        TODO("Not yet implemented")
    }

    override fun onSaveClick() {
        updateState { it.copy(isVisible = false) }
    }

    override fun onLogOutClick() {
        tryToExecute(
            dispatcher = ioDispatcher,
            callee = {
                logOutUseCase()
            },
            onSuccess = {
                isLoggedOutSuccess()
            },
        )
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return when (throwable) {
            is UnknownHostException -> BaseSnackBarMessage.NetworkError
            else -> BaseSnackBarMessage.UnknownError
        }
    }
}
