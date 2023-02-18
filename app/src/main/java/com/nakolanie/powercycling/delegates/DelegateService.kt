package com.nakolanie.powercycling.delegates

import com.nakolanie.powercycling.enums.DelegateDefinition
import kotlin.properties.ReadWriteProperty

abstract class DelegateService(
    val delegateDefinition: DelegateDefinition,
    val delegateObservable: ReadWriteProperty<Any?, *>
) {

}