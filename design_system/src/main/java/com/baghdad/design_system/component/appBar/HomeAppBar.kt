package com.baghdad.design_system.component.appBar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier
) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            Theme.color.primary,
            Theme.color.primary
        ),
    )
    BasicTopAppBar {
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
        HomeAppBar()
    }
}