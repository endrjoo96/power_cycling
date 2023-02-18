package com.nakolanie.powercycling.back

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class Engine(
    protected val engineName: ENGINE_NAME,
    private val coroutinesExecutionContext: CoroutineContext = Dispatchers.Main
) {
    protected lateinit var engineType: String
    protected var executionMethod: () -> Unit =
        { println("$engineType ${engineName.value}: Not set tick method.") }

    init {
        engineType = this::class.simpleName.toString()
        println("$engineType ${engineName.value} created")
    }

    fun setMethod(method: () -> Unit) {
        executionMethod = method
    }

    fun executeMethod() = CoroutineScope(coroutinesExecutionContext).launch {
        executionMethod()
    }

    open fun mapToPair(): Pair<ENGINE_NAME, Engine> {
        return Pair(engineName, this)
    }
}