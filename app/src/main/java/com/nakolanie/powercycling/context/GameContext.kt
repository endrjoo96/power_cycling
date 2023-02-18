package com.nakolanie.powercycling.context

import android.annotation.SuppressLint
import com.nakolanie.powercycling.*
import com.nakolanie.powercycling.configs.GameConfig
import com.nakolanie.powercycling.delegates.DelegateService
import com.nakolanie.powercycling.enums.CyclingCharacterState
import com.nakolanie.powercycling.enums.EfficiencyClass
import com.nakolanie.powercycling.enums.EngineName
import com.nakolanie.powercycling.enums.ReadyDevice
import com.nakolanie.powercycling.enums.DelegateDefinition
import com.nakolanie.powercycling.exceptions.ContextNotInitiatedException
import com.nakolanie.powercycling.models.CyclingCharacter
import com.nakolanie.powercycling.models.Engine
import com.nakolanie.powercycling.models.TickEngine
import com.nakolanie.powercycling.models.Wallet
import com.nakolanie.powercycling.services.EnergyDemandGovernorService
import com.nakolanie.powercycling.services.EngineService
import com.nakolanie.powercycling.utils.MathUtils.Companion.roundToDecimalAsString
import kotlin.properties.ReadWriteProperty
import kotlin.random.Random

class GameContext private constructor(
    var progressBarMax: Int,
    private val finishMethod: () -> Unit,
    private val delegateServicesMap: Map<DelegateDefinition, DelegateService<*>>
) {

    companion object {
        private var context: GameContext? = null

        fun init(
            finishMethod: () -> Unit,
            delegateServicesMap: Map<DelegateDefinition, DelegateService<*>>
        ): GameContext {
            context = GameContext(GameConfig.MAX_PROGRESSBAR_VALUE, finishMethod, delegateServicesMap)
            return get()
        }

        fun get(): GameContext{
            if (context == null) {
                throw ContextNotInitiatedException()
            }
            return context!!
        }

        fun clear() {
            context = null
        }
    }


    private var progressBarCurrent: Int by getDelegate(DelegateDefinition.PROGRESS_UPDATE)
    private var energyConsumption: String by getDelegate(DelegateDefinition.ENERGY_CONSUMPTION)
    private var cyclingCharacterState: CyclingCharacterState by getDelegate(DelegateDefinition.CYCLING_CHARACTER_STATE)

    val wallet: Wallet = Wallet(getDelegate(DelegateDefinition.WALLET_STATE))
    val receptionQueue: ReceptionQueue = ReceptionQueue(getDelegate(DelegateDefinition.QUEUE_STATE))

    val engineService = EngineService()

    private lateinit var cyclingCharacter: CyclingCharacter
    val rooms: MutableList<Room> = mutableListOf()

    private val energyDemandGovernorService: EnergyDemandGovernorService =
        EnergyDemandGovernorService(rooms)

    val initialBarConsumption = energyDemandGovernorService.getCurrentDemand()
    var currentBarConsumption = initialBarConsumption

    fun setup() {
        setupMessages()
        setupCycler()
        setupEngines()
        setupDefaultRoomWithDevices()
    }

    @SuppressLint("SetTextI18n")
    private fun setupMessages() {
        energyConsumption = "${currentBarConsumption.roundToDecimalAsString(3)} kW"
    }

    private fun setupCycler() {
        cyclingCharacter = CyclingCharacter()
    }

    @SuppressLint("SetTextI18n")
    private fun setupEngines() {
        engineService.apply {
            addEngine(TickEngine(EngineName.PROGRESSBAR_TICK_ENGINE).apply {
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
            })
            addEngine(TickEngine(EngineName.ENERGY_CONSUMPTION_TICK_ENGINE).apply {
                tickInterval = 2500
                setMethod {
                    currentBarConsumption = energyDemandGovernorService.getNextDemand()
                    energyConsumption = "${currentBarConsumption.roundToDecimalAsString(3)} kW"

                }
            })
            addEngine(TickEngine(EngineName.RECEPTION_QUEUE_TICK_ENGINE).apply {
                tickInterval = 1000
                setMethod {
                    if (Random.nextFloat() <= GameConfig.NEW_PERSON_IN_QUEUE_CHANCE) {
                        receptionQueue.addToQueue(
                            QueueBundle(
                                Random.nextInt(1, 4),
                                listOf(
                                    ReadyDevice.TV.get,
                                    ReadyDevice.MICROWAVE.get.also { it.quality = Quality.NEW_TOP })
                            )
                        )
                        //todo stopniowo zwiekszac wymagania co do sprzetow
                    }
                    val annoyedBundles = receptionQueue.removePatienceAndReturnEntriesWithoutPatience()
                    receptionQueue.removeFromQueue(annoyedBundles)
                }
            })
            addEngine(Engine(EngineName.TAP_ENGINE).apply {
                setMethod {
                    if (!engineService.get<TickEngine>(EngineName.PROGRESSBAR_TICK_ENGINE).paused) {
                        val current = progressBarCurrent + GameConfig.TAP_POWER
                        progressBarCurrent = if (current > progressBarMax) {
                            val overprod = current - progressBarMax
                            println("NADWYŻKA $overprod")
                            wallet.put(overprod / 1000f)
                            progressBarMax
                        } else {
                            current
                        }
                        cyclingCharacterState = cyclingCharacter.cycle()
                    }
                }
            })
        }
    }

    private fun setupDefaultRoomWithDevices() {
        rooms.addAll(
            listOf(
                Room().also { room ->
                    room.insertDevice(ReadyDevice.LED_LIGHT.get.also {
                        it.efficiencyClass = EfficiencyClass.E
                        it.quality = Quality.NEW_LOWEND
                    })
                    room.insertDevice(ReadyDevice.KETTLE.get.also {
                        it.efficiencyClass = EfficiencyClass.F
                        it.quality = Quality.USED_LOWEND
                    })
                })
        )
    }

    fun runTickEngines() {
        engineService.getTickEngines().forEach { it.start() }
    }

    fun pauseTickEngines() {
        engineService.getTickEngines().forEach { it.pause() }

    }

    fun resumeTickEngines() {
        engineService.getTickEngines().forEach { it.resume() }

    }

    fun stopTickEngines() {
        engineService.getTickEngines().forEach { it.stop() }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getDelegate(anEnum: DelegateDefinition): ReadWriteProperty<Any?, T> {
        return delegateServicesMap[anEnum]!!.getObservable() as ReadWriteProperty<Any?, T>
    }
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