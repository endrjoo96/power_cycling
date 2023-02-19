package com.nakolanie.powercycling.context

import android.annotation.SuppressLint
import com.nakolanie.powercycling.configs.GameConfig
import com.nakolanie.powercycling.converters.ProgressBarConverter
import com.nakolanie.powercycling.delegates.DelegateService
import com.nakolanie.powercycling.enums.*
import com.nakolanie.powercycling.exceptions.ContextNotInitiatedException
import com.nakolanie.powercycling.models.*
import com.nakolanie.powercycling.models.device.KettleDevice
import com.nakolanie.powercycling.models.device.LightBulbDevice
import com.nakolanie.powercycling.models.device.TvDevice
import com.nakolanie.powercycling.services.*
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
            context =
                GameContext(GameConfig.MAX_PROGRESSBAR_VALUE, finishMethod, delegateServicesMap)
            return get()
        }

        fun get(): GameContext {
            if (context == null) {
                throw ContextNotInitiatedException()
            }
            return context!!
        }

        fun clear() {
            context = null
        }
    }


    private var progressBarCurrent: Int by getPropertyToDelegate(DelegateDefinition.PROGRESS_UPDATE)
    val energyConsumptionService =
        EnergyConsumptionService(getDelegate(DelegateDefinition.ENERGY_CONSUMPTION))
    private val cyclingService =
        CyclingService(CyclingCharacter(getDelegate(DelegateDefinition.CYCLING_CHARACTER_STATE)))

    val wallet: Wallet = Wallet(getPropertyToDelegate(DelegateDefinition.WALLET_STATE))
    val queueService: QueueService =
        QueueService(getPropertyToDelegate(DelegateDefinition.QUEUE_STATE))

    val engineService = EngineService()

    val rooms: MutableList<Room> = mutableListOf()

    private val energyDemandGovernorService: EnergyDemandGovernorService =
        EnergyDemandGovernorService(rooms)

    var currentConsumption = energyDemandGovernorService.getCurrentDemand()

    fun setup() {
        setupMessages()
        setupEngines()
        setupDefaultRoomWithDevices()

        //TODO usunac
        tempAddToQueue()
    }

    //TODO usunac
    private fun tempAddToQueue() {
        queueService.addToQueue(
            QueueBundle(
                1,
                listOf(
                    LightBulbDevice(),
                    KettleDevice().also {
                        it.efficiencyClass = EfficiencyClass.F
                        it.quality = Quality.USED_LOWEND
                    })
            )
        )
        queueService.addToQueue(
            QueueBundle(
                4,
                listOf(
                    TvDevice().also {
                        it.efficiencyClass = EfficiencyClass.A
                        it.quality = Quality.NEW_GOOD
                    })
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setupMessages() {
        energyConsumptionService.write(currentConsumption)
    }

    @SuppressLint("SetTextI18n")
    private fun setupEngines() {
        engineService.apply {
            addEngine(TickEngine(EngineName.PROGRESSBAR_TICK_ENGINE).apply {
                tickInterval = 100
                setMethod {
                    if (progressBarCurrent > 0) {
                        progressBarCurrent -=
                            ProgressBarConverter.convertConsumption(currentConsumption)
                    } else {
                        finishMethod()
                    }
                }
            })
            addEngine(TickEngine(EngineName.ENERGY_CONSUMPTION_TICK_ENGINE).apply {
                tickInterval = 2500
                setMethod {
                    currentConsumption = energyDemandGovernorService.getNextDemand()
                    energyConsumptionService.write(currentConsumption)
                }
            })
            addEngine(TickEngine(EngineName.RECEPTION_QUEUE_TICK_ENGINE).apply {
                tickInterval = 1000
                setMethod {
                    if (Random.nextFloat() <= GameConfig.NEW_PERSON_IN_QUEUE_CHANCE) {
                        queueService.addToQueue(
                            QueueBundle(
                                Random.nextInt(1, 4),
                                listOf(
                                    LightBulbDevice(),
                                    KettleDevice().also {
                                        it.efficiencyClass = EfficiencyClass.F
                                        it.quality = Quality.USED_LOWEND
                                    })
                            )
                        )
                        //todo stopniowo zwiekszac wymagania co do sprzetow
                    }
                    val annoyedBundles =
                        queueService.removePatienceAndReturnEntriesWithoutPatience()
                    queueService.removeFromQueue(annoyedBundles)
                }
            })
            addEngine(Engine(EngineName.TAP_ENGINE).apply {
                setMethod {
                    cyclingService.cycle()
                    if (!engineService.get<TickEngine>(EngineName.PROGRESSBAR_TICK_ENGINE).paused) {
                        val current = progressBarCurrent + GameConfig.TAP_POWER
                        progressBarCurrent = if (current > progressBarMax) {
                            val overprod = current - progressBarMax
                            wallet.put(overprod / 1000f)
                            progressBarMax
                        } else {
                            current
                        }
                    }
                }
            })
        }
    }

    private fun setupDefaultRoomWithDevices() {
        rooms.addAll(
            listOf(
                Room().also { room ->
                    room.insertDevice(LightBulbDevice().also {
                        it.efficiencyClass = EfficiencyClass.E
                        it.quality = Quality.NEW_LOWEND
                    })
                    room.insertDevice(KettleDevice().also {
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
    private fun <T> getPropertyToDelegate(anEnum: DelegateDefinition): ReadWriteProperty<Any?, T> {
        delegateServicesMap[anEnum]!!.getObservable()
        return delegateServicesMap[anEnum]!!.getObservable() as ReadWriteProperty<Any?, T>
    }

    private fun <T> getDelegate(def: DelegateDefinition): T {
        val delegate: T by getPropertyToDelegate(def)
        return delegate
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