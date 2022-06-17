package com.nakolanie.powercycling.front.game

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nakolanie.powercycling.*
import com.nakolanie.powercycling.Helpers.Companion.roundToDecimalAsString
import com.nakolanie.powercycling.back.Engine
import com.nakolanie.powercycling.back.TickEngine
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    companion object {
        private lateinit var CURRENT_GAME_CONTEXT: GameActivity
        fun GET_CONTEXT() = CURRENT_GAME_CONTEXT
    }

    private val PROGRESSBAR_TICK_ENGINE = "progressbar"
    private val GAME_EVENTS_TICK_ENGINE = "gameEvents"
    private val TAP_ENGINE = "tap"

    private val tickEngines: Map<String, TickEngine> = mapOf(
        Pair(PROGRESSBAR_TICK_ENGINE, TickEngine(PROGRESSBAR_TICK_ENGINE)),
        Pair(GAME_EVENTS_TICK_ENGINE, TickEngine(GAME_EVENTS_TICK_ENGINE))
    )

    private val engines: Map<String, Engine> = mapOf(
        Pair(TAP_ENGINE, Engine(TAP_ENGINE))
    )

    private lateinit var cycler: Cycler
    val rooms: MutableList<Room> = mutableListOf(
        Room().apply {
            this.insertDevice(Device("Microwave", EfficiencyClass.D, 0.9f))
            this.insertDevice(Device("Electric kettle", EfficiencyClass.C, 2.2f, enabledByMaxTicks = 2))
            this.insertDevice(Device("Vacuum cleaner", EfficiencyClass.B, 1.7f, enabledByMaxTicks = 4))
            this.insertDevice(Device("TV", EfficiencyClass.B, 0.2f))
            this.insertDevice(Device("Desktop computer", EfficiencyClass.C, 0.5f))
            this.insertDevice(Device("LED light bulb", EfficiencyClass.Ap, 0.01f))
            this.insertDevice(Device("Laptop", EfficiencyClass.B, 0.07f, isOwnedByPlayer = false))
            this.upgradeMaxPeopleCount(2)
            this.bookRoom(3)
        })

    private val energyDemandGovernor: EnergyDemandGovernor = EnergyDemandGovernor(rooms)

    val initialBarConsumption = energyDemandGovernor.getCurrentDemand()
    var currentBarConsumption = initialBarConsumption

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CURRENT_GAME_CONTEXT = this
        setContentView(R.layout.activity_game)
        setup()
        run()
    }

    private fun setup() {
        this.gameActivity_constraintLayout.isSoundEffectsEnabled = false
        setupMessages()
        setupCycler()
        setupTickEngines()
        setupEngines()
    }

    @SuppressLint("SetTextI18n")
    private fun setupMessages() {
        this.gameActivity_textView_currentEnergyDemand.text =
            "${currentBarConsumption.roundToDecimalAsString(3)} kW"
    }

    private fun setupCycler() {
        cycler = Cycler(this, this.gameActivity_imageView_character)
    }

    @SuppressLint("SetTextI18n")
    private fun setupTickEngines() {
        tickEngines[PROGRESSBAR_TICK_ENGINE]!!.also {
            it.tickInterval = 100
            it.setMethod {
                if (this.gameActivity_progressBar.progress > 0) {
                    this.gameActivity_progressBar.progress -= ProgressBarConverter.convertConsumption(
                        currentBarConsumption
                    )
                } else {
                    finish()
                }
            }
        }
        tickEngines[GAME_EVENTS_TICK_ENGINE]!!.also {
            it.tickInterval = 2500
            it.setMethod {
                currentBarConsumption = energyDemandGovernor.getNextDemand()
                this.gameActivity_textView_currentEnergyDemand.text =
                    "${currentBarConsumption.roundToDecimalAsString(3)} kW"
            }
        }
        this.gameActivity_progressBar.max = 40000
        this.gameActivity_progressBar.progress = 20000
    }

    private fun setupEngines() {
        engines[TAP_ENGINE]!!.setMethod {
            if (!tickEngines[PROGRESSBAR_TICK_ENGINE]!!.paused) {
                this.gameActivity_progressBar.progress += Config.TAP_POWER
                cycler.cycle()
            }
        }
    }

    private fun run() {
        tickEngines.forEach { (_, tickEngine) -> tickEngine.start() }
    }

    private fun pause() {
        tickEngines.forEach { (_, tickEngine) -> tickEngine.pause() }

    }

    private fun resume() {
        tickEngines.forEach { (_, tickEngine) -> tickEngine.resume() }

    }

    override fun onStop() {
        tickEngines.forEach { (_, tickEngine) -> tickEngine.stop() }

        super.onStop()
    }

    fun onScreenTap(v: View) {
        engines[TAP_ENGINE]!!.executeMethod()

    }

    fun onRoomButtonClick(v: View) {
        pause()
        val newIntent = Intent(this, Game_RoomsManagementActivity::class.java)
        startActivityForResult(newIntent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 100) {
            resume()
        }
    }
}