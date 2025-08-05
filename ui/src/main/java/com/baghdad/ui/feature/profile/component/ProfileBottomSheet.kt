package com.baghdad.ui.feature.profile.component

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
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.bottomSheet.BottomSheetFooter
import com.baghdad.ui.feature.component.bottomSheet.toSelectable
import com.baghdad.ui.feature.search.component.BottomSheetHeader
import com.baghdad.viewmodel.share.ListUiState

@Composable
fun ProfileBottomSheet(
    title: String,
    onSaveClick: () -> Unit,
    isVisible: Boolean,
    onBottomSheetCloseClick: () -> Unit,
    lists: List<ListUiState>,
    onListSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = onBottomSheetCloseClick,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp)
        ) {
            BottomSheetHeader(
                onCloseClick = { onBottomSheetCloseClick() },
                title = title
            )
            lists.forEach {
                Selection(
                    option = it.toSelectable(),
                    onClick = { onListSelected(it.id) },
                    description = it.description,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            BottomSheetFooter(
                applyLabel = stringResource(R.string.save),
                onApplyClick = onSaveClick,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

    }
}

@Preview
@Composable
fun LanguageBottomSheetRev() {
    var isSelected by remember { mutableStateOf(true) }
    NovixTheme {
        ProfileBottomSheet(
            title = "Language",
            onSaveClick = {},
            onBottomSheetCloseClick = {},
            isVisible = true,
            lists = listOf(
                ListUiState(
                    id = 1,
                    name = "English",
                    isSelected = isSelected
                ),
                ListUiState(
                    id = 2,
                    name = "Arabic",
                    isSelected = isSelected
                )
            ),
            onListSelected = {}
        )
    }
}

@Preview
@Composable
fun ContentRestrictionBottomSheetPrev() {
    var isSelected by remember { mutableStateOf(true) }
    NovixTheme {
        ProfileBottomSheet(
            title = "Content Restriction",
            onSaveClick = {},
            onBottomSheetCloseClick = {},
            isVisible = true,

            lists = listOf(
                ListUiState(
                    id = 1,
                    name = "Strict",
                    isSelected = isSelected,
                    description = "Blurs all sensitive content."
                ),
                ListUiState(
                    id = 2,
                    name = "Moderate",
                    isSelected = isSelected,
                    description = "Blurs explicit scenes only."
                ),
                ListUiState(
                    id = 3,
                    name = "Off",
                    isSelected = isSelected,
                    description = "No content is blurred."
                )
            ),
            onListSelected = {}
        )
    }
}

