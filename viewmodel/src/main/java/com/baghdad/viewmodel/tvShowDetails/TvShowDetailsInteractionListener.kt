package com.baghdad.viewmodel.tvShowDetails

interface TvShowDetailsInteractionListener {
    fun onClickBackIcon()
    fun onClickReadMoreOverview()
    fun onClickGenre(genreId: Long)
    fun onClickCastMember(actorId: Long)
    fun onClickEpisode(episodeId: Long)
    fun onClickReviews(tvShowId: Long)
    fun onClickSaveTvShow(tvShowId: Long)
    fun onClickAddRating()
    fun onClickSeasonTab()
    fun onClickPlayTrailer() /*TODO*/
}