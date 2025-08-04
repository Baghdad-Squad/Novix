package com.baghdad.ui.feature.onBoarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun SkipText(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.skip),
        color = Theme.color.primary,
        style = Theme.typography.label.medium,
        modifier = Modifier.padding(top = 56.dp)
    )
    Box(
        modifier = Modifier
            .offset(x = (-50).dp, y = (-100).dp)
            .size(200.dp)
            .blur(
                radiusX = 250.dp,
                radiusY = 250.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            )
            .background(Theme.color.primary.copy(0.4f), shape = RoundedCornerShape(100))
            .noRippleClickable { onClick() }
    )
}