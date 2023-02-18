package com.nakolanie.powercycling.models.device

class KettleDevice() : Device("Czajnik elektryczny", 2.2f, enabledByMaxTicks = 2) {
    override val baseValue: Float = 20f

    fun getValue(): Float {
        //TODO generowanie wartosci na podstawie klasy energetycznej i jakosci
        return baseValue
    }

}