package com.nakolanie.powercycling.services

import com.nakolanie.powercycling.enums.CyclingCharacterState
import com.nakolanie.powercycling.models.CyclingCharacter

class CyclingService(private val character: CyclingCharacter) {

    fun cycle(): CyclingCharacterState {
        val newState = when(character.currentState){
            CyclingCharacterState.CYCLE_FAST_1 -> CyclingCharacterState.CYCLE_FAST_2
            CyclingCharacterState.CYCLE_FAST_2 -> CyclingCharacterState.CYCLE_FAST_3
            CyclingCharacterState.CYCLE_FAST_3 -> CyclingCharacterState.CYCLE_FAST_4
            CyclingCharacterState.CYCLE_FAST_4 -> CyclingCharacterState.CYCLE_FAST_1
            else -> CyclingCharacterState.CYCLE_FAST_1
        }
        character.currentState = newState
        return newState
    }
}