package com.nakolanie.powercycling

import com.nakolanie.powercycling.Helpers.Companion.roundToDecimal

/**
 * efficiencyClass: class of device efficiency
 * electricityDemand: electricity demand in kW
 */
class Device(
    val name: String,
    val electricityDemand: Float,
    efficiencyClass: EfficiencyClass = EfficiencyClass.F,
    val enabledByMaxTicks: Int = 0,
    val isOwnedByPlayer: Boolean = true,
) {
    lateinit var quality: Quality

    var efficiencyClass = efficiencyClass

    var enabled: Boolean = false
        set(value) {
            enabledByTicks = 0
            field = value
        }
    private var enabledByTicks: Int = 0
    private val measureTicks: Boolean = enabledByMaxTicks > 0

    /**
     * Returns power, if device is enabled
     */
    fun getCurrentPowerConsumption(): Float {
        if (!enabled) return 0f
        val consumption = getPowerConsumption()
        if (measureTicks) {
            enabledByTicks++
            if (enabledByTicks >= enabledByMaxTicks) {
                enabledByTicks = 0
                enabled = false
            }
        }
        return consumption
    }

    /**
     * Returns power consumption. It omits device "enabled" check, so use only for information purposes
     */
    fun getPowerConsumption(): Float = (efficiencyClass.value * electricityDemand).roundToDecimal(3)


    fun upgradeEfficiencyClass() {
        if (efficiencyClass != EfficiencyClass.Appp) {
            efficiencyClass = EfficiencyClass.values()[
                    EfficiencyClass.values().indexOf(efficiencyClass) + 1
            ]
        }
    }
}

enum class READY_DEVICES(val get: Device, val value: Float) {
    MICROWAVE(Device("Mikrofalówka",0.9f,
        enabledByMaxTicks = 3), 55f),
    KETTLE(Device(
        "Czajnik elektryczny",
        2.2f,
        enabledByMaxTicks = 2
    ), 20f),
    VACUUM_CLEANER(Device(
        "Odkurzacz",
        1.7f,
        enabledByMaxTicks = 4
    ), 80f),
    TV(Device("LED TV",  0.2f), 250f),
    DESKTOP_PC(Device("Desktop computer", 0.5f), 500f),
    LED_LIGHT(Device("Żarówka LED",  0.01f), 5f),

}