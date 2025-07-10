package com.baghdad.repository.model

data class SearchResultDto(
    val actors : List<ActorDto>,
    val movies : List<MovieDto>,
    val tvShows : List<TvShowDto>
)