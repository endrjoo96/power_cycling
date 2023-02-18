package com.nakolanie.powercycling.delegates

import com.nakolanie.powercycling.enums.DelegateDefinition
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

class DelegateService<T>(
    private val delegateDefinition: DelegateDefinition,
    private val delegateObservable: ReadWriteProperty<Any?, T>
) {

    constructor(
        delegateDefinition: DelegateDefinition,
        initialState: T,
        consumer: (T) -> Unit
    ) : this(delegateDefinition,
        Delegates.observable(initialState) { _, _, newValue ->
            consumer(
                newValue
            )
        })

    fun getObservable(): ReadWriteProperty<Any?, T> {
        return delegateObservable
    }
}