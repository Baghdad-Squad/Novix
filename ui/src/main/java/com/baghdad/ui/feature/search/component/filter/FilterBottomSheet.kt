package com.baghdad.ui.feature.search.component.filter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.BaseBottomSheet
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.ui.feature.search.component.BottomSheetHeader
import com.baghdad.viewmodel.search.SearchScreenState
import com.baghdad.viewmodel.search.SearchScreenState.GenreUiState

@Composable
fun FilterBottomSheet(
    isBottomSheetVisible: Boolean,
    searchFilter: SearchScreenState.SearchFilterUiState,
    onApplyClick: () -> Unit,
    onClearClick: () -> Unit,
    onBottomSheetCloseClick: () -> Unit,
    onGenreSelected: (GenreUiState) -> Unit,
    onYearRangeSelected: (ClosedFloatingPointRange<Float>) -> Unit,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BaseBottomSheet(
        isVisible = isBottomSheetVisible,
        onDismiss = { onBottomSheetCloseClick() }
    ) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 24.dp)
        ) {
            BottomSheetHeader(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                onCloseClick = onBottomSheetCloseClick

            )

            ReleasedYearSlider(
                minimumYear = searchFilter.minimumYear,
                maximumYear = searchFilter.maximumYear,
                onValueChange = onYearRangeSelected,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            GenresSection(
                allGenres = searchFilter.allGenres,
                selectedGenres = searchFilter.selectedGenres,
                onGenreSelected = onGenreSelected,
                modifier = Modifier
                    .padding(bottom = 24.dp)
            )

            IMDbRatingSection(
                rate = searchFilter.minimumRating,
                onRatingChanged = onRatingChanged,
                modifier = Modifier
                    .padding(bottom = 24.dp)
            )

            FilterBottomSheetFooter(
                onApplyClick = onApplyClick,
                onClearClick = onClearClick,
                modifier = Modifier
                    .padding(bottom = 24.dp)
            )
        }
    }
}

@NovixPreviews
@Composable
private fun FilterBottomSheetPrev() {
    NovixTheme {
        var isSheetVisible by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {
            PrimaryButton(
                onClick = { isSheetVisible = true },
                label = "Open Bottom Sheet",
                modifier = Modifier.align(Alignment.Center)
            )
            FilterBottomSheet(
                searchFilter = SearchScreenState.SearchFilterUiState(),
                onApplyClick = {},
                onClearClick = {},
                isBottomSheetVisible = isSheetVisible,
                onBottomSheetCloseClick = { isSheetVisible = false },
                onYearRangeSelected = {},
                onGenreSelected = {},
                onRatingChanged = {},
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
