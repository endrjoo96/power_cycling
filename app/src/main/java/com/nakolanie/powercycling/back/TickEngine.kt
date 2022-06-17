package com.nakolanie.powercycling.back

import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class TickEngine(
    engineName: String = "unnamedEngine",
    coroutinesExecutionContext: CoroutineContext = Dispatchers.Main,
    var tickInterval: Long = 2000
) : Engine(engineName, coroutinesExecutionContext) {

    private lateinit var timer: Timer
    private lateinit var task: TimerTask

    private var timeRemaining: Long = tickInterval
    var started = false
        private set
    var paused = false
        private set
//    private var lastExecutedTaskTimestamp: Long = 0

    init {
        timeRemaining = tickInterval
    }

    fun start(timeToFirstExecution: Long = tickInterval) {
        if (started) return
//        lastExecutedTaskTimestamp = System.currentTimeMillis()
        timer = Timer()
        task = object : TimerTask() {
            override fun run() {
                executeMethod()
            }
        }
        timer.scheduleAtFixedRate(task, timeToFirstExecution, tickInterval)
        started = true
        println("TickEngine $engineName started")
    }

    fun stop(showMessage: Boolean = true) {
        if (!started) return
        timer.cancel()
        timer.purge()
        started = false
        if (showMessage) println("TickEngine $engineName stopped")
    }

    fun pause() {
        if (started && !paused) {
            timeRemaining = getTimeToNextExecution()
            stop(false)
            paused = true
            println("TickEngine $engineName paused")
            println("timeRemaining: ${timeRemaining}")
        }
    }

    fun resume() {
        if (!started && paused) {
            start(timeRemaining)
            paused = false
            println("TickEngine $engineName resumed")
        }

    }

    fun getTimeToNextExecution(): Long = System.currentTimeMillis() - task.scheduledExecutionTime()
}