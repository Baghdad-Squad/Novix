package com.baghdad.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.baghdad.design_system.component.Chip
import com.baghdad.ui.R
import com.baghdad.ui.model.SearchTab
import com.baghdad.component.ActorCardList
import com.baghdad.component.MovieCardList
import com.baghdad.component.TvShowCardList
import com.baghdad.ui.search_screen.fake.data.getFakeActors


@Composable
fun ListSearchScreen(
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(SearchTab.MOVIES) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.color.surface),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(37.dp)
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Chip(
                    title = stringResource(id = R.string.tab_movies),
                    isSelected = selectedTab == SearchTab.MOVIES,
                    onClick = { selectedTab = SearchTab.MOVIES }
                )
                Chip(
                    title = stringResource(id = R.string.tab_tv_shows),
                    isSelected = selectedTab == SearchTab.TV_SHOWS,
                    onClick = { selectedTab = SearchTab.TV_SHOWS }
                )
                Chip(
                    title = stringResource(id = R.string.tab_actors),
                    isSelected = selectedTab == SearchTab.ACTORS,
                    onClick = { selectedTab = SearchTab.ACTORS }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        when (selectedTab) {
            SearchTab.MOVIES -> {
                item { MovieCardList() }
            }

            SearchTab.TV_SHOWS -> {
                item { TvShowCardList() }
            }

            SearchTab.ACTORS -> {
                item { ActorCardList(actors = getFakeActors()) }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SearchScreenPreview() {
    ListSearchScreen()
}