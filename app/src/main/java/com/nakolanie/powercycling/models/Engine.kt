package com.nakolanie.powercycling.models

import com.nakolanie.powercycling.enums.EngineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class Engine(
    protected val engineName: EngineName,
    private val coroutinesExecutionContext: CoroutineContext = Dispatchers.Main
) {
    protected var engineType: String = this::class.simpleName.toString()
    protected var executionMethod: () -> Unit =
        { println("$engineType ${engineName.value}: Not set tick method.") }

    init {
        println("$engineType ${engineName.value} created")
    }

    fun setMethod(method: () -> Unit) {
        executionMethod = method
    }

    fun executeMethod() = CoroutineScope(coroutinesExecutionContext).launch {
        executionMethod()
    }

    open fun mapToPair(): Pair<EngineName, Engine> {
        return Pair(engineName, this)
    }
}