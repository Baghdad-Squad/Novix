package com.baghdad.viewmodel.categories

import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.base.BaseUiState

data class CategoriesState(
    val selectedCategoriesTab: CategoriesTab = CategoriesTab.MOVIES,
    val movieGenres: List<GenreUiState> = emptyList(),
    val tvShowGenres: List<GenreUiState> = emptyList(),
    val categoryName: String = "",
    val isLoading: Boolean = false
): BaseUiState {
    val genres: List<GenreUiState>
    get() = when (selectedCategoriesTab) {
        CategoriesTab.MOVIES -> movieGenres
        CategoriesTab.TV_SHOWS -> tvShowGenres
    }

    data class GenreUiState(
        val id: Long = 0L,
        val name: String = "",
        val movieCategory: MovieCategory? = null,
        val tvShowCategory: TvShowCategory? = null
    )
    enum class CategoriesTab {
        MOVIES,
        TV_SHOWS
    }
}



fun Genre.toGenreUiState() = CategoriesState.GenreUiState(
    id = id,
    name = name,
    movieCategory = MovieCategory.entries.find { it.id == id },
    tvShowCategory = TvShowCategory.entries.find { it.id == id }
)

fun MovieCategory.toGenreUiState(): CategoriesState.GenreUiState {
    return CategoriesState.GenreUiState(
        id = id,
        name = name,
        movieCategory = this
    )
}

fun TvShowCategory.toGenreUiState(): CategoriesState.GenreUiState {
    return CategoriesState.GenreUiState(
        id = id,
        name = name,
        tvShowCategory = this
    )
}