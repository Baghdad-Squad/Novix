package com.baghdad.domain.result

import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor

data class SearchResult(
    val movies: List<Movie>,
    val tvShows: List<TvShow>,
    val actors: List<Actor>
)
