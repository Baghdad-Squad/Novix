package com.baghdad.viewmodel.util

import kotlin.math.roundToInt

fun Double.roundToFirstDecimal(): Double {
    return (this * 10).roundToInt() / 10.0
}