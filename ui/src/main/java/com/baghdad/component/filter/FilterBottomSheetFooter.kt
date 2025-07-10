package com.baghdad.component.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.button.OutlinedButton
import com.baghdad.design_system.component.button.PrimaryButton

@Composable
fun FilterBottomSheetFooter(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PrimaryButton(
            label = "Apply",
            onClick = {/*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(
            label = "Clear",
            onClick = {/*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}