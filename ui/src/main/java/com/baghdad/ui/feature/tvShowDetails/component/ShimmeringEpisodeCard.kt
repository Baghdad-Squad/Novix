package com.baghdad.ui.feature.tvShowDetails.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.CircleDot
import com.baghdad.design_system.theme.Theme

@Composable
fun ShimmeringEpisodeCard(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(116.dp)
                .height(78.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmerEffect()
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(16.dp)
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .shimmerEffect()
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(12.dp)
                        .shimmerEffect()
                )

                CircleDot(
                    modifier = Modifier.size(4.dp),
                )

                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(12.dp)
                        .shimmerEffect()
                )

                CircleDot(
                    modifier = Modifier.size(4.dp),
                )

                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(12.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
private fun Modifier.shimmerEffect(): Modifier {
    val color = Theme.color.disable
    return this.then(
        Modifier.background(
            brush = remember {
                Brush.linearGradient(
                    colors = listOf(
                        color.copy(alpha = 0.9f),
                        color.copy(alpha = 0.9f),
                        color.copy(alpha = 0.9f),
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            }
        )
    )
}