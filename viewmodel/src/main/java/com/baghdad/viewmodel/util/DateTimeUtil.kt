package com.baghdad.viewmodel.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

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
    val format = LocalDate.Format {
        day(padding = Padding.NONE)
        char(' ')
        monthName(names = MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        year(padding = Padding.ZERO)
    }
    return this.format(format)
}