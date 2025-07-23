package com.baghdad.ui.feature.trendingActors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.ActorCard
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.people.PeopleInteractionListener
import com.baghdad.viewmodel.people.PeopleUiEffect
import com.baghdad.viewmodel.people.TrendingActorUiState
import com.baghdad.viewmodel.people.TrendingActorViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrendingActorsScreen(
    viewModel: TrendingActorViewModel = koinViewModel(),
    handleNavigation: (HomeNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    TrendingActorsContent(
        uiState = uiState,
        listner = viewModel,
        snackBarState = snackBarState
    )

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is PeopleUiEffect.OnBackClick -> {
                handleNavigation(HomeNavEvent.NavigateBack)
            }

            is PeopleUiEffect.NavigateToActorDetails -> {
                handleNavigation(HomeNavEvent.NavigateToActorDetails(effect.actorId))
            }
        }
    }
}


@Composable
fun TrendingActorsContent(
    uiState: TrendingActorUiState,
    listner: PeopleInteractionListener,
    snackBarState: SnackBarState
) {
    val trendingActors = uiState.trendingActor.collectAsLazyPagingItems()

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
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface)
                .statusBarsPadding()
        ) {
            TopAppBar(
                modifier = Modifier.padding(top = 12.dp),
                onGoBackClick = listner::onBackClick,
                screenTitle = stringResource(R.string.actors),
            ) {}


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(trendingActors.itemCount) { index ->
                    trendingActors[index]?.let { actor ->
                        ActorCard(
                            actorName = actor.name,
                            actorImage = actor.profilePictureURL,
                            onClick = { listner.onPeopleClick(actor.id) },
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}