package com.baghdad.domain.util

import android.util.Log
import com.baghdad.entity.media.Genre

class SearchFilterHelper {

    fun matchesGenreFilter(itemGenres: List<Genre>, selectedGenres: List<Genre>): Boolean {
        val itemGenreIds = itemGenres.map { it.id }
        val selectedIds = selectedGenres.map { it.id }
        val a = selectedGenres.isEmpty() || itemGenreIds.any { it in selectedIds }
        Log.e(
            "SearchFilterHelper",
            "matchesGenreFilter: $a, itemGenres: $itemGenres, selectedGenres: $selectedGenres"
        )
        return a
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
