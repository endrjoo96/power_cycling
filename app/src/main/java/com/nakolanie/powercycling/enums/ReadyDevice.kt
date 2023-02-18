package com.nakolanie.powercycling.enums

import com.nakolanie.powercycling.Device

enum class ReadyDevice(val get: Device, val value: Float) {
    MICROWAVE(
        Device("Mikrofalówka",0.9f,
        enabledByMaxTicks = 3), 55f),
    KETTLE(
        Device(
        "Czajnik elektryczny",
        2.2f,
        enabledByMaxTicks = 2
    ), 20f),
    VACUUM_CLEANER(
        Device(
        "Odkurzacz",
        1.7f,
        enabledByMaxTicks = 4
    ), 80f),
    TV(Device("LED TV",  0.2f), 250f),
    DESKTOP_PC(Device("Desktop computer", 0.5f), 500f),
    LED_LIGHT(Device("Żarówka LED",  0.01f), 5f),

}