package com.nakolanie.powercycling.models

import com.nakolanie.powercycling.enums.CyclingCharacterState

data class CyclingCharacter(
    var currentState: CyclingCharacterState = CyclingCharacterState.CYCLE_FAST_1
)