package com.baghdad.design_system.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun Star(
    isFilled: Boolean,
    starSize: Dp,
    onClick: () -> Unit,
) {
    AnimatedContent(
        targetState = isFilled,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    ) { clicked ->
        Icon(
            imageVector = ImageVector.vectorResource(
                if (clicked) R.drawable.ic_star_filled
                else R.drawable.ic_star_outlined
            ),
            contentDescription = if (clicked) stringResource(R.string.clicked) else stringResource(R.string.not_clicked),
            tint = Theme.color.yellowAccent,
            modifier = Modifier
                .size(starSize)
                .noRippleClickable { onClick() }
        )
    }

}


@NovixPreviews
@Composable
private fun StarIconPreview() {
    NovixTheme {
        var isFilled by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(color = Theme.color.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Star(isFilled = isFilled, onClick = { isFilled = !isFilled }, starSize = 24.dp)
        }
    }
}