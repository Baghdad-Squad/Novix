package com.baghdad.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.HomeCard
import com.baghdad.ui.search_screen.fake.data.getFakeTvShows

@Composable
fun TvShowCardList() {
    val tvShows = getFakeTvShows()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        items(tvShows) { tvShow ->
            HomeCard(
                url = tvShow.posterUrl,
                contentDescription = tvShow.title,
                isSaved = tvShow.isSaved,
                onSavedClick = { },
                modifier = Modifier.aspectRatio(0.8f)
            )
        }
    }
}