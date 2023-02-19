package com.nakolanie.powercycling.services

import com.nakolanie.powercycling.utils.MathUtils.Companion.roundToDecimalAsString

class EnergyConsumptionService(private var energyConsumption: String) {
    private val unit: String = "kW"

    private fun writeRaw(rawValue: String){
        energyConsumption = rawValue
    }

    fun write(energyConsumption: Float){
        val convertedConsumption = energyConsumption.roundToDecimalAsString(3)
        writeRaw("$convertedConsumption $unit")
    }
}