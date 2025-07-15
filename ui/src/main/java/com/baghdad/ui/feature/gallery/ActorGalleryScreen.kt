package com.baghdad.ui.feature.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.viewmodel.gallery.GalleryScreenEffect
import com.baghdad.viewmodel.gallery.GalleryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActorGalleryScreen(
    viewModel: GalleryViewModel = koinViewModel(),
    actorId: Long,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getActorGalleryImages(actorId)
    }
    GalleryScreen(
        uiState = uiState,
        onBackClick = { viewModel.onBackClick() },
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
