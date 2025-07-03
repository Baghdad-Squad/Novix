package com.baghdad.design_system.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme

@Composable
fun SectionHeader(
    title: String,
    actionText: String,
    isAllTextVisiable: Boolean,
    actionIcon: ImageVector,
    contentDiscription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 7.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = Theme.typography.headline.small
                .copy(color = Theme.color.title)
        )

        if (isAllTextVisiable) {
            Row {
                Text(
                    text = actionText,
                    style = Theme.typography.label.medium
                        .copy(color = Theme.color.primary)
                )
                Icon(
                    imageVector = actionIcon,
                    contentDescription = contentDiscription,
                    tint = Theme.color.primary,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp)
                )
            }
        }
    }
}

    
