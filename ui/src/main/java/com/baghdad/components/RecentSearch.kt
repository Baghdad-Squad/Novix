package com.baghdad.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun RecentSearch(
    title: String,
    onCanceleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = com.baghdad.design_system.R.drawable.clock),
            contentDescription = stringResource(R.string.clock_icon),
            modifier = Modifier
                .padding(end = 8.dp)
                .size(20.dp)
        )
        Text(
            text = title,
            style = Theme.typography.body.medium,
            color = Theme.color.title
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onCanceleClick() }) {
            Icon(
                painter = painterResource(id = com.baghdad.design_system.R.drawable.cancel),
                contentDescription = stringResource(R.string.cancel_icon),
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(16.dp)

            )
        }
    }
}

