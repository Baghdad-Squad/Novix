package com.baghdad.ui.feature.savedListDetails.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun EmptyListScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 52.dp)
        ) {
            Icon(
                painter = showIconDependsOnTheme(),
                contentDescription = stringResource(R.string.empty_list),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = stringResource(R.string.there_is_no_items_here),
                color = Theme.color.body,
                textAlign = TextAlign.Center,
                style = Theme.typography.body.small,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
            )
        }
    }
}

@Composable
private fun showIconDependsOnTheme(): Painter = if (Theme.isDarkTheme) {
    painterResource(R.drawable.img_empty_list_night)
} else {
    painterResource(R.drawable.img_empty_list)
}