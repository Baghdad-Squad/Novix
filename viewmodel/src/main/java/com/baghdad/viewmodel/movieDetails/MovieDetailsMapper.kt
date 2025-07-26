package com.baghdad.viewmodel.movieDetails

import kotlin.math.roundToInt

fun Double.roundToFirstDecimal(): Double {
    return (this * 10).roundToInt() / 10.0
}

fun Int.formatDuration(): String {
    val hours = this / 60
    val minutes = this % 60
    return when {
        this <= 0 -> "unknown"
        else -> if (hours > 0) {
            "$hours hr $minutes min"
        } else {
            "$minutes min"
        }
    }
}