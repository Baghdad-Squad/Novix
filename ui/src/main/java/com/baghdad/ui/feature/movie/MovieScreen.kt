package com.baghdad.ui.feature.movie

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
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.HomeCard
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.viewmodel.movie.MovieScreenInteractionListener
import com.baghdad.viewmodel.movie.MovieScreenState
import com.baghdad.viewmodel.movie.MovieViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun MoviesScreen(
    viewModel: MovieViewModel = koinViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyState = rememberLazyGridState()

    MovieContent(
        uiState = uiState,
        listener = viewModel,
        lazyState = lazyState
    )

}

@Composable
fun MovieContent(
    uiState: MovieScreenState,
    listener: MovieScreenInteractionListener,
    lazyState: LazyGridState
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
                onGoBackClick = { listener.onBackClick() },
                modifier = Modifier.padding(vertical = 12.dp)
            ) {}
        },
        snackbar = {}
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

            LazyVerticalGrid(
                state = lazyState,
                columns = GridCells.Adaptive(minSize = 158.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(uiState.movies) { movie ->

                    HomeCard(
                        url = movie.posterPictureURL,
                        isSaved = movie.isSaved,
                        contentDescription = stringResource(R.string.movie_card),
                        onSavedClick = { listener.onToggleSaveMovie(movie.id) },
                        onClick = { listener.onMovieClick(movie.id) },
                        modifier = Modifier
                    )

                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MovieContentPreview() {
    val fakeState = MovieScreenState(
        categories = listOf(
            MovieScreenState.CategoryUiState(1, "Action", isSelected = true),
            MovieScreenState.CategoryUiState(2, "Comedy", isSelected = false),
            MovieScreenState.CategoryUiState(3, "Drama", isSelected = false),
            MovieScreenState.CategoryUiState(4, "Horror", isSelected = false),
            MovieScreenState.CategoryUiState(5, "Thriller ", isSelected = false),
            MovieScreenState.CategoryUiState(6, "Romance", isSelected = false),
            MovieScreenState.CategoryUiState(7, "Sience Fiction", isSelected = false),
            MovieScreenState.CategoryUiState(8, "Western", isSelected = false),
            MovieScreenState.CategoryUiState(9, "Fantasy", isSelected = false),
            MovieScreenState.CategoryUiState(10, "Music", isSelected = false),
            MovieScreenState.CategoryUiState(11, "War", isSelected = false),
            MovieScreenState.CategoryUiState(12, "Animation", isSelected = false),
            MovieScreenState.CategoryUiState(13, "Anime", isSelected = false),
        ),
        movies = List(40) {
            MovieScreenState.MovieUiState(
                id = it.toLong(),
                posterPictureURL = "https://media.themoviedb.org/t/p/w600_and_h900_bestv2/tKhneTl9BL6W1jIgTV43o5duFPx.jpg",
                isSaved = it == 12 || it == 0 || it == 8 ||
                it == 15 || it == 3 || it == 32
            )
        },
        isLoading = false
    )

    val dummyListener = object : MovieScreenInteractionListener {
        override fun onBackClick() {}
        override fun onMovieClick(movieId: Long) {}
        override fun onToggleSaveMovie(movieId: Long) {}
        override fun onCategoryClick(categoryId: Long) {}
    }

    NovixTheme(
        isDarkTheme = true
    ) {
        MovieContent(
            uiState = fakeState,
            listener = dummyListener,
            lazyState = rememberLazyGridState()
        )
    }
}
