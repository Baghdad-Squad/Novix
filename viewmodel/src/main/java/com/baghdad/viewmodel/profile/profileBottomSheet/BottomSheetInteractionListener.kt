package com.baghdad.viewmodel.profile.profileBottomSheet

interface BottomSheetInteractionListener {
    fun onCancelClick()
    fun onDarkAppearanceClick()
    fun onLightAppearanceClick()
    fun onEnglishLanguageClick()
    fun onArabicLanguageClick()
    fun onStrictContentRestrictionClick()
    fun onModerateContentRestrictionClick()
    fun onvContentRestrictionClick()
    fun onSaveClick()
    fun onLogOutClick()
}