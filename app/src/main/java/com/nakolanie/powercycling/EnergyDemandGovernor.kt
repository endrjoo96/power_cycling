package com.nakolanie.powercycling

import com.nakolanie.powercycling.Helpers.Companion.roundToDecimal
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

class EnergyDemandGovernor(val rooms: MutableList<Room>) {
    private var totalMinDemand = 3f
    private var totalMaxDemand = 6f
    private var minDemandStep = 0.1f
    private var maxDemandStep = 1f
    private var probabilityOfChangingDemand = 0.7f

    private var currentDemand = 4f

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

    private fun calculateDemand(): Float {
        return currentDemand + positiveOrNegative(rng(minDemandStep, maxDemandStep))
    }

    private fun positiveOrNegative(number: Float): Float {
        val factor = arrayOf(-1, 1)[Random.nextInt(0, 2)]
        println("Generated factor: $factor")
        return factor * number
    }

    private fun rng(minRange: Float, maxRange: Float): Float {
        return minRange + (Random.nextFloat() * (maxRange - minRange));
    }
}