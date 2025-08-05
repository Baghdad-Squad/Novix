package com.baghdad.ui.feature.myLists

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.component.button.FloatingActionButton
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.myLists.component.AddListBottomSheet
import com.baghdad.ui.feature.myLists.component.EmptyStateContent
import com.baghdad.ui.feature.myLists.component.SavedListsVerticalGrid
import com.baghdad.ui.navigation.graph.myLists.MyListsNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.myLists.MyListsInteractionListener
import com.baghdad.viewmodel.myLists.MyListsScreenEffect
import com.baghdad.viewmodel.myLists.MyListsScreenState
import com.baghdad.viewmodel.myLists.MyListsViewModel

@Composable
fun MyListsScreen(
    viewModel: MyListsViewModel = hiltViewModel(),
    handleNavigation: (MyListsNavEvent) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    MyListsScreenContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState,
    )
}

@Composable
private fun MyListsScreenContent(
    uiState: MyListsScreenState,
    listener: MyListsInteractionListener,
    snackBarState: SnackBarState,
) {
    val savedLists = uiState.savedLists.collectAsLazyPagingItems()
    Scaffold(isLoading = uiState.isLoading, topBar = {
        TopAppBar(
            screenTitle = stringResource(R.string.my_lists),
        )
    }, snackbar = {
        SnackBar(
            isVisible = snackBarState.isVisible,
            isSuccess = snackBarState.isSuccess,
            message = stringResource(snackBarMessage(snackBarState.message)),
            actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
            onActionClick = listener::onSnackBarActionLabelClick,
        )
    }, floatingActionButton = {
        AnimatedVisibility(uiState.isUsedLoggedIn) {
            FloatingActionButton(
                painter = painterResource(R.drawable.ic_add),
                onClick = listener::onAddListFabClick,
            )
        }
    }) {
        AnimatedContent(
            targetState = savedLists.itemCount == 0 && uiState.isLoading.not(),
        ) { isEmptyList ->
            if (isEmptyList) {
                EmptyStateContent(
                    isUserLoggedIn = uiState.isUsedLoggedIn,
                    onLoginClick = listener::onLoginClick,
                )
            } else {
                SavedListsVerticalGrid(
                    savedLists = savedLists,
                    onSavedListClick = listener::onListClick,
                )
            }
        }
        AddListBottomSheet(
            isVisible = uiState.addListBottomSheetState.isVisible,
            isLoading = uiState.addListBottomSheetState.isLoading,
            listName = uiState.addListBottomSheetState.listName,
            onDismiss = listener::onAddListBottomSheetDismiss,
            onAddClick = listener::onAddListBottomSheetAddClick,
            onListNameChange = listener::onAddedListNameChange,
        )
    }
}

private fun handleEffect(
    effect: MyListsScreenEffect,
    handleNavigation: (MyListsNavEvent) -> Unit,
) {
    TODO("Not yet implemented")
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int = type.toStringResource()
