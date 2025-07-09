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
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.Theme



@Composable
fun SnackBar(
    message: String,
    modifier: Modifier = Modifier,
    isSuccess: Boolean = true,
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
                .background(color = Theme.color.surface)
                .border(
                    width = 1.dp,
                    color = Theme.color.stroke,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SnackBarIcon(
                isSuccess = isSuccess,
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
private fun SnackBarIcon(
    isSuccess: Boolean,
    modifier: Modifier
) {
    val icon = if (isSuccess) R.drawable.ic_tick_double else R.drawable.ic_alert
    val description = if (isSuccess) "Success icon" else "Error icon"
    val tintColor = if (isSuccess) Theme.color.greenAccent else Theme.color.redAccent

    Icon(
        painter = painterResource(id = icon),
        contentDescription = description,
        tint = tintColor,
        modifier = modifier
    )
}


@Preview(showBackground = true,backgroundColor = 0xFF0D0608)
@Composable
fun PreviewSuccessSnackBarDark() {
    SnackBar(
        message = "Rate submitted successfully.",
        isVisible = true,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewSuccessSnackBarLight() {
    SnackBar(
        message = "Rate submitted successfully.",
        isVisible = true,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0608)
@Composable
fun PreviewErrorSnackBarDark() {
    SnackBar(
        message = "Some error happened",
        isVisible = true,
        isSuccess = false,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewErrorSnackBarLight() {
    SnackBar(
        message = "Some error happened",
        isVisible = true,
        isSuccess = false,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0608)
@Composable
fun PreviewBothSnackBarsDark() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SnackBar(
            message = "Rate submitted successfully.",
            isSuccess = true
        )
        SnackBar(
            message = "Some error happened",
            isSuccess = false
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewBothSnackBarsLight() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SnackBar(
            message = "Rate submitted successfully.",
            isSuccess = true
        )
        SnackBar(
            message = "Some error happened",
            isSuccess = false
        )
    }
}