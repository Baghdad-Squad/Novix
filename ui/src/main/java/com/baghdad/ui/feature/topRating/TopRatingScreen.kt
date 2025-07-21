package com.baghdad.ui.feature.topRating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.R
import com.baghdad.design_system.component.HomeCard
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.feature.topRating.component.GenresSection
import com.baghdad.viewmodel.topRating.TopRatingInteractionListener
import com.baghdad.viewmodel.topRating.TopRatingMovieState
import com.baghdad.viewmodel.topRating.TopRatingViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun TopRatingMoviesScreen(
    viewModel: TopRatingViewModel = koinViewModel(parameters = {parametersOf()}),
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TopRatingMoviesContent(
        uiState = uiState,
        listener = viewModel,
        genreFilter = uiState.moviesByGenreFilter.moviesFilter
    )

}


@Composable
fun TopRatingMoviesContent(
    uiState: TopRatingMovieState,
    listener : TopRatingInteractionListener,
    genreFilter: TopRatingMovieState.MovieFilter,

    ) {
    Scaffold (
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.color.surface)
                    .statusBarsPadding()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    icon = painterResource(R.drawable.ic_go_back),
                    onClick = {  },
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 8.dp)
                )
                Text(
                    text = stringResource(com.baghdad.ui.R.string.top_rating),
                    style = Theme.typography.title.large,
                    color = Theme.color.title,
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                )
            }
            GenresSection(
                allGenres = genreFilter.allGenres,
                selectedGenres = genreFilter.selectedGenres,
                onGenreSelected = { listener.onGenreClick(it.id) },
                modifier = Modifier
                    .background(Theme.color.surface)
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp)
            )
        }
    ){
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize()) {
                WavyLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(uiState.movies) { movie ->
                HomeCard(
                    url = movie.posterPictureURL,
                    contentDescription = null,
                    isSaved = movie.isSaved,
                    onSavedClick = { listener.onSaveMovieClick(movie.id) },
                    onClick = { listener.onMovieDetailsClick(movie.id) },
                    modifier = Modifier.aspectRatio(0.8f)
                )
            }
        }
    }
}

@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun x() {
    TopRatingMoviesScreen()
}