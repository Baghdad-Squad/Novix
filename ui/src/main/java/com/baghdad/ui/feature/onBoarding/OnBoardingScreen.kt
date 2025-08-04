package com.baghdad.ui.feature.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.feature.onBoarding.component.BottomSlidingSection
import com.baghdad.ui.feature.onBoarding.component.OnBoardingHorizontalPagerContent
import com.baghdad.ui.feature.onBoarding.component.SkipText
import com.baghdad.ui.navigation.graph.onBoarding.OnBoardingNavEvent
import com.baghdad.viewmodel.onBoarding.OnBoardingEffect
import com.baghdad.viewmodel.onBoarding.OnBoardingInteractionListener
import com.baghdad.viewmodel.onBoarding.OnBoardingState
import com.baghdad.viewmodel.onBoarding.OnBoardingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    handleNavigation: (OnBoardingNavEvent) -> Unit,
) {

    val state by viewModel.uiState.collectAsState()
    val listener: OnBoardingInteractionListener = viewModel

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }

    OnBoardingContent(
        state = state,
        listener = listener,
    )

}


@Composable
private fun OnBoardingContent(
    state: OnBoardingState,
    listener: OnBoardingInteractionListener,
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val pagerState =
        rememberPagerState(initialPage = state.currentPage, pageCount = { state.onBoardingInfo.size })


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surface)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        item {
            SkipText(onClick = { listener.onSkipButtonClick() })
        }
        item {
            OnBoardingHorizontalPagerContent(
                pagerState = pagerState,
                state = state,
                modifier = Modifier.fillParentMaxWidth(),
                onNext = { listener.onNextButtonClick() },
                onBack = {listener.onBackButtonClick()},
            )
        }
        item {
            BottomSlidingSection(
                pagerState = pagerState,
                onClickNext = {
                    if (pagerState.currentPage < pagerState.pageCount - 1) {
                        coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        listener.onSkipButtonClick()
                    }
                },
                onClickBack = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage - 1
                        )

                    }
                }
            )
        }
    }

}

private fun handleEffect(
    effect: OnBoardingEffect,
    handleNavigation: (OnBoardingNavEvent) -> Unit,
) {
    when (effect) {
        OnBoardingEffect.NavigateToWelcomeToNovix -> {
            handleNavigation(
                OnBoardingNavEvent.NavigateToWelcome
            )
        }
    }

}
