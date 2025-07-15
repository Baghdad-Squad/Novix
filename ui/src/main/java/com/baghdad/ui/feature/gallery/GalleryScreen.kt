package com.baghdad.ui.feature.gallery

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.islamic_image_loader.component.SafeImage
import com.baghdad.ui.R
import com.baghdad.viewmodel.gallery.GalleryScreenState

@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    uiState: GalleryScreenState,
    onBackClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            onGoBackClick = onBackClick,
            screenTitle = stringResource(R.string.gallery),
        ) {}

        ActorGalleryScreenContent(
            uiState = uiState
        )
    }

}

@Composable
fun ActorGalleryScreenContent(
    uiState: GalleryScreenState,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(uiState.images) { actorItem ->
            SafeImage(
                imageUrl = actorItem,
                contentDescription = stringResource(R.string.gallery),
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12))
                    .border(1.dp, Theme.color.stroke)
            )
        }
    }
}




