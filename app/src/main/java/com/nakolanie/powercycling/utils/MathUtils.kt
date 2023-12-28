package com.nakolanie.powercycling.utils

import kotlin.math.roundToInt

abstract class MathUtils {
    companion object {
        fun Float.roundToDecimal(n: Int): Float {
            val exponent = Math.pow(10.toDouble(), n.toDouble());
            return (this * exponent).roundToInt() / exponent.toFloat()
        }

        fun Float.roundToDecimalAsString(n: Int): String {
            return String.format("%.${n}f", this).replace(".", ",")
        }
    }
}