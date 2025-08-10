package com.baghdad.ui.feature.component.bottomSheet

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.BaseBottomSheet
import com.baghdad.design_system.component.Selection
import com.baghdad.design_system.component.button.OutlinedButton
import com.baghdad.design_system.shared.Selectable
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingColumn
import com.baghdad.ui.feature.search.component.BottomSheetHeader
import com.baghdad.viewmodel.shared.SavedListUiState
import kotlinx.coroutines.flow.flowOf

@Composable
fun SavedListBottomSheet(
    isVisible: Boolean,
    isUserLoggedIn: Boolean,
    onAddClick: () -> Unit,
    onCreateNewListClick: () -> Unit,
    onLoginClick: () -> Unit,
    onBottomSheetCloseClick: () -> Unit,
    lists: LazyPagingItems<SavedListUiState>,
    selectedListId: Long?,
    onListSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = { onBottomSheetCloseClick() },
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier =
                modifier
                    .padding(start = 16.dp, end = 16.dp),
        ) {
            BottomSheetHeader(
                onCloseClick = { onBottomSheetCloseClick() },
                title = stringResource(R.string.save_to_list),
            )
            AnimatedContent(isUserLoggedIn) { isLoggedIn ->
                if (isLoggedIn) {
                    SavedListsBottomSheetContent(
                        lists = lists,
                        selectedListId = selectedListId,
                        onListSelected = onListSelected,
                        onAddClick = onAddClick,
                        onCreateNewListClick = onCreateNewListClick,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                } else {
                    NoLoginContent(
                        onLoginClick = onLoginClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedListsBottomSheetContent(
    lists: LazyPagingItems<SavedListUiState>,
    selectedListId: Long?,
    onListSelected: (Long) -> Unit,
    onAddClick: () -> Unit,
    onCreateNewListClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        LazyPagingColumn(
            modifier = Modifier.weight(1f),
            items = lists,
            state = listState,
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            key = { it.id },
        ) { list ->
            Selection(
                option = list.toSelectable(selectedListId),
                onClick = { onListSelected(list.id) },
                description = stringResource(R.string.list_item_count_placeholder, list.itemsCount),
            )
        }
        BottomSheetFooter(
            applyLabel = stringResource(R.string.add),
            clearLabel = stringResource(R.string.create_new_list),
            onApplyClick = onAddClick,
            isApplyEnabled = selectedListId != null,
            onClearClick = onCreateNewListClick,
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
        )
    }
}

@Composable
private fun NoLoginContent(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        EmptyMediaState(
            imagePath = com.baghdad.design_system.R.drawable.user_person_profile,
            contentDescription = stringResource(R.string.bottom_sheet_content_description),
            message = stringResource(R.string.please_login_to_save_to_list),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
        )

        OutlinedButton(
            label = stringResource(R.string.login),
            onClick = onLoginClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
        )
    }
}

fun SavedListUiState.toSelectable(selectedListId: Long?): Selectable<String> =
    Selectable(
        value = this.name,
        isSelected = this.id == selectedListId,
    )

@Preview(showBackground = true)
@Composable
private fun PreviewSavedListBottomSheet() {
    NovixTheme {
        SavedListBottomSheet(
            isVisible = true,
            isUserLoggedIn = true,
            onAddClick = {},
            onCreateNewListClick = {},
            onLoginClick = {},
            onBottomSheetCloseClick = {},
            lists = flowOf<PagingData<SavedListUiState>>().collectAsLazyPagingItems(),
            selectedListId = 2L,
            onListSelected = {},
        )
    }
}
