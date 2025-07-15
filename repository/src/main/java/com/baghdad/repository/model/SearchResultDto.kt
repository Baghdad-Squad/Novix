package com.baghdad.repository.model

import com.baghdad.repository.model.actor.ActorDto

data class SearchResultDto(
    val actors : List<ActorDto>,
    val movies : List<MovieDto>,
    val tvShows : List<TvShowDto>
)