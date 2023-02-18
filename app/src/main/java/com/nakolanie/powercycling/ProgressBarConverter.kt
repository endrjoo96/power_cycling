package com.nakolanie.powercycling

import com.nakolanie.powercycling.configs.GameConfig

abstract class ProgressBarConverter {
    companion object {
        fun convertConsumption(power: Float): Int {
            return (power * GameConfig.TAP_POWER.toFloat() / 10f).toInt()
        }
    }
}