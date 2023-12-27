package com.nakolanie.powercycling.models

import com.nakolanie.powercycling.enums.CyclingCharacterState
import kotlin.properties.ReadWriteProperty

class CyclingCharacter(
    currentStateDelegate: ReadWriteProperty<Any?, CyclingCharacterState>
) {
    var currentState: CyclingCharacterState by currentStateDelegate
}