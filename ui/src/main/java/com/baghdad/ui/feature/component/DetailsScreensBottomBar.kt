package com.baghdad.ui.feature.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.baghdad.design_system.R
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.theme.Theme

@Composable
fun DetailsScreenBottomBar(
    isRated: Boolean,
    onRateClicked: () -> Unit,
    hasTrailer: Boolean,
    onPlayTrailerClicked: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .zIndex(1f)
                .fillMaxWidth()
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF0D0608)),
                        ),
                ).padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AnimatedVisibility(
            visible = isRated.not(),
        ) {
            IconButton(
                icon = painterResource(R.drawable.ic_star),
                tintIcon = Theme.color.onPrimary,
                background = Theme.color.primary,
                borderStroke = null,
                size = Pair(52.dp, 48.dp),
                onClick = onRateClicked,
            )
        }
        PrimaryButton(
            stringResource(com.baghdad.ui.R.string.play_trailer),
            modifier = Modifier.fillMaxWidth(),
            isEnabled = hasTrailer && !isLoading,
            onClick = onPlayTrailerClicked,
        )
    }
}
