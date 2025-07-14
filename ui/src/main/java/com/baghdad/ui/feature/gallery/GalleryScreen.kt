package com.baghdad.ui.feature.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.baghdad.viewmodel.gallery.GalleryInteractionListener
import com.baghdad.viewmodel.gallery.GalleryScreenState

@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    uiState: GalleryScreenState,
    listener: GalleryInteractionListener,
    content: @Composable () -> Unit = {}
) {
    content()
}




