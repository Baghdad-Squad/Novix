package com.baghdad.ui.feature.people

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.ActorCard
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.navigation.graph.people.PeopleNavEvent
import com.baghdad.viewmodel.people.PeopleInteractionListener
import com.baghdad.viewmodel.people.PeopleUiEffect
import com.baghdad.viewmodel.people.PeopleUiState
import com.baghdad.viewmodel.people.PeopleViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PeopleScreen(
    viewModel: PeopleViewModel = koinViewModel(), handleNavigation: (PeopleNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PeopleScreenContent(
        uiState = uiState, listner = viewModel
    )

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is PeopleUiEffect.OnBackClick -> {
                handleNavigation(PeopleNavEvent.NavigateBack)
            }

            is PeopleUiEffect.NavigateToActorDetails -> {
                handleNavigation(PeopleNavEvent.NavigateToActorDetails(effect.actorId))
            }
        }
    }
}


@Composable
fun PeopleScreenContent(
    uiState: PeopleUiState, listner: PeopleInteractionListener
) {
    val peopleItems = uiState.people.collectAsLazyPagingItems()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Theme.color.surface)
        .statusBarsPadding()) {
        TopAppBar(
            modifier = Modifier.padding(top = 12.dp),
            onGoBackClick = listner::onBackClick,
            screenTitle = stringResource(R.string.people),
        ) {}


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(peopleItems.itemCount) { index ->
                peopleItems[index]?.let { person ->
                    ActorCard(
                        actorName = person.name,
                        actorImage = person.profilePictureURL,
                        onClick = { listner.onPeopleClick(person.id) },
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}