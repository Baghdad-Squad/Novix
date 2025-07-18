package com.baghdad.ui.feature.episodeDetails.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun EpisodeDetailsBottomBar(
    isRated: Boolean,
    onRateClicked: () -> Unit,
    hasTrailer: Boolean,
    onPlayTrailerClicked: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Box(
            modifier = Modifier.Companion
                .zIndex(1f)
                .fillMaxWidth()
                .height(112.dp)
                .background(
                    brush = Brush.Companion.verticalGradient(
                        colors = listOf(
                            Color(0x000D0608),
                            Color(0xFF000000),
                        ),
                    )
                )
        )
        Row(
            modifier = modifier
                .zIndex(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 24.dp),
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Crossfade(
                targetState = isRated,
            ) { isRated ->
                IconButton(
                    icon = if (isRated) painterResource(R.drawable.ic_star_filled) else painterResource(
                        R.drawable.ic_star
                    ),
                    tintIcon = Theme.color.onPrimary,
                    background = Theme.color.primary,
                    borderStroke = null,
                    size = Pair(52.dp, 48.dp),
                    onClick = onRateClicked
                )
            }
            PrimaryButton(
                stringResource(com.baghdad.ui.R.string.play_trailer),
                modifier = Modifier.Companion.fillMaxWidth(),
                isEnabled = hasTrailer,
                isLoading = isLoading,
                onClick = onPlayTrailerClicked
            )
        }
    }
}