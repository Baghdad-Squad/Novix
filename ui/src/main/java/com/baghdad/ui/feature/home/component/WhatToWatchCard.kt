package com.baghdad.ui.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme

@Composable
fun WhatToWatchCard(
    image: Painter,
    title: String,
    colors: List<Color>,
    imageWidth: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .noRippleClickable { onClick() },
    ) {
        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(colors = colors)),
        )
        Image(
            painter = image,
            contentDescription = title,
            modifier =
                Modifier
                    .size(width = imageWidth, height = 100.dp)
                    .align(alignment = Alignment.TopStart)
                    .padding(start = 4.dp)
                    .offset(y = -(32).dp),
        )
        Text(
            text = title,
            style = Theme.typography.title.medium,
            color = Theme.color.onPrimary,
            maxLines = 1,
            modifier =
                Modifier
                    .align(alignment = Alignment.BottomStart)
                    .padding(top = 81.dp, bottom = 7.dp)
                    .padding(8.dp),
        )
    }
}
