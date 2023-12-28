package com.nakolanie.powercycling.models

class QueueBundle(
    val bundleSize: Int,
    val minimalDevicesRequirements: List<Device>,
    private var patience: Int = 1000
) {
    fun getPatience(): Int {
        return patience
    }

    fun lowerWaitingPatience() {
        patience -= 5
    }

    fun bundleLostPatience(): Boolean {
        return patience <= 0
    }
}