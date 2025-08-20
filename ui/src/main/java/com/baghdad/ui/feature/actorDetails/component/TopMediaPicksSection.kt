package com.baghdad.ui.feature.actorDetails.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.baghdad.design_system.component.SectionHeader
import com.baghdad.ui.feature.component.HorizontalMediaCardList

@Composable
fun <T> TopMediaPicksSection(
    title: String,
    items: List<T>,
    imageUrl: (T) -> String,
    onCardClick: (T) -> Unit,
    isSaved: (T) -> Boolean,
    isShowAllVisible: Boolean,
    modifier: Modifier = Modifier,
    onSavedClick: (T) -> Unit = {},
    onClickShowAll: () -> Unit,
    isSaveVisible: (T) -> Boolean = { true },
) {

    Column(modifier = modifier) {
        SectionHeader(
            title = title,
            isShowAllVisible = isShowAllVisible,
            onClick = onClickShowAll,
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalMediaCardList(
            items = items,
            imageUrl = { imageUrl(it) },
            onSavedClick = { onSavedClick(it) },
            onCardClick = { onCardClick(it) },
            isSaved = { isSaved(it) },
            isSaveVisible = isSaveVisible
        )
    }
}