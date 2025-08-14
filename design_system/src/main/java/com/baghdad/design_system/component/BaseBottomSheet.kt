package com.baghdad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { sheetValue ->
            true
        }
    )

    LaunchedEffect(isVisible) {
        if (isVisible) {
            sheetState.partialExpand()
        } else {
            sheetState.hide()
        }
    }
    if (sheetState.isVisible || isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            containerColor = Theme.color.surface,
            dragHandle = {
                DragHandle()
            },
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()

        ) {
            content()
        }
    }
}

@Composable
private fun DragHandle(){
    Box(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .width(width = 32.dp)

    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 32.dp, height = 4.dp)
                .background(
                    color = Theme.color.body.copy(0.3f),
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}