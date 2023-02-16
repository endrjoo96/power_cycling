package com.nakolanie.powercycling

import com.nakolanie.powercycling.Helpers.Companion.roundToDecimal
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

class EnergyDemandGovernor(val rooms: MutableList<Room>) {
    private var currentDemand = 0f

    fun getNextDemand(): Float {
        calculateNextDemand()
        println("Calculated new demand: $currentDemand")
        return currentDemand
    }

    fun getCurrentDemand(): Float {
        return currentDemand
    }

    private fun calculateNextDemand() {
            var sumOfDemands = 0f
            rooms.forEach {
                println("Calculating room: ${it.name}")
                sumOfDemands += it.getCurrentEnergyDemand()
            }
            currentDemand = sumOfDemands.roundToDecimal(3)
    }
}