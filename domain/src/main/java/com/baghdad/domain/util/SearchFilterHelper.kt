package com.baghdad.domain.util

import com.baghdad.entity.media.Genre
import javax.inject.Inject

class SearchFilterHelper @Inject constructor(){

    fun matchesGenreFilter(itemGenres: List<Genre>, selectedGenres: List<Genre>): Boolean {
        val itemGenreIds = itemGenres.map { it.id }
        val selectedIds = selectedGenres.map { it.id }
        return selectedGenres.isEmpty() || itemGenreIds.any { it in selectedIds }
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
