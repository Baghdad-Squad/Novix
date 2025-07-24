package com.baghdad.ui.feature.trendingMovies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.viewmodel.movie.TrendingMoviesInteractionListener
import com.baghdad.viewmodel.movie.TrendingMoviesScreenState
import com.baghdad.viewmodel.movie.TrendingMoviesViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun MoviesScreen(
    viewModel: TrendingMoviesViewModel = koinViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movieItems =
        uiState.movies.collectAsLazyPagingItems()
    TrendingMoviesContent(
        movieItems = movieItems,
        uiState = uiState,
        listener = viewModel,
    )

}

@Composable
private fun TrendingMoviesContent(
    movieItems: LazyPagingItems<TrendingMoviesScreenState.TrendingMovieUiState>,
    uiState: TrendingMoviesScreenState,
    listener: TrendingMoviesInteractionListener,
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Theme.color.surface),
        topBar = {
            TopAppBar(
                screenTitle = stringResource(R.string.movies),
                onGoBackClick = listener::onBackClick,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {}
        },
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface)
        ) {

            LazyRow(
                contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
                modifier = Modifier.padding(bottom = 11.dp)
            ) {

                items(uiState.categories) { category ->
                    Chip(
                        title = category.name,
                        isSelected = category.isSelected,
                        onClick = { listener.onCategoryClick(category.id) }
                    )
                }

            }

            LazyPagingVerticalGrid<TrendingMoviesScreenState.TrendingMovieUiState>(
                items = movieItems,
                columns = GridCells.Adaptive(minSize = 158.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) { movie ->


                HomeCard(
                    url = movie.posterPictureURL,
                    isSaved = movie.isSaved,
                    contentDescription = stringResource(R.string.movie_card),
                    onSavedClick = { listener.onToggleSaveMovie(movie.id) },
                    onClick = { listener.onMovieClick(movie.id) },
                )

            }
        }
    }
}



