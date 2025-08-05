package com.baghdad.viewmodel.tvShowDetails

interface TvShowDetailsInteractionListener {
    fun onClickBackIcon()
    fun onClickReadMoreOverview()
    fun onClickGenre(genreId: Long)
    fun onClickCastMember(actorId: Long)
    fun onClickEpisode(seasonNumber: Int, episodeNumber: Int)
    fun onClickReviews()
    fun onClickSaveTvShow()
    fun onClickStarButton()
    fun onRatingChanged(rating: Int)
    fun onClickSubmitRating(rating: Int)
    fun onDismissRatingBottomSheet()
    fun onClickSeasonTab(seasonIndex: Int)
    fun onClickPlayTrailer()
    fun onSnackBarActionLabelClick()
    fun onClickLoginButton()
}