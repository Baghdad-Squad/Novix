package com.baghdad.ui.feature.myLists

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.component.button.FloatingActionButton
import com.baghdad.design_system.component.scaffold.Scaffold
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.bottomSheet.AddListBottomSheet
import com.baghdad.ui.feature.myLists.component.EmptyStateContent
import com.baghdad.ui.feature.myLists.component.SavedListsVerticalGrid
import com.baghdad.ui.navigation.graph.myLists.MyListsNavEvent
import com.baghdad.ui.util.toScaffoldSnackBarState
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.MyListsSnackBarMessage
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

    Scaffold(
        isLoading = uiState.isLoading,
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 25.dp, bottom = 12.dp),
                screenTitle = stringResource(R.string.my_lists),
            )
        },
        snackBarState = snackBarState.toScaffoldSnackBarState(::mapSnackBarMessage),
        onSnackBarActionClick = listener::onSnackBarActionLabelClick,
        floatingActionButton = {
            AnimatedVisibility(uiState.isUsedLoggedIn) {
                FloatingActionButton(
                    painter = painterResource(R.drawable.ic_add),
                    onClick = listener::onAddListFabClick
                )
            }
        },
        backgroundContent = { BackgroundBlur() }

    ) {

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
    when (effect) {
        is MyListsScreenEffect.NavigateToLogin ->
            handleNavigation(
                MyListsNavEvent.NavigateToLogin,
            )

        is MyListsScreenEffect.NavigateToViewSavedDetails ->
            handleNavigation(
                MyListsNavEvent.NavigateToListDetails(effect.listId),
            )
    }
}

private fun mapSnackBarMessage(type: BaseSnackBarMessage): Int =
    when (type) {
        MyListsSnackBarMessage.SavedListCreatedSuccessfully -> R.string.added_list_successfully
        MyListsSnackBarMessage.SavedListDeletedSuccessfully -> R.string.deleted_list_successfully
        else -> type.toStringResource()
    }

