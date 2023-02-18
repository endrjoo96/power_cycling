package com.nakolanie.powercycling.enums

import kotlin.reflect.KClass

enum class DelegateDefinition(val assignedClass: KClass<*>) {
    PROGRESS_UPDATE(Int::class),
    ENERGY_CONSUMPTION(String::class),
    CYCLING_CHARACTER_STATE(CyclingCharacterState::class),
    WALLET_STATE(Float::class),
    QUEUE_STATE(Int::class);
}