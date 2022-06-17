package com.nakolanie.powercycling

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Cycler (private val context: AppCompatActivity, private val cyclerImageViewReference: ImageView) {
    var currentState: CyclerState = CyclerState.CYCLE_FAST_1
    private set

    fun cycle(){
        currentState = when(currentState){
            CyclerState.CYCLE_FAST_1 -> CyclerState.CYCLE_FAST_2
            CyclerState.CYCLE_FAST_2 -> CyclerState.CYCLE_FAST_3
            CyclerState.CYCLE_FAST_3 -> CyclerState.CYCLE_FAST_4
            CyclerState.CYCLE_FAST_4 -> CyclerState.CYCLE_FAST_1
            else -> CyclerState.CYCLE_FAST_1
        }
        Helpers.setImageResourceFromBitmap(context, cyclerImageViewReference, currentState.mipmapPath)

    }
}