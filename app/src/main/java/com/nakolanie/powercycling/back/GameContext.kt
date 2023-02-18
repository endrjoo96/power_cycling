package com.nakolanie.powercycling.back

import android.annotation.SuppressLint
import com.nakolanie.powercycling.*
import com.nakolanie.powercycling.Helpers.Companion.roundToDecimalAsString
import com.nakolanie.powercycling.front.game.DELEGATE
import kotlin.properties.ReadWriteProperty
import kotlin.random.Random

class GameContext(
    var progressBarMax: Int,
    private val finishMethod: () -> Unit,
    private val delegatesMap: Map<DELEGATE, ReadWriteProperty<Any?, *>>
) {



    private var progressBarCurrent: Int by getDelegate(DELEGATE.PROGRESS_UPDATE)
    private var energyConsumption: String by getDelegate(DELEGATE.ENERGY_CONSUMPTION)
    private var cyclerState: CyclerState by getDelegate(DELEGATE.CYCLER_STATE)

    val wallet: Wallet = Wallet(getDelegate(DELEGATE.WALLET_CHANGE))
    val receptionQueue: ReceptionQueue = ReceptionQueue(getDelegate(DELEGATE.QUEUE_STATE))

    val tickEngines: Map<ENGINE_NAME, TickEngine> = mapOf(
        TickEngine(ENGINE_NAME.PROGRESSBAR_TICK_ENGINE).mapToPair(),
        TickEngine(ENGINE_NAME.ENERGY_CONSUMPTION_TICK_ENGINE).mapToPair(),
        TickEngine(ENGINE_NAME.RECEPTION_QUEUE_TICK_ENGINE).mapToPair()
    )

    val engines: Map<ENGINE_NAME, Engine> = mapOf(
        Engine(ENGINE_NAME.TAP_ENGINE).mapToPair()
    )
    private lateinit var cycler: Cycler
    val rooms: MutableList<Room> = mutableListOf(
        Room().also { room ->
            room.insertDevice(READY_DEVICES.LED_LIGHT.get.also {
                it.efficiencyClass = EfficiencyClass.E
                it.quality = Quality.NEW_LOWEND
            })
            room.insertDevice(READY_DEVICES.KETTLE.get.also {
                it.efficiencyClass = EfficiencyClass.F
                it.quality = Quality.USED_LOWEND
            })
        })

    private val energyDemandGovernor: EnergyDemandGovernor = EnergyDemandGovernor(rooms)

    val initialBarConsumption = energyDemandGovernor.getCurrentDemand()
    var currentBarConsumption = initialBarConsumption

    fun setup() {
        setupMessages()
        setupCycler()
        setupTickEngines()
        setupEngines()
    }

    @SuppressLint("SetTextI18n")
    private fun setupMessages() {
        energyConsumption = "${currentBarConsumption.roundToDecimalAsString(3)} kW"
    }

    private fun setupCycler() {
        cycler = Cycler()
    }

    @SuppressLint("SetTextI18n")
    private fun setupTickEngines() {
        tickEngines[ENGINE_NAME.PROGRESSBAR_TICK_ENGINE]!!.apply {
            tickInterval = 100
            setMethod {
                if (progressBarCurrent > 0) {
                    progressBarCurrent -= ProgressBarConverter.convertConsumption(
                        currentBarConsumption
                    )
                } else {
                    finishMethod()
                }
            }
        }
        tickEngines[ENGINE_NAME.ENERGY_CONSUMPTION_TICK_ENGINE]!!.apply {
            tickInterval = 2500
            setMethod {
                currentBarConsumption = energyDemandGovernor.getNextDemand()
                energyConsumption = "${currentBarConsumption.roundToDecimalAsString(3)} kW"

            }
        }
        tickEngines[ENGINE_NAME.RECEPTION_QUEUE_TICK_ENGINE]!!.apply {
            tickInterval = 1000
            setMethod {
                if (Random.nextFloat() <= Config.NEW_PERSON_IN_QUEUE_CHANCE) {
                    receptionQueue.addToQueue(QueueBundle(Random.nextInt(1, 4),
                    listOf(READY_DEVICES.TV.get, READY_DEVICES.MICROWAVE.get.also { it.quality = Quality.NEW_TOP })))
                    //todo stopniowo zwiekszac wymagania co do sprzetow
                }
                val annoyedBundles = receptionQueue.removePatienceAndReturnEntriesWithoutPatience()
                receptionQueue.removeFromQueue(annoyedBundles)
            }
        }
    }

    private fun setupEngines() {
        engines[ENGINE_NAME.TAP_ENGINE]!!.setMethod {
            if (!tickEngines[ENGINE_NAME.PROGRESSBAR_TICK_ENGINE]!!.paused) {
                val current = progressBarCurrent + Config.TAP_POWER
                progressBarCurrent = if (current > progressBarMax) {
                    val overprod = current - progressBarMax
                    println("NADWYŻKA $overprod")
                    wallet.put(overprod / 1000f)
                    progressBarMax
                } else {
                    current
                }
                cyclerState = cycler.cycle()
            }
        }
    }

    fun run() {
        tickEngines.forEach { (_, tickEngine) -> tickEngine.start() }
    }

    fun pause() {
        tickEngines.forEach { (_, tickEngine) -> tickEngine.pause() }

    }

    fun resume() {
        tickEngines.forEach { (_, tickEngine) -> tickEngine.resume() }

    }

    fun stop() {
        tickEngines.forEach { (_, tickEngine) -> tickEngine.stop() }
    }

    private fun <T> getDelegate(enum: DELEGATE): ReadWriteProperty<Any?, T> {
        return delegatesMap[enum] as ReadWriteProperty<Any?, T>
    }

    fun <T> MutableList<T>.add(element: T): Boolean {
        println("EXECUTED INTERCEPTOR")
        return this.add(element)
    }
}

enum class ENGINE_NAME (val value: String) {
    UNDEFINED("UNDEFINED-ENGINE"),
    PROGRESSBAR_TICK_ENGINE("progressbar"),
    ENERGY_CONSUMPTION_TICK_ENGINE("energyConsumption"),
    RECEPTION_QUEUE_TICK_ENGINE("receptionQueue"),
    TAP_ENGINE("tap")
}


/*
        Room().apply {
            this.insertDevice(Device("Microwave", EfficiencyClass.D, 0.9f))
            this.insertDevice(
                Device(
                    "Electric kettle",
                    EfficiencyClass.C,
                    2.2f,
                    enabledByMaxTicks = 2
                )
            )
            this.insertDevice(
                Device(
                    "Vacuum cleaner",
                    EfficiencyClass.B,
                    1.7f,
                    enabledByMaxTicks = 4
                )
            )
            this.insertDevice(Device("TV", EfficiencyClass.B, 0.2f))
            this.insertDevice(Device("Desktop computer", EfficiencyClass.C, 0.5f))
            this.insertDevice(Device("LED light bulb", EfficiencyClass.Ap, 0.01f))
            this.insertDevice(Device("Laptop", EfficiencyClass.B, 0.07f, isOwnedByPlayer = false))
            this.upgradeMaxPeopleCount(2)
            this.bookRoom(3)
        }
*/