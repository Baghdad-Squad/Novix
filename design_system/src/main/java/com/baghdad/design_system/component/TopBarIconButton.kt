package com.baghdad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.Theme

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    icon: Int,
    tintIcon: Color = Theme.color.title,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(color = Theme.color.backgroundLow)
            .border(
                width = 1.dp,
                color = Theme.color.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick.invoke()
            }
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(R.string.app_bar_icon),
            tint = tintIcon,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.Center)
        )
    }
}