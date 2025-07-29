package com.baghdad.domain.util

import com.baghdad.entity.media.Genre
import javax.inject.Inject

class SearchFilterHelper @Inject constructor(){

    fun matchesGenreFilter(itemGenres: List<Genre>, selectedGenres: List<Genre>): Boolean {
        return selectedGenres.isEmpty() || itemGenres.any { selectedGenres.contains(it) }
    }

    fun matchesYearFilter(releaseYear: Int, minimumYear: Int?, maximumYear: Int?): Boolean {
        val minCheck = minimumYear?.let { releaseYear >= it } != false
        val maxCheck = maximumYear?.let { releaseYear <= it } != false
        return minCheck && maxCheck
    }

    fun matchesRatingFilter(rating: Double, minimumRating: Int): Boolean {
        return rating >= minimumRating
    }
}
