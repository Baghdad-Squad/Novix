package com.baghdad.viewmodel.myLists

interface MyListsInteractionListener {
    fun onAddListFabClick()

    fun onLoginClick()

    fun onAddedListNameChange(name: String)

    fun onAddListBottomSheetDismiss()

    fun onAddListBottomSheetAddClick()

    fun onListClick(listId: Long)

    fun onSnackBarActionLabelClick()
}
