package com.nakolanie.powercycling.delegates

import com.nakolanie.powercycling.enums.DelegateDefinition
import kotlin.properties.ReadWriteProperty

interface DelegateProducer<T> {
    fun produce(definition: DelegateDefinition, observable: ReadWriteProperty<Any?, T>): DelegateService
}