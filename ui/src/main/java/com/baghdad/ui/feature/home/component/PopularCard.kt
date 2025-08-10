package com.baghdad.ui.feature.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.SaveIcon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.modifier.shimmerEffect
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.feature.component.islamicImage.IslamicImage

private const val ANIMATION_DURATION = 75
private val ENTER_ANIMATION_SPEC = fadeIn(animationSpec = tween(ANIMATION_DURATION))
private val EXIT_ANIMATION_SPEC = fadeOut(animationSpec = tween(ANIMATION_DURATION))

@Composable
fun PopularCard(
    contentName: String,
    contentRating: Double,
    imageUrl: String,
    onCardClick: () -> Unit,
    onSavedClick: () -> Unit,
    isSaved: Boolean,
    modifier: Modifier = Modifier,
    isSaveToListVisible: Boolean = true,
    isCentralCard: Boolean = false,
) {
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isCentralCard) 0.6f else 0.8f,
        animationSpec = tween(durationMillis = ANIMATION_DURATION, easing = FastOutSlowInEasing),
        label = "overlay_alpha",
    )

    val startColor by animateColorAsState(
        targetValue = if (isCentralCard) Color(0x000D0608) else Color(0x4D0D0608),
        animationSpec = tween(durationMillis = 150),
        label = "start_color",
    )

    val endColor by animateColorAsState(
        targetValue = Color(0x990D0608),
        animationSpec = tween(durationMillis = 150),
        label = "end_color",
    )

    Box(
        modifier
            .size(width = 188.dp, height = 244.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.color.surface, shape = RoundedCornerShape(12.dp))
            .border(1.dp, Theme.color.stroke, shape = RoundedCornerShape(12.dp))
            .noRippleClickable { onCardClick() },
        contentAlignment = Alignment.Center,
    ) {
        IslamicImage(
            imageUrl = imageUrl,
            loadingContent = null,
            contentDescription = contentName,
            modifier =
                Modifier
                    .fillMaxSize()
                    .align(alignment = Alignment.Center)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush =
                                if (isCentralCard) {
                                    Brush.verticalGradient(
                                        colors =
                                            listOf(
                                                startColor,
                                                startColor,
                                                startColor,
                                                endColor,
                                            ),
                                    )
                                } else {
                                    Brush.linearGradient(
                                        colors = listOf(startColor, endColor),
                                    )
                                },
                            alpha = overlayAlpha,
                        )
                    },
        )
        AnimatedVisibility(
            visible = isCentralCard && isSaveToListVisible,
            enter = ENTER_ANIMATION_SPEC,
            exit = EXIT_ANIMATION_SPEC,
            modifier =
                Modifier
                    .padding(4.dp)
                    .align(Alignment.TopStart),
        ) {
            SaveIcon(
                isSaved = isSaved,
                onClick = { onSavedClick() },
            )
        }
        AnimatedVisibility(
            visible = isCentralCard,
            enter = ENTER_ANIMATION_SPEC,
            exit = EXIT_ANIMATION_SPEC,
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = contentName,
                    style = Theme.typography.label.medium,
                    color = Theme.color.onPrimary,
                )
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_rating_star),
                        contentDescription = stringResource(R.string.rating_star),
                        tint = Theme.color.yellowAccent,
                        modifier = Modifier.size(12.dp),
                    )
                    Text(
                        text = contentRating.toString(),
                        style = Theme.typography.label.small,
                        color = Theme.color.onPrimary,
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingPopularCard(
    isCentralCard: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(width = 188.dp, height = 244.dp)
                .background(Theme.color.surface)
                .clip(RoundedCornerShape(12.dp))
                .shimmerEffect(),
    ) {
        if (isCentralCard) {
            Column(
                modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp, bottom = 8.dp, end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(width = 172.dp, height = 44.dp)
                            .background(Theme.color.surface, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerEffect(),
                )
                Box(
                    modifier =
                        Modifier
                            .size(width = 43.dp, height = 18.dp)
                            .background(Theme.color.surface)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect(),
                )
            }
        }
    }
}

@NovixPreviews
@Composable
private fun PopularCardPrev() {
    NovixTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = Theme.color.surface)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            PopularCard(
                contentName = "The Shawshank Redemption",
                contentRating = 9.9,
                imageUrl = "https://image.tmdb.org/t/p/w500/etT14XfDEqhQZdD47ywpyihXPyW.jpg",
                onCardClick = {},
                onSavedClick = {},
                isSaved = false,
            )
        }
    }
}
