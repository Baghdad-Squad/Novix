package com.baghdad.design_system.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.Theme

enum class SnackBarType {
    SUCCESS,
    ERROR
}

@Composable
fun SnackBar(
    message: String,
    modifier: Modifier = Modifier,
    type: SnackBarType = SnackBarType.SUCCESS,
    isVisible: Boolean = true,
    animationDuration: Int = 500
) {

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(animationDuration)) + slideInVertically(
            animationSpec = tween(animationDuration),
            initialOffsetY = { -it }
        ),
        exit = fadeOut(tween(animationDuration)) + slideOutVertically(
            animationSpec = tween(animationDuration),
            targetOffsetY = { -it }
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = Theme.color.surface
                )
                .border(
                    width = 1.dp,
                    color = Theme.color.stroke,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = when (type) {
                        SnackBarType.SUCCESS -> R.drawable.ic_tick_double
                        SnackBarType.ERROR -> R.drawable.ic_alert
                    }
                ),
                contentDescription = when (type) {
                    SnackBarType.SUCCESS -> stringResource(R.string.success)
                    SnackBarType.ERROR -> stringResource(R.string.error)
                },
                tint = when (type) {
                    SnackBarType.SUCCESS -> Theme.color.greenAccent
                    SnackBarType.ERROR -> Theme.color.redAccent
                },
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = message,
                style = Theme.typography.body.medium,
                color = Theme.color.title,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
@Composable
fun SuccessSnackBar(
    modifier: Modifier = Modifier,
    message: String,
    isVisible: Boolean = true,
    animationDuration: Int = 500
) {
    SnackBar(
        modifier = modifier,
        message = message,
        type = SnackBarType.SUCCESS,
        isVisible = isVisible,
        animationDuration = animationDuration
    )
}

@Composable
fun ErrorSnackBar(
    modifier: Modifier = Modifier,
    message: String,
    isVisible: Boolean = true,
    animationDuration: Int = 500
) {
    SnackBar(
        modifier = modifier,
        message = message,
        type = SnackBarType.ERROR,
        isVisible = isVisible,
        animationDuration = animationDuration
    )
}

@Preview(showBackground = true,backgroundColor = 0xFF0D0608)
@Composable
fun PreviewSuccessSnackBarDark() {
    SuccessSnackBar(
        message = "Rate submitted successfully.",
        isVisible = true,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewSuccessSnackBarLight() {
    SuccessSnackBar(
        message = "Rate submitted successfully.",
        isVisible = true,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0608)
@Composable
fun PreviewErrorSnackBarDark() {
    ErrorSnackBar(
        message = "Some error happened",
        isVisible = true,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewErrorSnackBarLight() {
    ErrorSnackBar(
        message = "Some error happened",
        isVisible = true,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0608)
@Composable
fun PreviewBothSnackBarsDark() {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SuccessSnackBar(
            message = "Rate submitted successfully."
        )
        ErrorSnackBar(
            message = "Some error happened"
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewBothSnackBarsLight() {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SuccessSnackBar(
            message = "Rate submitted successfully."
        )
        ErrorSnackBar(
            message = "Some error happened"
        )
    }
}