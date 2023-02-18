package com.nakolanie.powercycling.models.device

class VacuumCleanerDevice() : Device("Odkurzacz", 1.7f, enabledByMaxTicks = 4) {
    override val baseValue: Float = 80f

    fun getValue(): Float {
        //TODO generowanie wartosci na podstawie klasy energetycznej i jakosci
        return baseValue
    }

}