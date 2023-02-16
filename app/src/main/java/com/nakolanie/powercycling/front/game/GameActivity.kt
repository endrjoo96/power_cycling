package com.nakolanie.powercycling.front.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nakolanie.powercycling.*
import com.nakolanie.powercycling.Helpers.Companion.roundToDecimalAsString
import com.nakolanie.powercycling.back.GameContext
import com.nakolanie.powercycling.back.TickEngine
import com.nakolanie.powercycling.extensions.GameAppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass

class GameActivity : GameAppCompatActivity() {
    companion object {
        private var CURRENT_GAME_CONTEXT: GameContext? = null
        fun GetContext() = CURRENT_GAME_CONTEXT!!
    }

    private val delegatesMap: Map<DELEGATE, ReadWriteProperty<Any?, *>> = mapOf(
        Pair(DELEGATE.PROGRESS_UPDATE,
            Delegates.observable(Config.START_PROGRESSBAR_VALUE) { _, _, newValue ->
                this.gameActivity_progressBar.progress = newValue
            }),
        Pair(DELEGATE.ENERGY_CONSUMPTION,
            Delegates.observable("") { _, _, newValue ->
                this.gameActivity_textView_currentEnergyDemand.text = newValue
            }),
        Pair(DELEGATE.CYCLER_STATE,
            Delegates.observable(CyclerState.CYCLE_FAST_1) { _, _, newValue ->
                Helpers.setImageResourceFromBitmap(
                    this,
                    gameActivity_imageView_character,
                    newValue.mipmapPath
                )
            }),
        Pair(DELEGATE.WALLET_CHANGE,
            Delegates.observable(100f) { _, _, newValue ->
                this.gameActivity_textView_wallet.text = newValue.roundToDecimalAsString(2)
            }),
        Pair(DELEGATE.QUEUE_STATE,
            Delegates.observable(0) { _, _, newValue ->
                this.gameActivity_textView_receptionQueueSize.text = newValue.toString()
            })
    )

    private val progressUpdateDelegate =
        Delegates.observable(Config.START_PROGRESSBAR_VALUE) { _, _, newValue ->
            this.gameActivity_progressBar.progress = newValue
        }
    private val energyConsumptionDelegate = Delegates.observable("") { _, _, newValue ->
        this.gameActivity_textView_currentEnergyDemand.text = newValue
    }
    private val cyclerStateDelegate =
        Delegates.observable(CyclerState.CYCLE_FAST_1) { _, _, newValue ->
            Helpers.setImageResourceFromBitmap(
                this,
                gameActivity_imageView_character,
                newValue.mipmapPath
            )
        }
    private val walletChangeDelegate = Delegates.observable(100f) { _, _, newValue ->
        this.gameActivity_textView_wallet.text = newValue.roundToDecimalAsString(2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        gameActivity_progressBar.max = Config.MAX_PROGRESSBAR_VALUE
        this.gameActivity_constraintLayout.isSoundEffectsEnabled = false

        CURRENT_GAME_CONTEXT = GameContext(
            Config.MAX_PROGRESSBAR_VALUE,
            finishMethod = { finish() },
            delegatesMap
        )
        gameActivity_textView_wallet.text = GetContext().wallet.check().roundToDecimalAsString(2)
        GetContext().setup()
        GetContext().run()
    }

    override fun onDestroy() {
        GetContext().stop()
        clearContext()
        super.onDestroy()
    }

    private fun clearContext() {
        CURRENT_GAME_CONTEXT = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 100) {
            GetContext().resume()
        }
    }
}

enum class DELEGATE(val assignedClass: KClass<*>) {
    PROGRESS_UPDATE(Int::class),
    ENERGY_CONSUMPTION(String::class),
    CYCLER_STATE(CyclerState::class),
    WALLET_CHANGE(Float::class),
    QUEUE_STATE(Int::class);

}