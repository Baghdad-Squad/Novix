package com.baghdad.design_system.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.baghdad.design_system.R
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme

@Composable
fun RowScope.CategoryCard(
    title: String,
    @DrawableRes image: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .weight(1f)
            .height(height = 68.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .noRippleClickable(onClick = onClick)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = image),
            contentDescription = "category image ${stringResource(R.string.category_image)}"
        )
        Box(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf<Color>(
                            Color(0xFF0D0608),
                            Color(0xB20D0608),
                            Color(0xCC0D0608),
                            Color(0x000D0608),
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color(0x141F1F1F)
                )
        )
        Text(
            title,
            style = Theme.typography.label.large,
            color = Theme.color.onPrimary,
            modifier = Modifier
                .zIndex(3f)
                .widthIn(max = 99.dp)
                .padding(
                    top = 8.5.dp, start = 8.dp
                )
        )
    }
}
