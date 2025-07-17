package com.baghdad.ui.feature.actorDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.AutoSlidingImageCarousel
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.feature.actorDetails.component.ActorBiographySection
import com.baghdad.ui.feature.actorDetails.component.ActorCardDetails
import com.baghdad.ui.feature.actorDetails.component.GallerySection
import com.baghdad.ui.feature.actorDetails.component.TopMediaPicksSection
import com.baghdad.viewmodel.actorDetails.ActorDetailsInteractionListener
import com.baghdad.viewmodel.actorDetails.ActorDetailsScreenState

@Composable
fun ActorDetailsContent(
    uiState: ActorDetailsScreenState,
    listener: ActorDetailsInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Theme.color.surface)
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        Box {
            AutoSlidingImageCarousel(
                imageUrls = uiState.actorInfo.headerPictures,
                modifier = Modifier.padding(bottom = 104.dp)
            )

            IconButton(
                icon = painterResource(com.baghdad.design_system.R.drawable.ic_go_back),
                onClick = { listener.onBackIconClick() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 60.dp)
            )

            ActorCardDetails(
                fullName = uiState.actorInfo.name,
                characterRole = uiState.actorInfo.department,
                birthPlace = uiState.actorInfo.placeOfBirth,
                birthDate = uiState.actorInfo.birthdayDate,
                deathDate = uiState.actorInfo.deathDate,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
            )
        }

        if (!uiState.actorInfo.biography.isNullOrBlank()) {
            ActorBiographySection(
                biography = uiState.actorInfo.biography,
                onExpandedChange = { listener.onReadMoreBiographyClick() },
                isExpanded = uiState.isTextExpanded,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (uiState.gallery.isNotEmpty()) {
            GallerySection(
                imageUrls = uiState.gallery,
                onClickShowAll = { listener.onViewAllGalleryClick() },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (uiState.topMoviesPicks.isNotEmpty()) {
            TopMediaPicksSection(
                title = stringResource(com.baghdad.ui.R.string.top_movies_picks),
                items = uiState.topMoviesPicks,
                imageUrl = { it.posterPictureURL },
                onSavedClick = { listener.onSaveMovieClick(it.id) },
                onCardClick = { listener.onMovieCardClick(it.id) },
                isSaved = { it.isSaved },
                onClickShowAll = { listener.onViewAllTopMoviesPicksClick() },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        if (uiState.topTvShowsPicks.isNotEmpty()) {
            TopMediaPicksSection(
                title = stringResource(com.baghdad.ui.R.string.top_tv_shows_picks),
                items = uiState.topTvShowsPicks,
                imageUrl = { it.posterPictureURL },
                onSavedClick = { listener.onSaveTvShowClick(it.id) },
                onCardClick = { listener.onTvShowCardClick(it.id) },
                isSaved = { it.isSaved },
                onClickShowAll = { listener.onViewAllTopTvShowsClick() },
            )
        }
    }
}

@NovixPreviews
@Composable
private fun ActorDetailsScreenPrev() {
    NovixTheme {
        ActorDetailsContent(
            uiState = ActorDetailsScreenState(),
            listener = object : ActorDetailsInteractionListener {
                override fun onReadMoreBiographyClick() {}
                override fun onViewAllGalleryClick() {}
                override fun onViewAllTopMoviesPicksClick() {}
                override fun onViewAllTopTvShowsClick() {}
                override fun onBackIconClick() {}
                override fun onSaveMovieClick(movieId: Long) {}
                override fun onSaveTvShowClick(tvShowId: Long) {}
                override fun onMovieCardClick(movieId: Long) {}
                override fun onTvShowCardClick(tvShowId: Long) {}
            }
        )
    }
}