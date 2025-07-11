package com.baghdad.domain.testHelper

import com.baghdad.entity.media.Media

fun getTestMedia(
    count: Int
): List<Media> {
    val moviesCount = count / 2
    val tvShowsCount = count - moviesCount
    return getTestMovies(moviesCount) + getTestTvShows(tvShowsCount)
}