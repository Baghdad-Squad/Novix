package com.baghdad.viewmodel.tvShowDetails

interface TvShowDetailsInteractionListener {
    fun onClickBackIcon()
    fun onClickReadMoreOverview()
    fun onClickGenre(genreId: Long)
    fun onClickCastMember(actorId: Long)
    fun onClickEpisode(seasonNumber: Int, episodeNumber: Int)
    fun onClickReviews(tvShowId: Long)
    fun onClickSaveTvShow(tvShowId: Long)
    fun onClickAddRating()
    fun onClickSeasonTab(seasonIndex: Int)

    fun onClickPlayTrailer()
}