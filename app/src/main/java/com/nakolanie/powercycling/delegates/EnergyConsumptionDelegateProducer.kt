package com.nakolanie.powercycling.delegates

import com.nakolanie.powercycling.enums.DelegateDefinition

class EnergyConsumptionDelegateProducer : DelegateProducer<String>() {
    override val initialObservableState = ""
    override val delegateDefinition = DelegateDefinition.ENERGY_CONSUMPTION
}