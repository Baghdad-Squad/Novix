package com.baghdad.viewmodel.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import java.util.Locale

fun LocalDate.toYYYYMMDDFormat(): String {
    val format = LocalDate.Format {
        year(padding = Padding.ZERO)
        char('-')
        monthNumber(padding = Padding.ZERO)
        char('-')
        day(padding = Padding.ZERO)
    }
    return this.format(format)
}

fun LocalDate.toDDMMYYYYFormat(): String {
    val format = LocalDate.Format {
        day(padding = Padding.ZERO)
        char('-')
        monthNumber(padding = Padding.ZERO)
        char('-')
        year(padding = Padding.ZERO)
    }
    return this.format(format)
}

fun LocalDate.toDDMMMYYYYFormat(): String {
    val currentLocale = Locale.getDefault()
    val isArabic = currentLocale.language == "ar"

    val monthNames = if (isArabic) {
        MonthNames(
            "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
            "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
        )
    } else {
        MonthNames.ENGLISH_ABBREVIATED
    }

    val format = LocalDate.Format {
        day(padding = Padding.NONE)
        char(' ')
        monthName(names = monthNames)
        char(' ')
        year(padding = Padding.ZERO)
    }

    val formattedDate = this.format(format)

    return if (isArabic) {
        formattedDate
    } else {
        formattedDate
    }
}