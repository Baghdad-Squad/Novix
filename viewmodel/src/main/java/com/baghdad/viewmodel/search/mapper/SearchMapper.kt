package com.baghdad.viewmodel.search.mapper

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Media
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.search.RecentSearch
import com.baghdad.viewmodel.search.SearchScreenState

fun Movie.toMovieUI() = SearchScreenState.MovieUiState(
    id = id,
    posterPictureURL = posterPictureURL,
    // TODO: we need to add isSaved from domain
)

fun TvShow.toTvShowUI() = SearchScreenState.TvShowUiState(
    id = id,
    posterPictureURL = posterPictureURL,
    // TODO: we need to add isSaved from domain
)

fun Actor.toActorUI() = SearchScreenState.ActorUiState(
    id = id,
    name = name,
    profilePictureURL = profilePictureURL
)
fun Media.toMediaUI() = SearchScreenState.MediaUiState(
    id = id,
    posterPictureURL = posterPictureURL,
    // TODO: we need to add isSaved from domain

)

fun RecentSearch.toRecentSearchUI() = SearchScreenState.RecentSearchUiState(
    id = id,
    query = query
)

fun Genre.toGenreUI() = SearchScreenState.GenreUiState(
    id = id,
    name = name
)
