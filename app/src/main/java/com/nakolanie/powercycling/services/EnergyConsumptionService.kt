package com.nakolanie.powercycling.services

import com.nakolanie.powercycling.utils.MathUtils.Companion.roundToDecimalAsString
import kotlin.properties.ReadWriteProperty

class EnergyConsumptionService(energyConsumptionDelegate: ReadWriteProperty<Any?, String>) {
    private val unit: String = "kW"
    private var energyConsumption: String by energyConsumptionDelegate

    private fun writeRaw(rawValue: String){
        energyConsumption = rawValue
    }

    fun write(energyConsumption: Float){
        val convertedConsumption = energyConsumption.roundToDecimalAsString(3)
        writeRaw("$convertedConsumption $unit")
    }
}