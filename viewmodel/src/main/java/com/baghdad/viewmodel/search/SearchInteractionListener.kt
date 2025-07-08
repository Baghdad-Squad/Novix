package com.baghdad.viewmodel.search

interface SearchInteractionListener {
    fun onFilterClick()
    fun onApplyClick()
    fun onCloseClick()
    fun onClearClick()
    fun onClearAllClick()
    fun onMoviesClick()
    fun onTvShowsClick()
    fun onActorsClick()
    fun onRemoveRecentSearchClick()
    fun onRatingChange(newRating: Int)
    fun onYearSelected(minYear: Int?, maxYear: Int?)
    fun onGenresSelected(selectedGenres: List<Genres>)
}
