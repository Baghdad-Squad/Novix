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
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.onBoarding.OnBoardingEffect
import com.baghdad.viewmodel.onBoarding.OnBoardingInfo
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
    val onBoardingInfo: List<OnBoardingInfo> = listOf(
        OnBoardingInfo(
            imageIndex = com.baghdad.ui.R.drawable.img_on_boarding_1,
            title = R.string.discover_best_movies_and_series,
            description = R.string.browse_the_latest_releases_trends_and_content_tailored_to_your_taste_all_in_one_place
        ),
        OnBoardingInfo(
            imageIndex = com.baghdad.ui.R.drawable.img_on_boarding_2,
            title = R.string.search_easily_and_watch_to_your_taste,
            description = R.string.use_smart_search_and_filters_to_find_exactly_what_you_love_action_drama_crime_anime_and_more
        ),
        OnBoardingInfo(
            imageIndex = com.baghdad.ui.R.drawable.img_on_boarding_3,
            title = R.string.rate_save_and_create_your_own_lists,
            description = R.string.rate_movies_track_your_viewing_history_and_easily_save_your_favorite_lists
        ),
    )
    val pagerState =
        rememberPagerState(initialPage = state.currentPage, pageCount = {onBoardingInfo.size })


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
                onBoardingInfo = onBoardingInfo,
                modifier = Modifier.fillParentMaxWidth().fillMaxSize(),
                onNext = { listener.onNextButtonClick(onBoardingInfo.size) },
                onBack = {listener.onBackButtonClick()},
            )
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
