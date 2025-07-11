package com.baghdad.ui.feature.search.component.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.button.OutlinedButton
import com.baghdad.design_system.component.button.PrimaryButton

@Composable
fun FilterBottomSheetFooter(
    onApplyClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PrimaryButton(
            label = stringResource(R.string.apply),
            onClick = onApplyClick,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(
            label = stringResource(R.string.clear),
            onClick = onClearClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}