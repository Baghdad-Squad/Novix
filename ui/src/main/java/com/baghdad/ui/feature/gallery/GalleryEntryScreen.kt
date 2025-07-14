package com.baghdad.ui.feature.gallery

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.theme.Theme
import com.baghdad.islamic_image_loader.component.SafeImage
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.viewmodel.gallery.GalleryInteractionListener
import com.baghdad.viewmodel.gallery.GalleryScreenEffect
import com.baghdad.viewmodel.gallery.GalleryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GalleryEntryScreen(
    viewModel: GalleryViewModel = koinViewModel(),
    listener: GalleryInteractionListener,
    onBackClick: () -> Unit

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GalleryScreen(
        uiState = uiState,
        listener = listener
    ) {
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
                ObserveAsEffect(
                    effect = viewModel.uiEffect,
                    onEvent = { effect ->
                        when (effect) {
                            GalleryScreenEffect.OnBackClick -> onBackClick()
                        }
                    }
                )

            }

        }

    }
}