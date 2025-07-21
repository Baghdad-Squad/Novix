package com.baghdad.viewmodel.topRating

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TopRatingMovieState(
    val movies: Flow<PagingData<MovieUiState>> = flowOf(),
    val genres: List<GenreUiState> = emptyList(),
    val allTopRatedMovies: Flow<PagingData<MovieUiState>> = flowOf(),
    val moviesByGenreFilter: MovieFilterUiState = MovieFilterUiState(),
    override val isLoading: Boolean = false,
) : BaseUiState{

    data class MovieFilterUiState(
        val moviesFilter: MovieFilter = MovieFilter(),
    )

    data class MovieFilter(
        val minimumYear: Int? = null,
        val maximumYear: Int? = null,
        val minimumRating: Int = 0,
        val selectedGenres: GenreUiState = GenreUiState() ,
        val allGenres: List<GenreUiState> = emptyList(),
    )

    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )

    data class GenreUiState(
        val id: Long = 0,
        val name: String = ""
    )
}
