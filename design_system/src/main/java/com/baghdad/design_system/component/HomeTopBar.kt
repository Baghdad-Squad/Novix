package com.baghdad.design_system.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier
) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            Theme.color.primary,
            Theme.color.primary
        ),
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.logo_design),
            contentDescription = stringResource(R.string.home_icon),
            tint = Theme.color.primary,
            modifier = modifier
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(brush = gradient, blendMode = BlendMode.SrcAtop)
                    }
                }
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.novix),
                style = Theme.typography.title.medium,
                color = Theme.color.body
            )
            Text(
                text = stringResource(R.string.novix_description),
                style = Theme.typography.label.small,
                color = Theme.color.hint
            )
        }
    }
}

@Preview(
    name = "HomeTopBar Preview", showBackground = true,
    backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 56
)
@Composable
private fun HomeTopBarPreview() {
    NovixTheme {
        HomeTopBar()
    }
}