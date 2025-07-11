package com.baghdad.design_system.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        skipPartiallyExpanded = true
    )

    LaunchedEffect(isVisible) {
        if (isVisible) {
            sheetState.show()
            sheetState.expand()
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
            scrimColor = Color(0x99000000),
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}

