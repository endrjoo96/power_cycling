package com.nakolanie.powercycling

import com.nakolanie.powercycling.Helpers.Companion.roundToDecimal

/**
 * efficiencyClass: class of device efficiency
 * electricityDemand: electricity demand in kW
 */
class Device(
    val name: String,
    efficiencyClass: EfficiencyClass,
    val electricityDemand: Float,
    val enabledByMaxTicks: Int = 0,
    val isOwnedByPlayer: Boolean = true,
) {

    var efficiencyClass = efficiencyClass
        private set

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