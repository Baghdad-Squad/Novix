package com.baghdad.ui.feature.actorDetails.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme

@Composable
fun IconTextInfo(
    painter: Painter,
    contentDescription: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = Theme.color.body,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = text,
            style = Theme.typography.label.small,
            color = Theme.color.body,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
