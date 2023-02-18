package com.nakolanie.powercycling.delegates

import com.nakolanie.powercycling.configs.GameConfig
import com.nakolanie.powercycling.enums.DelegateDefinition

class ProgressUpdateDelegateProducer : DelegateProducer<Int>() {
    override val initialObservableState = GameConfig.START_PROGRESSBAR_VALUE
    override val delegateDefinition = DelegateDefinition.PROGRESS_UPDATE
}