package com.nakolanie.powercycling.delegates

import com.nakolanie.powercycling.enums.DelegateDefinition
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

class DelegateService<T>(
    private val delegateDefinition: DelegateDefinition,
    private val consumersMap: MutableMap<String, (T) -> Unit>,
    private val delegateObservable: ReadWriteProperty<Any?, T>
) {
    companion object {
        val DEFAULT_CONSUMER = "DEFAULT"
    }

    constructor(
        delegateDefinition: DelegateDefinition,
        initialState: T,
        consumersMap: MutableMap<String, (T) -> Unit>
    ) : this(delegateDefinition, consumersMap,
        Delegates.observable(initialState) { _, _, updatedValue ->
            consumersMap.forEach { mapPosition ->
                mapPosition.value(updatedValue)
            }
        })

    constructor(
        delegateDefinition: DelegateDefinition,
        initialState: T,
        consumer: (T) -> Unit
    ) : this(delegateDefinition, initialState, mutableMapOf(Pair(DEFAULT_CONSUMER, consumer)))

    fun getObservable(): ReadWriteProperty<Any?, T> {
        return delegateObservable
    }

    fun registerNewConsumer(key: String, consumer: (T) -> Unit) {
        consumersMap[key] = consumer
    }

    fun unregisterConsumerBy(key: String) {
        consumersMap.remove(key)
    }
}