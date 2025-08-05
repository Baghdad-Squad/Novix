package com.baghdad.viewmodel.myLists

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface MyListsScreenEffect : BaseUiEffect {
    data class NavigateToViewSavedDetails(val listId: Long) : MyListsScreenEffect
    data object NavigateToLogin : MyListsScreenEffect
}
