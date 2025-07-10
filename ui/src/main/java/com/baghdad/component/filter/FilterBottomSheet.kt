package com.baghdad.component.filter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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

@Composable
fun FilterBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss
    ) {
        Column(
            modifier = modifier
                .padding(top = 16.dp, bottom = 24.dp)
        ) {
            FilterBottomSheetHeader(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )
            ReleasedYearSlider(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            GenresSection(
                modifier = Modifier
                    .padding(bottom = 24.dp)
            )
            IMDbRatingSection(
                modifier = Modifier
                    .padding(bottom = 24.dp)
            )
            FilterBottomSheetFooter()

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
                isVisible = isSheetVisible,
                onDismiss = { isSheetVisible = false },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
