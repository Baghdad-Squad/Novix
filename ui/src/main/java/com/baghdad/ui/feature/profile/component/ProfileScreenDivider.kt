package com.baghdad.ui.feature.profile.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.HorizontalDivider
import com.baghdad.design_system.theme.Theme

@Composable
fun ProfileScreenDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        color = Theme.color.stroke,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    )
}