package com.baghdad.design_system.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SaveIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSaved: Boolean = false,
    backgroundColor: Color = Theme.color.iconBackground,
    tint: Color = Theme.color.onPrimary,
    size: Int = 32,
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = backgroundColor)
            .border(
                width = 1.dp,
                color = Theme.color.stroke,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {

        AnimatedContent(
            targetState = isSaved,
            transitionSpec = {
                scaleIn() + fadeIn() togetherWith scaleOut() + fadeOut()
            }
        ) { saved ->
            Icon(
                painter = painterResource(
                    if (!saved) R.drawable.ic_add_bookmark
                    else R.drawable.ic_remove_bookmark
                ),
                contentDescription = if (saved) stringResource(R.string.saved) else stringResource(R.string.unsaved),
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@NovixPreviews
@Composable
private fun SaveIconPreview() {
    NovixTheme {
        var isSaved by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(color = Theme.color.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SaveIcon(isSaved = isSaved, onClick = { isSaved = !isSaved })
        }
    }
}