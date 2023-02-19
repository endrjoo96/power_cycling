package com.nakolanie.powercycling.delegates

import com.nakolanie.powercycling.enums.DelegateDefinition

abstract class DelegateProducer<T> {
    abstract val initialObservableState: T
    abstract val delegateDefinition: DelegateDefinition

    fun produce(consumer: (T) -> Unit): Pair<DelegateDefinition, DelegateService<T>> {
        return Pair(
            delegateDefinition, produceDelegate(consumer)
        )
    }

    private fun produceDelegate(consumer: (T) -> Unit): DelegateService<T> =
        DelegateService(delegateDefinition, initialObservableState, consumer)

}