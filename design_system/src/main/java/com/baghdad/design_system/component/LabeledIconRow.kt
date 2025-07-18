package com.baghdad.design_system.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme

@Composable
fun LabeledIconRow(title: String, icon: Painter, modifier: Modifier = Modifier, tint: Color = Theme.color.body) {
    Row(modifier = modifier) {
        Icon(
            painter = icon,
            contentDescription = "Icon",
            modifier = Modifier.size(12.dp).align(alignment = Alignment.CenterVertically),
            tint = tint
        )
        Text(
            text = title,
            color = Theme.color.body,
            style = Theme.typography.label.small,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
