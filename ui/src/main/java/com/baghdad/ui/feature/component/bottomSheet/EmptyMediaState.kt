package com.baghdad.ui.feature.component.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun EmptyMediaState(
    imagePath: Int,
    contentDescription: String,
    message: String,
    modifier: Modifier = Modifier,
    imgSize: Dp = 100.dp,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = imagePath),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(imgSize)
                .padding(bottom = 24.dp)
        )
        Text(
            text = message,
            style = Theme.typography.body.small,
            color = Theme.color.body,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .fillMaxWidth()
        )
    }
}

@NovixPreviews
@Composable
private fun EmptySearchScreenPreview() {
    NovixTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface),
            verticalArrangement = Arrangement.Center
        ) {
            EmptyMediaState(
                imagePath = R.drawable.user_person_profile,
                contentDescription = "",
                message = "Please login to rate your favorite items."
            )
        }
    }
}

