package com.baghdad.ui.feature.actorGallery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.ActorImageDialog
import com.baghdad.ui.feature.component.islamicImage.IslamicImage
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent
import com.baghdad.viewmodel.actorGallery.ActorGalleryInteractionListener
import com.baghdad.viewmodel.actorGallery.ActorGalleryScreenEffect
import com.baghdad.viewmodel.actorGallery.ActorGalleryScreenState
import com.baghdad.viewmodel.actorGallery.ActorGalleryViewModel
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage


@Composable
fun GalleryScreen(
    viewModel: ActorGalleryViewModel = hiltViewModel(),
    handleNavigation: (ActorDetailsNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()

    ActorGalleryScreenContent(
        uiState = uiState, listener = viewModel, snackBarState = snackBarState
    )
    ObserveAsEffect(effect = viewModel.uiEffect) { effect ->
        when (effect) {
            is ActorGalleryScreenEffect.OnBackClick -> {
                handleNavigation(ActorDetailsNavEvent.NavigateBack)
            }
        }
    }
}

@Composable
fun ActorGalleryScreenContent(
    uiState: ActorGalleryScreenState,
    listener: ActorGalleryInteractionListener,
    snackBarState: SnackBarState
) {
    Scaffold(
        modifier = Modifier
            .background(Theme.color.surface)
            .systemBarsPadding()
            .statusBarsPadding(),
        isLoading = uiState.isLoading,
        snackbar = { position ->
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = listener::onSnackBarActionLabelClick,
                position = position,
            )
        },
        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null,
        topBar = {
            TopAppBar(
                onGoBackClick = listener::onBackClick,
                screenTitle = stringResource(R.string.gallery),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(top = 12.dp)
            ) {}
        },
        backgroundBlur = {
            BackgroundBlur()
        }) {
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize()) {
                WavyLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(104.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.images) { actorItem ->
                    IslamicImage(
                        imageUrl = actorItem,
                        contentDescription = stringResource(R.string.gallery),
                        modifier = Modifier
                            .size(104.dp)
                            .clip(RoundedCornerShape(12))
                            .border(1.dp, Theme.color.stroke)
                            .clickable{ listener .onGalleryImageClick(actorItem)}
                    )
                }
            }
            if (uiState.selectedImage.isNotBlank()) {
                ActorImageDialog(
                    selectedImage = uiState.selectedImage,
                    onDismiss = { listener.onImageDialogDismiss() })
            }

        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}




