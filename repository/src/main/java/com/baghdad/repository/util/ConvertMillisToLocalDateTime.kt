package com.baghdad.repository.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun convertMillisToLocalDateTime(milliseconds: Long): LocalDateTime {
    return Instant.fromEpochMilliseconds(milliseconds)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}