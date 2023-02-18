package com.nakolanie.powercycling.models

import com.nakolanie.powercycling.enums.EngineName
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class TickEngine(
    engineName: EngineName = EngineName.UNDEFINED,
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

    init {
        timeRemaining = tickInterval
    }

    fun getName(): EngineName {
        return engineName
    }

    fun start(timeToFirstExecution: Long = tickInterval) {
        if (started) return
        timer = Timer()
        task = object : TimerTask() {
            override fun run() {
                executeMethod()
            }
        }
        timer.scheduleAtFixedRate(task, timeToFirstExecution, tickInterval)
        started = true
        println("$engineType $engineName started")
    }

    fun stop(showMessage: Boolean = true) {
        if (!started) return
        timer.cancel()
        timer.purge()
        started = false
        if (showMessage) println("$engineType $engineName stopped")
    }

    fun pause() {
        if (started && !paused) {
            timeRemaining = getTimeToNextExecution()
            stop(false)
            paused = true
            println("$engineType $engineName paused")
        }
    }

    fun resume() {
        if (!started && paused) {
            start(timeRemaining)
            paused = false
            println("$engineType $engineName resumed")
        }
    }

    override fun mapToPair(): Pair<EngineName, TickEngine> {
        return Pair(engineName, this)
    }

    fun getTimeToNextExecution(): Long = System.currentTimeMillis() - task.scheduledExecutionTime()
}