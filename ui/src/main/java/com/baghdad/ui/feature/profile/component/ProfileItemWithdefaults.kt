package com.baghdad.ui.feature.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme

@Composable
fun ProfileItemWithDefaults(
    title: String,
    icon: Painter,
    defaultValue: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable {
            onClick
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Theme.color.surface,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = Theme.color.stroke,
                    shape = RoundedCornerShape(12.dp),
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = "profile setting item",
                tint = Theme.color.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
            )
        }
        Text(
            text = title,
            style = Theme.typography.label.large,
            color = Theme.color.title,
            modifier = Modifier.padding(start = 12.dp),
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = defaultValue,
            style = Theme.typography.label.small,
            color = Theme.color.hint,
        )

    }

}