package com.nakolanie.powercycling.models.device

class TvDevice() : Device("TV", 0.2f) {
    override val baseValue: Float = 250f

    fun getValue(): Float {
        //TODO generowanie wartosci na podstawie klasy energetycznej i jakosci
        return baseValue
    }

}