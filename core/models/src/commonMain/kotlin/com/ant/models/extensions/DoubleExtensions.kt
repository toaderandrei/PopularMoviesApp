package com.ant.models.extensions

import kotlin.math.round

/**
 * Rounds this [Double] to one decimal place and returns it as a string.
 */
fun Double.toTwoDigitNumber(): String {
    val rounded = round(this * 10) / 10.0
    return rounded.toString()
}
