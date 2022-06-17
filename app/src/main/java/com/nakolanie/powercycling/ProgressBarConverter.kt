package com.nakolanie.powercycling

abstract class ProgressBarConverter {
    companion object {
        fun convertConsumption(power: Float): Int {
            return (power * Config.TAP_POWER.toFloat() / 10f).toInt()
        }
    }
}