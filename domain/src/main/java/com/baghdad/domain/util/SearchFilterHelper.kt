package com.baghdad.domain.util

import com.baghdad.entity.media.Genre

class SearchFilterHelper {

    fun matchesGenreFilter(itemGenres: List<Genre>, selectedGenres: List<Genre>): Boolean {
        return selectedGenres.isEmpty() || itemGenres.any { selectedGenres.contains(it) }
    }

    fun matchesYearFilter(releaseYear: Int, minimumYear: Int, maximumYear: Int): Boolean {
        return releaseYear in minimumYear..maximumYear
    }

    fun matchesRatingFilter(rating: Double, minimumRating: Int): Boolean {
        return rating >= minimumRating
    }
}
