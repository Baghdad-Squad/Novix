package com.baghdad.screen

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.component.HorizontalDivider
import com.baghdad.component.MoviesCard
import com.baghdad.component.RecentSearchContent
import com.baghdad.component.SectionHeaderWithAction
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun SearchScreen(
    imageUrl: String,
    contentDescription: String,
    isSaved: Boolean,
    onSavedClick: () -> Unit,
    onClearAllClick: () -> Unit
) {
    val recentSearches = listOf("Shutter Island", "Seven", "Th", "The prestige", "Spider man")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        item {
            SectionHeaderWithAction(
                title = stringResource(R.string.recent_viewed),
                onClearAllClick = { onClearAllClick() }
            )
        }
        item {
            MoviesCard(
                url = imageUrl,
                contentDescription = contentDescription,
                isSaved = isSaved,
                onSavedClick = { onSavedClick() },
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillParentMaxWidth(0.45f)
                    .aspectRatio(0.8f)
            )
        }
        item {
            SectionHeaderWithAction(
                title = stringResource(R.string.recent_search),
                onClearAllClick = { onClearAllClick() })
        }
        itemsIndexed(recentSearches) { index, title ->
            RecentSearchContent(
                title = title,
                onCancelClick = { onClearAllClick() }
            )

            if (index < recentSearches.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 2.dp),
                         thickness = 1.dp,
                         color = Theme.color.stroke
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchScreenPreview() {
        SearchScreen(
            imageUrl = "",
            contentDescription = "",
            isSaved = false,
            onSavedClick = {},
            onClearAllClick = {}

        )
}