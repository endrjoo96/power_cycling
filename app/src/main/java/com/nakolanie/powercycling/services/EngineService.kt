package com.nakolanie.powercycling.services

import com.nakolanie.powercycling.enums.EngineName
import com.nakolanie.powercycling.models.Engine
import com.nakolanie.powercycling.models.TickEngine

class EngineService {

    private val tickEngines: MutableMap<EngineName, TickEngine> = mutableMapOf()
    private val engines: MutableMap<EngineName, Engine> = mutableMapOf()

    fun addEngine(engine: Engine) {
        if (engine is TickEngine) {
            tickEngines.putAll(mapOf(engine.mapToPair()))
        } else {
            engines.putAll(mapOf(engine.mapToPair()))
        }
    }

    fun getTickEngines(): List<TickEngine> {
        return tickEngines.toList().map { pair: Pair<EngineName, TickEngine> -> pair.second }
    }


    fun getEngines(): List<Engine> {
        return engines.toList().map { pair: Pair<EngineName, Engine> -> pair.second }
    }

    inline fun <reified T> get(engineName: EngineName):T {
        return when (T::class) {
            TickEngine::class -> getFromTickEngine(engineName) as T
            Engine::class -> getFromEngine(engineName) as T
            else -> throw UnsupportedOperationException("Class ${T::class} is not supported")
        }
    }

    fun getFromTickEngine(engineName: EngineName): TickEngine {
        return tickEngines[engineName]
            ?: throw NoSuchFieldException("Could not find such field in map [tickEngines]: ${engineName.name}")
    }

    fun getFromEngine(engineName: EngineName): Engine {
        return engines[engineName]
            ?: throw NoSuchFieldException("Could not find such field in map [engines]: ${engineName.name}")
    }
}