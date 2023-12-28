package com.nakolanie.powercycling.enums

import com.nakolanie.powercycling.models.Device

enum class ReadyDevice(
    private val deviceName: DeviceName,
    private val electricityDemand: Float,
    private val enabledByTicks: Int,
    private val value: Float,
) {
    MICROWAVE      (DeviceName.MICROWAVE,      0.9f,  3, 55f  ),
    KETTLE         (DeviceName.KETTLE,         2.2f,  2, 20f  ),
    VACUUM_CLEANER (DeviceName.VACUUM_CLEANER, 1.7f,  4, 80f  ),
    TV             (DeviceName.TV,             0.2f,  0, 250f ),
    DESKTOP_PC     (DeviceName.DESKTOP_PC,     0.5f,  0, 500f ),
    LED_LIGHT      (DeviceName.LIGHT_BULB,     0.01f, 0, 5f   );

    fun create(): Device {
        return Device(
            deviceName.localizedName,
            electricityDemand,
            enabledByMaxTicks = enabledByTicks,
            baseValue = value
        )
    }
}