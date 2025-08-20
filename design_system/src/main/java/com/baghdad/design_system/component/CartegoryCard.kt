package com.baghdad.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.baghdad.design_system.R
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun CategoryCard(
    title: String,
    image: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .height(height = 68.dp)
            .border(
                width = 1.dp,
                color = Theme.color.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(shape = RoundedCornerShape(12.dp))
            .noRippleClickable(onClick = onClick)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f),
            contentScale = ContentScale.Crop,
            painter = image,
            contentDescription = "category image ${stringResource(R.string.category_image)}"
        )
        Box(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFF0D0608),
                            Color(0xCC0D0608),
                            Color(0xB20D0608),
                            Color(0x000D0608),
                        )
                    )
                )
        )

        Text(
            title,
            style = Theme.typography.label.large,
            color = Theme.color.onPrimary,
            modifier = Modifier
                .zIndex(3f)
                .widthIn(max = 110.dp)
                .padding(top = 8.dp, start = 8.dp)
        )
    }
}

@NovixPreviews
@Composable
private fun CategoryCardPreview() {
    NovixTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Theme.color.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CategoryCard(
                title = "Action",
                image = painterResource(id = R.drawable.action),
                onClick = {},
                modifier = Modifier.width(150.dp)
            )
        }
    }
}