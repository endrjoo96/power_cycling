package com.nakolanie.powercycling.delegates

import com.nakolanie.powercycling.enums.DelegateDefinition

class WalletStateDelegateProducer : DelegateProducer<Float>() {
    override val initialObservableState = 100f
    override val delegateDefinition = DelegateDefinition.WALLET_STATE
}