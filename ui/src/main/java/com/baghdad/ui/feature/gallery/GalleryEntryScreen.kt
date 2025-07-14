package com.baghdad.ui.feature.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.viewmodel.gallery.GalleryInteractionListener
import com.baghdad.viewmodel.gallery.GalleryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GalleryEntryScreen(
    viewModel: GalleryViewModel = koinViewModel(),
    listener: GalleryInteractionListener

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GalleryScreen(
        uiState = uiState,
        listener = listener
    )

}