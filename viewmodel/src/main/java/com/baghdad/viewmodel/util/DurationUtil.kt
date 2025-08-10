package com.baghdad.viewmodel.util

fun Int.formatDuration(): String {
    val hours = this / 60
    val minutes = this % 60
    return when {
        this <= 0 -> ""
        else -> if (hours > 0) {
            "${hours}h ${minutes}m"
        } else {
            "${minutes}m"
        }
    }
}