package com.baghdad.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.baghdad.ui.R

@Composable
fun arabicDuration(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60

    val hoursPart = convertHourToArabic(hours)
    val minutesPart = convertMinutesToArabic(remainingMinutes)


    return when {
        hours > 0 && remainingMinutes > 0 -> stringResource(
            R.string.duration_combined,
            hoursPart,
            minutesPart
        )

        hours > 0 -> hoursPart
        remainingMinutes > 0 -> minutesPart
        else -> stringResource(R.string.unknown)
    }

}

@Composable
private fun convertHourToArabic(hours: Int): String {
    return when (hours) {
        0 -> ""
        1 -> stringResource(R.string.hour)
        2 -> stringResource(R.string.two_hour)
        in 3..10 -> stringResource(R.string.hours, hours)
        else -> stringResource(R.string.hour, hours)
    }
}


@Composable
private fun convertMinutesToArabic(minutes: Int): String {
    return when (minutes) {
        0 -> stringResource(R.string.unknown)
        1 -> stringResource(R.string.min)
        2 -> stringResource(R.string.two_minute)
        in 3..10 -> stringResource(R.string.minutes, minutes)
        else -> stringResource(R.string.min, minutes)
    }
}