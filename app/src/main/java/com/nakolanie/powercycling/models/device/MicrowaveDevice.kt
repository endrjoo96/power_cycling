package com.nakolanie.powercycling.models.device

class MicrowaveDevice() : Device("Mikrofalówka", 0.9f, enabledByMaxTicks = 3) {
    override val baseValue: Float = 55f

    fun getValue(): Float {
        //TODO generowanie wartosci na podstawie klasy energetycznej i jakosci
        return baseValue
    }

}