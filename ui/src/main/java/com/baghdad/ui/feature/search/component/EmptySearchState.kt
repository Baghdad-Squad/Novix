package com.baghdad.ui.feature.search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun EmptySearchState(
    imagePath: Int,
    contentDescription: String,
    message: String,
    modifier: Modifier = Modifier
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
                .size(128.dp)
        )
        Text(
            text = message,
            style = Theme.typography.body.small,
            color = Theme.color.body,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 48.dp)
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
            EmptySearchState(
                imagePath = R.drawable.start_explore_night,
                contentDescription = "Search",
                message = "Start exploring! Search for your favorite movies, series and shows"
            )
        }
    }

}

