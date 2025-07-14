package com.baghdad.viewmodel.search

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.search.RecentSearch

fun Movie.toMovieUI() = SearchScreenState.MovieUiState(
    id = id,
    posterPictureURL = posterImageURL,
    // TODO: we need to add isSaved from domain
)

fun TvShow.toTvShowUI() = SearchScreenState.TvShowUiState(
    id = id,
    posterPictureURL = posterImageURL,
    // TODO: we need to add isSaved from domain
)

fun Actor.toActorUI() = SearchScreenState.ActorUiState(
    id = id,
    name = name,
    profilePictureURL = profilePictureURL
)
fun RecentlyViewed.toRecentlyViewedUI() = SearchScreenState.RecentlyViewedUiState(
    id = contentId,
    posterPictureURL = contentImageUrl,
    contentType = contentType
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

fun SearchScreenState.SearchFilterUiState.toSearchFilter() = SearchFilter(
    minimumYear = minimumYear,
    maximumYear = maximumYear,
    minimumRating = minimumRating,
    selectedGenres = selectedGenres.map { it.toGenre() }
)

fun SearchScreenState.GenreUiState.toGenre() = Genre(
    id = id,
    name = name
)
