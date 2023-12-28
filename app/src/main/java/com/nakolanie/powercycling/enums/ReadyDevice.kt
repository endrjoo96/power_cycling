package com.nakolanie.powercycling.enums

import com.nakolanie.powercycling.models.Device

enum class ReadyDevice(
    private val deviceName: String,
    private val electricityDemand: Float,
    private val enabledByTicks: Int,
    private val value: Float,
) {
    MICROWAVE(      "Mikrofalówka",         0.9f,  3, 55f),
    KETTLE(         "Czajnik elektryczny",  2.2f,  2, 20f),
    VACUUM_CLEANER( "Odkurzacz",            1.7f,  4, 80f),
    TV(             "LED TV",               0.2f,  0, 250f),
    DESKTOP_PC(     "Desktop computer",     0.5f,  0, 500f),
    LED_LIGHT(      "Żarówka LED",          0.01f, 0, 5f);

    fun create(): Device {
        return Device(
            deviceName,
            electricityDemand,
            enabledByMaxTicks = enabledByTicks,
            baseValue = value
        );
    }
}