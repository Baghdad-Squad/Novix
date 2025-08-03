package com.baghdad.viewmodel.onBoarding

import androidx.annotation.StringRes
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseUiState

data class OnBoardingState(
    val initPage : Int = 0,
    val currentPage: Int = 0,
    val onBoardingInfo: List<OnBoardingInfo> = listOf(
        OnBoardingInfo(
            imageIndex = 0,
            title = R.string.discover_best_movies_and_series,
            description = R.string.browse_the_latest_releases_trends_and_content_tailored_to_your_taste_all_in_one_place
        ),
        OnBoardingInfo(
            imageIndex = 1,
            title = R.string.search_easily_and_watch_to_your_taste,
            description = R.string.use_smart_search_and_filters_to_find_exactly_what_you_love_action_drama_crime_anime_and_more
        ),
        OnBoardingInfo(
            imageIndex = 2,
            title = R.string.rate_save_and_create_your_own_lists,
            description = R.string.rate_movies_track_your_viewing_history_and_easily_save_your_favorite_lists
        ),



    ),
): BaseUiState

data class OnBoardingInfo(
    val imageIndex: Int,
    @StringRes val title: Int ,
    @StringRes val description: Int,
)

