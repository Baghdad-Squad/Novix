package com.baghdad.ui.feature.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.feature.component.islamicImage.IslamicImage

@Composable
fun ActorImageDialog(
    selectedImage: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = modifier
                .background(
                    color = Theme.color.surface,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            IslamicImage(
                imageUrl = selectedImage,
                contentDescription = stringResource(com.baghdad.ui.R.string.actor_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 240.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Box(
                modifier = Modifier
                    .offset(y = -(8).dp, x = (8).dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(color = Theme.color.primary)
                    .size(28.dp)
                    .border(
                        width = 1.dp,
                        color = Theme.color.stroke,
                        shape = CircleShape
                    )
                    .noRippleClickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(R.string.close_bottomsheet),
                    tint = Theme.color.title,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}