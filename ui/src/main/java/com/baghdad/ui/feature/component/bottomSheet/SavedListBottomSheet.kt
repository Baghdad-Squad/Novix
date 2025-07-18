package com.baghdad.ui.feature.component.bottomSheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.BaseBottomSheet
import com.baghdad.design_system.component.Selection
import com.baghdad.design_system.shared.Selectable
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.ui.R
import com.baghdad.ui.feature.search.component.BottomSheetHeader
import com.baghdad.ui.feature.search.component.filter.BottomSheetFooter
import com.baghdad.viewmodel.share.ListUiState

@Composable
fun SavedListBottomSheet(
    onAddClick: () -> Unit,
    createNewListClick: () -> Unit,
    isVisible: Boolean,
    onBottomSheetCloseClick: () -> Unit,
    lists: List<ListUiState>,
    onListSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = { onBottomSheetCloseClick() },
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp)
        ) {

            BottomSheetHeader(
                onCloseClick = { onBottomSheetCloseClick() },
                title = stringResource(R.string.save_to_list),
            )

            lists.forEach {
                Selection(
                    option = it.toSelectable(),
                    onClick = { onListSelected(it.id) },
                    trailingText = "${it.itemsCount} items",
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            BottomSheetFooter(
                applyLabel = stringResource(R.string.add),
                clearLabel = stringResource(R.string.create_new_list),
                onApplyClick = onAddClick,
                onClearClick = createNewListClick,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

fun ListUiState.toSelectable(): Selectable<String> {
    return Selectable(
        value = this.name,
        isSelected = this.isSelected,
    )
}

@Preview
@Composable
private fun SavedListBottomSheetPrev() {
    var isSelectedFavorite by remember { mutableStateOf(true) }
    NovixTheme {
        SavedListBottomSheet(
            onAddClick = {},
            createNewListClick = {},
            onBottomSheetCloseClick = {},
            isVisible = true,
            lists = listOf(
                ListUiState(
                    id = 1,
                    name = "Favorites",
                    itemsCount = 5,
                    isSelected = isSelectedFavorite
                ),
                ListUiState(
                    id = 2,
                    name = "Watch Later",
                    itemsCount = 10,
                    isSelected = isSelectedFavorite
                ),
                ListUiState(
                    id = 3,
                    name = "My Movies",
                    itemsCount = 15,
                    isSelected = isSelectedFavorite
                )
            ),
            onListSelected = {}
        )
    }
}