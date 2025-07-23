package com.baghdad.ui.feature.component.islamicImage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme

@Composable
fun BlurImageContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_eye),
            contentDescription = stringResource(R.string.unsuitable_image),
            tint = Theme.color.body,
            modifier = Modifier
                .size(24.dp)
                .padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(R.string.unsuitable_image),
            style = Theme.typography.label.small,
            color = Theme.color.body,
            textAlign = TextAlign.Center
        )
    }
}