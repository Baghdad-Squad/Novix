package com.baghdad.ui.feature.search.component.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.button.OutlinedButton
import com.baghdad.design_system.component.button.PrimaryButton

@Composable
fun BottomSheetFooter(
    onApplyClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    applyLabel: String = stringResource(R.string.apply),
    clearLabel: String = stringResource(R.string.clear),
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PrimaryButton(
            label = applyLabel,
            onClick = onApplyClick,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(
            label = clearLabel,
            onClick = onClearClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}