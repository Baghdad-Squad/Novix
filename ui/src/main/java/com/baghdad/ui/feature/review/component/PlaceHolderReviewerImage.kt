package com.baghdad.ui.feature.review.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.theme.Theme

@Composable
fun PlaceHolderReviewerImage() {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.img_reviewer_default),
            contentDescription = stringResource(com.baghdad.ui.R.string.default_image),
            tint = Theme.color.hint,
            modifier = Modifier.size(28.dp)
        )
    }
}