package com.baghdad.ui.feature.myRating.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.viewmodel.myRating.MyRatingState

@Composable
fun MyRatingVerticalGrid(
    mediaItems: LazyPagingItems<MyRatingState.MediaItemUiState>,
    onMediaClick : (mediaId: Long, contentType: MyRatingState.ContentType) -> Unit,
    onDeleteClick: (mediaId: Long, contentType: MyRatingState.ContentType) -> Unit,
) {
    LazyPagingVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 12.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        items = mediaItems,
    ) { media ->
        RatingCard(
            url = media.posterPictureURL,
            rating = media.rating,
            contentDescription = null,
            onClick = {onMediaClick(media.id, media.contentType) },
            onDeleteClick = { onDeleteClick(media.id, media.contentType) }
        )
    }
}