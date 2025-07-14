package com.baghdad.ui.feature.gallery

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.baghdad.viewmodel.gallery.GalleryInteractionListener
import com.baghdad.viewmodel.gallery.GalleryScreenState

@Composable
fun GalleryScreen(
    uiState: GalleryScreenState,
    listener: GalleryInteractionListener,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        TopAppBar(
            modifier = modifier.padding(top = 12.dp),
            onGoBackClick = listener::onBackClick,
            screenTitle = stringResource(R.string.gallery),
        ) {}
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.images) { imageActor ->
                    SafeImage(
                        imageUrl = imageActor,
                        contentDescription = stringResource(R.string.gallery),
                        modifier = Modifier
                            .clickable { /*TODO*/ }
                            .size(100.dp)
                            .clip(RoundedCornerShape(12))
                            .border(
                                1.dp,
                                Theme.color.stroke
                            ),
                    )
                }
            }
        }
    }



