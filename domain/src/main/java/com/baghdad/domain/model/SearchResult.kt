package com.baghdad.domain.model

import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor

data class SearchResult (
    val actors: List<Actor>,
    val movies: List<Movie>,
    val tvShows: List<TvShow>
)