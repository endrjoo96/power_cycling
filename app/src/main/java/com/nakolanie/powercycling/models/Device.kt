package com.nakolanie.powercycling.models

import com.nakolanie.powercycling.enums.EfficiencyClass
import com.nakolanie.powercycling.enums.Quality
import com.nakolanie.powercycling.utils.MathUtils.Companion.roundToDecimal

/**
 * efficiencyClass: class of device efficiency
 * electricityDemand: electricity demand in kW
 */
class Device(
    private var name: String,
    private val electricityDemand: Float,
    var efficiencyClass: EfficiencyClass = EfficiencyClass.F,
    val enabledByMaxTicks: Int = 0,
    val isOwnedByPlayer: Boolean = true,    //urzadzenia moga byc wlasnoscia gosci, obecnie niewykorzystywane
    var quality: Quality = Quality.GARBAGE,
    private val baseValue: Float //najnizsza mozliwa cena urzadzenia
) {
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
    fun getPowerConsumption(): Float = (efficiencyClass.efficiencyMultiplicator * quality.efficiencyMultiplicator * electricityDemand).roundToDecimal(3)


    fun upgradeEfficiencyClass() {
        if (efficiencyClass != EfficiencyClass.Appp) {
            efficiencyClass = EfficiencyClass.values()[
                efficiencyClass.ordinal + 1
            ]
        }
    }

    fun upgradeQuality() {
        if (quality != Quality.NEW_TOP) {
            quality = Quality.values()[quality.ordinal + 1]
            efficiencyClass = if (efficiencyClass.ordinal > 3) {
                EfficiencyClass.values()[
                    efficiencyClass.ordinal - 3
                ]
            } else {
                EfficiencyClass.F
            }
        }
    }

    fun getName(): String {
        return name
    }

    protected fun changeName(newName: String) {
        name = newName
    }

    fun calculatePrice(): Float {
        return baseValue + ((baseValue*quality.priceMultiplicator) * (1+(0.25f*efficiencyClass.ordinal)))
    }


    override fun toString(): String {
        return String.format(
            "[%s] %s\n(%s)",
            efficiencyClass.friendlyName,
            getName(),
            quality.friendlyDescription
        )
    }
}