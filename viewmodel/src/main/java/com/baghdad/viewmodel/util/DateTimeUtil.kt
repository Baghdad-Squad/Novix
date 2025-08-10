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

    return if (isArabic) {
        val arabicMonths = arrayOf(
            "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
            "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
        )

        val monthName = arabicMonths[this.monthNumber - 1]
        "${this.dayOfMonth} $monthName ${this.year}"
    } else {
        val format = LocalDate.Format {
            dayOfMonth(padding = Padding.NONE)
            char(' ')
            monthName(names = MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            year(padding = Padding.ZERO)
        }
        this.format(format)
    }
}