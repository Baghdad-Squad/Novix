package com.baghdad.ui.feature.categoryMovies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.util.hideNavigationBar
import com.baghdad.ui.navigation.graph.categories.CategoriesNavEvent
import com.baghdad.viewmodel.categoryMovies.CategoryMoviesEffect
import com.baghdad.viewmodel.categoryMovies.CategoryMoviesState
import com.baghdad.viewmodel.categoryMovies.CategoryMoviesViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CategoryMoviesScreen(
    categoryId: Long,
    viewModel: CategoryMoviesViewModel = koinViewModel(
        key = categoryId.toString(),
        parameters = { parametersOf(categoryId) }
    ),
    handleNavigation: (CategoriesNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }


    CategoryMoviesContent(
        uiState = uiState,
        listener = viewModel
    )
}

private fun handleEffect(
    effect: CategoryMoviesEffect,
    handleNavigation: (CategoriesNavEvent) -> Unit
) {
    when (effect) {
        is CategoryMoviesEffect.NavigateBack -> handleNavigation(
            CategoriesNavEvent.NavigateBack
        )

        is CategoryMoviesEffect.NavigateToMovieDetails -> handleNavigation(
            CategoriesNavEvent.NavigateToMovieDetails(effect.movieId)
        )
    }
}

@Composable
private fun CategoryMoviesContent(
    uiState: CategoryMoviesState,
    listener: CategoryMoviesViewModel,
) {
    val context = LocalContext.current
    val view = LocalView.current
    LaunchedEffect(Unit) {
        hideNavigationBar(context, view)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.color.surface)
                    .statusBarsPadding()
                    .padding(top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    icon = painterResource(R.drawable.ic_go_back),
                    onClick = { listener.onBackClicked() },
                    modifier = Modifier
                        .padding(start = 16.dp, end = 12.dp)
                )
                Text(
                    text = uiState.categoryName,
                    style = Theme.typography.title.large,
                    color = Theme.color.title,
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                )
            }
        },
    ) {
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
            items(
                uiState.movies,
                key = { it.id }
            ) { movie ->
                HomeCard(
                    url = movie.posterPictureURL,
                    contentDescription = null,
                    isSaved = movie.isSaved,
                    onSavedClick = { listener.onSavedClick(movie.id) },
                    onClick = { listener.onMovieClicked(movie.id) },
                    modifier = Modifier.aspectRatio(0.8f)
                )
            }
        }
    }
}
