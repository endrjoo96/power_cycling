package com.nakolanie.powercycling.delegates

import com.nakolanie.powercycling.enums.CyclingCharacterState
import com.nakolanie.powercycling.enums.DelegateDefinition

class CyclingCharacterStateDelegateProducer : DelegateProducer<CyclingCharacterState>() {
    override val initialObservableState = CyclingCharacterState.CYCLE_FAST_1
    override val delegateDefinition = DelegateDefinition.CYCLING_CHARACTER_STATE
}