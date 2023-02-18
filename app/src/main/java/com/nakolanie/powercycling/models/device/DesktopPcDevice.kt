package com.nakolanie.powercycling.models.device

class DesktopPcDevice() : Device("Komputer", 0.5f) {
    override val baseValue: Float = 350f

    fun getValue(): Float {
        //TODO generowanie wartosci na podstawie klasy energetycznej i jakosci
        return baseValue
    }

}