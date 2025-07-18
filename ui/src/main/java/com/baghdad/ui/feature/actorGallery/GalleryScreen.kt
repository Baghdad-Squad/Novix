package com.baghdad.ui.feature.actorGallery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.modifier.dropShadow
import com.baghdad.design_system.theme.Theme
import com.baghdad.islamic_image_loader.component.SafeImage
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent
import com.baghdad.viewmodel.actorGallery.ActorGalleryInteractionListener
import com.baghdad.viewmodel.actorGallery.ActorGalleryScreenEffect
import com.baghdad.viewmodel.actorGallery.ActorGalleryScreenState
import com.baghdad.viewmodel.actorGallery.ActorGalleryViewModel
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun GalleryScreen(
    actorId: Long,
    viewModel: ActorGalleryViewModel = koinViewModel(
        key = actorId.toString(),
        parameters = { parametersOf(actorId) }),
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
        snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible
            )
        }, topBar = {
            TopAppBar(
                onGoBackClick = listener::onBackClick,
                screenTitle = stringResource(R.string.gallery),
            ) {}
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface)
                .dropShadow(
                    color = Theme.color.primary,
                    alpha = 0.08f,
                    offsetX = (-210).dp,
                    offsetY = (18).dp,
                    shape = RectangleShape,
                    blur = 336.dp
                )
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(top = 12.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(104.dp),
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
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}




