package com.nakolanie.powercycling.models

import com.nakolanie.powercycling.enums.CyclingCharacterState

class CyclingCharacter {
    private var currentState: CyclingCharacterState = CyclingCharacterState.CYCLE_FAST_1

    fun cycle(): CyclingCharacterState {
        currentState = when(currentState){
            CyclingCharacterState.CYCLE_FAST_1 -> CyclingCharacterState.CYCLE_FAST_2
            CyclingCharacterState.CYCLE_FAST_2 -> CyclingCharacterState.CYCLE_FAST_3
            CyclingCharacterState.CYCLE_FAST_3 -> CyclingCharacterState.CYCLE_FAST_4
            CyclingCharacterState.CYCLE_FAST_4 -> CyclingCharacterState.CYCLE_FAST_1
            else -> CyclingCharacterState.CYCLE_FAST_1
        }

        return currentState
    }
}