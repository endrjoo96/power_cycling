package com.nakolanie.powercycling.services

import com.nakolanie.powercycling.models.Room
import com.nakolanie.powercycling.utils.MathUtils.Companion.roundToDecimal

class EnergyDemandGovernorService(val rooms: MutableList<Room>) {
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