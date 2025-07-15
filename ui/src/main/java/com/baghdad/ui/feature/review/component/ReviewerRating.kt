package com.baghdad.ui.feature.review.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import java.util.Locale

private val iconSize = 12.dp
private val spacerBetweenElements = 4.dp
private val elementsPaddingHorizontal = 12.dp
private val elementsPaddingVertical = 12.dp

@Composable
fun ReviewerRating(
    rating: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = elementsPaddingHorizontal,
                vertical = elementsPaddingVertical
            ),
        horizontalArrangement = Arrangement.spacedBy(spacerBetweenElements),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_rating_star),
            contentDescription = stringResource(R.string.rating_star),
            tint = Theme.color.yellowAccent,
            modifier = Modifier.size(iconSize)
        )

        Text(
            text = String.format(Locale.getDefault(), "%.1f", rating),
            style = Theme.typography.label.small,
            color = Theme.color.title
        )

    }
}

@Preview
@Composable
private fun ReviewRattingPreview() {
    NovixTheme(isDarkTheme = true) {
        Column(
            modifier = Modifier
                .background(color = Theme.color.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReviewerRating(rating = 9.9111f)
        }
    }
}