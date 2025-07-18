package com.baghdad.ui.feature.component.lazyPaging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.TextButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun DefaultErrorItem(
    onRetry: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.an_error_occurred),
            color = Theme.color.title,
            style = Theme.typography.body.medium
        )
        TextButton(
            modifier = Modifier.padding(start = 16.dp),
            label = stringResource(R.string.retry),
            onClick = onRetry
        )
    }
}
