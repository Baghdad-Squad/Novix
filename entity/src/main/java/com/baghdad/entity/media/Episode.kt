package com.baghdad.entity.media

import kotlinx.datetime.LocalDate

data class Episode(
    val id: Long,
    val title: String,
    val episodeNumber: Int,
    val rating: Double,
    val duration: String,
    val releasedDate: LocalDate
)