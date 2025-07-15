package com.baghdad.viewmodel.actorDetails

interface ActorDetailsInteractionListener {
    fun onBackIconClick()
    fun onReadMoreBiographyClick()
    fun onViewAllGalleryClick()
    fun onViewAllTopMoviesPicksClick()
    fun onViewAllTopTvShowsClick()
    fun onMovieCardClick(movieId: Long)
    fun onTvShowCardClick(tvShowId: Long)
    fun onSaveMovieClick(movieId: Long)
    fun onSaveTvShowClick(tvShowId: Long)
}