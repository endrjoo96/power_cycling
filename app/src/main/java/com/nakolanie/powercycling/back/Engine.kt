package com.nakolanie.powercycling.back

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class Engine(
    protected val engineName: String = "unnamedEngine",
    private val coroutinesExecutionContext: CoroutineContext = Dispatchers.Main
) {
    protected var executionMethod: () -> Unit =
        { println("${this::class} $engineName: Not set tick method.") }

    init {
        println("${this::class} $engineName created")
    }

    fun setMethod(method: () -> Unit) {
        executionMethod = method
    }

    fun executeMethod() = CoroutineScope(coroutinesExecutionContext).launch {
//                    println("@@@@ ${engineName} >>> Uruchomiono po ${System.currentTimeMillis() - lastExecutedTaskTimestamp} ms")
        executionMethod()
//                    lastExecutedTaskTimestamp = System.currentTimeMillis()
    }
}