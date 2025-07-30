package com.baghdad.design_system.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme


@Composable
fun Tab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedTitleStyle = Theme.typography.label.medium.copy(
        color = Theme.color.title, textAlign = TextAlign.Center
    )
    val unSelectedTitleStyle = Theme.typography.label.medium.copy(
        color = Theme.color.hint, textAlign = TextAlign.Center
    )
    val titleColor by remember(isSelected) {
        derivedStateOf {
            if (isSelected) {
                selectedTitleStyle
            } else {
                unSelectedTitleStyle
            }
        }
    }

    AnimatedUnderlineWrapper(
        isSelected = isSelected,
        color = Theme.color.primary,
        modifier = modifier,
    ) { underlineModifier ->
        Row(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .animateContentSize(tween(ANIMATION_DURATION))
                .noRippleClickable { onClick() }
                .then(underlineModifier),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Text(
                text = text,
                style = titleColor,
                maxLines = 1
            )
        }
    }
}


@Composable
private fun AnimatedUnderlineWrapper(
    isSelected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    animationSpec: AnimationSpec<Float> = tween(ANIMATION_DURATION),
    content: @Composable (modifier: Modifier) -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = animationSpec,
        label = "underline_size_scale"
    )

    val underlinePath = remember { Path() }
    content(
        modifier.drawBehind {
            if (animatedScale > 0f) {
                val underlineWidth = size.width * animatedScale
                val underlineHeight = 4.dp.toPx() * animatedScale
                val startX = (size.width - underlineWidth) / 2f
                val startY = size.height - underlineHeight

                val rect = RoundRect(
                    rect = Rect(
                        offset = Offset(startX, startY),
                        size = Size(underlineWidth, underlineHeight)
                    ),
                    topLeft = CornerRadius(12.dp.toPx()),
                    topRight = CornerRadius(12.dp.toPx()),
                    bottomLeft = CornerRadius(12.dp.toPx()),
                    bottomRight = CornerRadius(12.dp.toPx())
                )
                underlinePath.reset()
                underlinePath.addRoundRect(rect)
                drawPath(
                    path = underlinePath, color = color
                )
            }
        }

    )
}

private const val ANIMATION_DURATION = 200

@Preview
@Composable
private fun TabsPreview() {
    NovixTheme {
        Tab(text = "Tab 1", isSelected = true, onClick = {})
    }
}