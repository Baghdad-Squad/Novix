package com.baghdad.design_system.component

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun NovixBottomNavigationBar(
    items: List<BottomNavItem>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedIconIndex: Int = 0
) {
    val horizontalPadding = 25.dp
    val strokeColor = Theme.color.stroke

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(Theme.color.surface)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = 0f
                drawLine(
                    color = strokeColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        val iconCount = items.size
        val availableWidth = maxWidth - (horizontalPadding * 2)
        val slotWidth = availableWidth / iconCount
        val dotWidth = 2.dp

        val dotOffset by animateDpAsState(
            targetValue = horizontalPadding + (slotWidth * selectedIconIndex) + (slotWidth / 2) - dotWidth,
            animationSpec = spring(dampingRatio = 0.7f, stiffness = 800f),
            label = "dot_offset"
        )

        val shadowOffset by animateDpAsState(
            targetValue = horizontalPadding + (slotWidth * selectedIconIndex) + (slotWidth / 2) - 35.dp,
            animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
            label = "box_offset"
        )

        val largeRadialGradient = Brush.radialGradient(
            colors = listOf(
                Theme.color.primary.copy(alpha = 0.08f),
                Theme.color.primary.copy(alpha = 0.05f),
                Theme.color.surface.copy(alpha = 0.02f),
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = 14.dp),
        ) {
            items.forEachIndexed { index, item ->
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable { onClick(index) },
                    imageVector = ImageVector.vectorResource(
                        id = if (selectedIconIndex == index) item.selectedIcon else item.unselectedIcon
                    ),
                    contentDescription = stringResource(R.string.nav_bar_icon) + " $index",
                    tint = if (selectedIconIndex == index) Theme.color.primary else Theme.color.hint
                )
            }
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(x = dotOffset.roundToPx(), y = -16) }
                .padding(bottom = 6.dp)
                .size(4.dp)
                .clip(CircleShape)
                .background(Theme.color.primary)
                .blur(50.dp)
                .align(Alignment.BottomStart)
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(x = shadowOffset.roundToPx(), y = 0) }
                .size(70.dp)
                .clip(CircleShape)
                .scale(scaleX = 1.1f, scaleY = 1.0f)
                .background(largeRadialGradient)
                .align(Alignment.CenterStart)
        )
    }
}


data class BottomNavItem(
    @DrawableRes
    val selectedIcon: Int,
    @DrawableRes
    val unselectedIcon: Int
)

@NovixPreviews
@Composable
fun NovixBottomNavigationBarPreview() {
    NovixTheme {
        var selected by remember { mutableIntStateOf(0) }
        val items = remember {
            listOf(
                BottomNavItem(R.drawable.ic_home_filled, R.drawable.ic_home_outlined),
                BottomNavItem(R.drawable.ic_search_filled, R.drawable.ic_search_outlined),
                BottomNavItem(R.drawable.ic_masks_filled, R.drawable.ic_masks_outlined),
                BottomNavItem(R.drawable.ic_allbookmark_filled, R.drawable.ic_allbookmark_outlined),
                BottomNavItem(
                    R.drawable.ic_user_octagon_filled,
                    R.drawable.ic_user_octagon_outlined
                ),
            )
        }
        NovixBottomNavigationBar(
            items = items,
            onClick = {
                selected = it
            },
            modifier = Modifier,
            selectedIconIndex = selected
        )
    }
}