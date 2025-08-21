package com.baghdad.viewmodel.search

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.search.RecentSearch

fun SavedMovie.toMovieUI() =
    SearchScreenState.MovieUiState(
        id = movie.id,
        posterPictureURL = movie.posterImageURL,
        isSaved = isSaved,
        savedListId = listId ?: -1L
    )

fun TvShow.toTvShowUI() = SearchScreenState.TvShowUiState(
    id = id,
    posterPictureURL = posterImageURL,
)

fun Actor.toActorUI() = SearchScreenState.ActorUiState(
    id = id,
    name = name,
    profilePictureURL = profilePictureURL
)

fun RecentlyViewed.toRecentlyViewedUI() = SearchScreenState.RecentlyViewedUiState(
    id = contentId,
    posterPictureURL = contentImageUrl,
    contentType = SearchScreenState.RecentlyViewedUiState.ContentType.valueOf(contentType.name),
    isSaved = isSaved,
    savedListId = listId ?: -1L

)

fun RecentSearch.toRecentSearchUI() = SearchScreenState.RecentSearchUiState(
    id = id,
    query = query
)
