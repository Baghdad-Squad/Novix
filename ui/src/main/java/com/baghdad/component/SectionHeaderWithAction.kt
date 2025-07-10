package com.baghdad.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.TextButton
import com.baghdad.design_system.theme.Theme

@Composable
fun SectionHeaderWithAction(
    title: String,
    onClearAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = Theme.typography.label.medium,
            color = Theme.color.body
        )
        TextButton(
            label = stringResource(R.string.clear_all),
            onClick = { onClearAllClick() },
        )
    }
}



