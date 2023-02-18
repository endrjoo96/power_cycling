package com.nakolanie.powercycling.delegates

import com.nakolanie.powercycling.enums.DelegateDefinition
import kotlin.properties.ReadWriteProperty

object ProgressUpdateDelegateProducer : DelegateProducer<Int>{
    override fun produce(
        definition: DelegateDefinition,
        observable: ReadWriteProperty<Any?, Int>
    ): DelegateService {
        TODO("Not yet implemented")
    }
}