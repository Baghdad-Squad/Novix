package com.baghdad.viewmodel.profile.profileBottomSheet

import com.baghdad.domain.model.AppAppearanceMode
import com.baghdad.domain.usecase.profile.language.GetLanguageUseCase
import com.baghdad.domain.usecase.profile.appearance.GetAppearanceModeUseCase
import com.baghdad.domain.usecase.profile.language.SetLanguageUseCase
import com.baghdad.domain.usecase.profile.appearance.SetAppearanceModeUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel@Inject constructor(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val getThemeModeUseCase: GetAppearanceModeUseCase,
    private val setAppearanceModeUseCase: SetAppearanceModeUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<BottomSheetState,Nothing>(BottomSheetState()),
    BottomSheetInteractionListener {

    init {
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

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return when (throwable) {
            is java.net.UnknownHostException -> BaseSnackBarMessage.NetworkError
            else -> BaseSnackBarMessage.UnknownError
        }
    }
}
