package com.nakolanie.powercycling

class Cycler {
    private var currentState: CyclerState = CyclerState.CYCLE_FAST_1

    fun cycle(): CyclerState{
        currentState = when(currentState){
            CyclerState.CYCLE_FAST_1 -> CyclerState.CYCLE_FAST_2
            CyclerState.CYCLE_FAST_2 -> CyclerState.CYCLE_FAST_3
            CyclerState.CYCLE_FAST_3 -> CyclerState.CYCLE_FAST_4
            CyclerState.CYCLE_FAST_4 -> CyclerState.CYCLE_FAST_1
            else -> CyclerState.CYCLE_FAST_1
        }

        return currentState
    }
}