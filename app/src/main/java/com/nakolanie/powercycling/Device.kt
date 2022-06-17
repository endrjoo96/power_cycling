package com.nakolanie.powercycling

/**
 * efficiencyClass: class of device efficiency
 * electricityDemand: electricity demand in kW
 */
class Device(
    val name: String,
    efficiencyClass: EfficiencyClass,
    val electricityDemand: Float,
    val isOwnedByPlayer: Boolean = true
) {

    var efficiencyClass = efficiencyClass
    private set

    var enabled: Boolean = false

    fun requiredPower(): Float {
        if (!enabled) return 0f
        return efficiencyClass.value * electricityDemand
    }

    fun switch() {
        enabled = !enabled
    }

    fun upgradeEfficiencyClass() {
        if (efficiencyClass != EfficiencyClass.Appp) {
            efficiencyClass = EfficiencyClass.values()[
                    EfficiencyClass.values().indexOf(efficiencyClass) + 1
            ]
        }
    }
}

enum class EfficiencyClass(val value: Int) {
    F(9),
    E(8),
    D(7),
    C(6),
    B(5),
    A(4),
    Ap(3),
    App(2),
    Appp(1),
}