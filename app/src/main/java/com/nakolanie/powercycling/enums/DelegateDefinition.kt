package com.nakolanie.powercycling.enums

import kotlin.reflect.KClass

enum class DelegateDefinition(val assignedClass: KClass<*>) {
    PROGRESS_UPDATE(Int::class),
    ENERGY_CONSUMPTION(String::class),
    CYCLER_STATE(CyclingCharacterState::class),
    WALLET_CHANGE(Float::class),
    QUEUE_STATE(Int::class);
}