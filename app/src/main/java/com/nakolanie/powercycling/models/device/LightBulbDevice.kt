package com.nakolanie.powercycling.models.device

class LightBulbDevice() : Device("Żarówka", 0.01f) {
    override val baseValue: Float = 5f

    fun getValue(): Float {
        //TODO generowanie wartosci na podstawie klasy energetycznej i jakosci
        return baseValue
    }

}