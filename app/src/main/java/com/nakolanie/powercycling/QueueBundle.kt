package com.nakolanie.powercycling

class QueueBundle(
    val bundleSize: Int,
    val minimalDevicesRequirements: List<Device>,
    private var patience: Int = 1000
) {
    fun lowerWaitingPatience() {
        patience--
    }

    fun bundleLostPatience(): Boolean {
        return patience <= 0
    }
}