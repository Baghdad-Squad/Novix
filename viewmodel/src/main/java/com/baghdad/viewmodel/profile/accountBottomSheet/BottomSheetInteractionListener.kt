package com.baghdad.viewmodel.profile.accountBottomSheet

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
    /**
     * we wanna add key or id to each list item
     */
}