package com.baghdad.ui.feature.onBoarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.scaffold.Scaffold
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.feature.onBoarding.component.BottomSlidingSection
import com.baghdad.ui.feature.onBoarding.component.OnBoardingHorizontalPagerContent
import com.baghdad.ui.feature.onBoarding.component.SkipText
import com.baghdad.ui.feature.onBoarding.component.isTablet
import com.baghdad.ui.navigation.graph.onBoarding.OnBoardingNavEvent
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.onBoarding.OnBoardingEffect
import com.baghdad.viewmodel.onBoarding.OnBoardingInfo
import com.baghdad.viewmodel.onBoarding.OnBoardingInteractionListener
import com.baghdad.viewmodel.onBoarding.OnBoardingState
import com.baghdad.viewmodel.onBoarding.OnBoardingViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
    val onBoardingInfo: List<OnBoardingInfo> = remember {
        listOf(
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
    }
    val pagerState = rememberPagerState { onBoardingInfo.size }

    val scope = rememberCoroutineScope()

    val systemUiController = rememberSystemUiController()

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage > state.currentPage) {
            listener.onNextButtonClick(onBoardingInfo.size)
        } else {
            listener.onBackButtonClick()
        }
    }
    Scaffold(
        backgroundContent = { BackgroundBlur() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            item {
                SkipText(
                    onClick = { listener.onSkipButtonClick() },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                OnBoardingHorizontalPagerContent(
                    modifier = if (isTablet()) Modifier.offset(y = (-64).dp) else Modifier,
                    pagerState = pagerState,
                    onBoardingInfo = onBoardingInfo,
                )

            }
            item {

                BottomSlidingSection(
                    pagerState = pagerState,
                    onClickNext = {
                        scope.launch {
                            val isLastPage = pagerState.currentPage >= onBoardingInfo.size - 1

                            if (isLastPage) {
                                listener.onNextButtonClick(onBoardingInfo.size)
                            } else {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    onClickBack = {
                        scope.launch {
                            if (pagerState.currentPage > 0) {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    },
                )
            }
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
