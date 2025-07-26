package com.baghdad.ui.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun WhatToWatchSection(
    onMoviesClick: () -> Unit,
    onTvShowsClick: () -> Unit,
    onActorsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val isDark = isSystemInDarkTheme()

    val gradientMoviesColors = if (isDark) {
        listOf(Theme.color.primary, Color(0xFFC65A42), Color(0xff4D1D12))
    } else {
        listOf(Theme.color.primary, Color(0xffBF4C33))
    }

    val gradientTvShowsColors = if (isDark) {
        listOf(Color(0xff4B0412), Color(0xff39010C))
    } else {
        listOf(Color(0xff80071F), Color(0xff5B0113))
    }

    val gradientActorsColors = if (isDark) {
        listOf(Color(0xff3B99AC), Color(0xff094E5C))
    } else {
        listOf(Color(0xff3B99AC), Color(0xff21606D))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(com.baghdad.ui.R.string.what_do_you_want_to_watch),
            style = Theme.typography.headline.small,
            color = Theme.color.title,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            WhatToWatchCard(
                image = painterResource(id = com.baghdad.design_system.R.drawable.im_popcorn),
                title = stringResource(com.baghdad.ui.R.string.movies),
                colors = gradientMoviesColors,
                imageWidth = 60.dp,
                onClick = onMoviesClick,
                modifier = Modifier.weight(1f)
            )

            WhatToWatchCard(
                image = painterResource(id = com.baghdad.design_system.R.drawable.im_tape),
                title = stringResource(com.baghdad.ui.R.string.tv_shows),
                colors = gradientTvShowsColors,
                imageWidth = 90.dp,
                onClick = onTvShowsClick,
                modifier = Modifier.weight(1f)
            )

            WhatToWatchCard(
                image = painterResource(id = com.baghdad.design_system.R.drawable.im_cast),
                title = stringResource(com.baghdad.ui.R.string.actors),
                colors = gradientActorsColors,
                imageWidth = 70.dp,
                onClick = onActorsClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@NovixPreviews
@Composable
private fun WhatToWatchSectionPrev() {
    NovixTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Theme.color.surface),
            verticalArrangement = Arrangement.Center
        ) {
            WhatToWatchSection({}, {}, {})
        }
    }
}