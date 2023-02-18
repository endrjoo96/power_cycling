package com.nakolanie.powercycling.delegates

import com.nakolanie.powercycling.enums.DelegateDefinition

class QueueStateDelegateProducer : DelegateProducer<Int>() {
    override val initialObservableState = 0
    override val delegateDefinition = DelegateDefinition.QUEUE_STATE
}