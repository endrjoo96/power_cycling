package com.nakolanie.powercycling.front.game

import android.content.Intent
import android.os.Bundle
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.context.GameContext
import com.nakolanie.powercycling.configs.GameConfig
import com.nakolanie.powercycling.enums.CyclingCharacterState
import com.nakolanie.powercycling.enums.DelegateDefinition
import com.nakolanie.powercycling.extensions.GameAppCompatActivity
import com.nakolanie.powercycling.utils.MathUtils.Companion.roundToDecimalAsString
import com.nakolanie.powercycling.utils.ResourcesUtils
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

class GameActivity : GameAppCompatActivity() {
    private val delegatesMap: Map<DelegateDefinition, ReadWriteProperty<Any?, *>> = mapOf(
        Pair(
            DelegateDefinition.PROGRESS_UPDATE,
            Delegates.observable(GameConfig.START_PROGRESSBAR_VALUE) { _, _, newValue ->
                this.gameActivity_progressBar.progress = newValue
            }),
        Pair(
            DelegateDefinition.ENERGY_CONSUMPTION,
            Delegates.observable("") { _, _, newValue ->
                this.gameActivity_textView_currentEnergyDemand.text = newValue
            }),
        Pair(
            DelegateDefinition.CYCLER_STATE,
            Delegates.observable(CyclingCharacterState.CYCLE_FAST_1) { _, _, newValue ->
                ResourcesUtils.setImageResourceFromBitmap(
                    this,
                    gameActivity_imageView_character,
                    newValue.mipmapPath
                )
            }),
        Pair(
            DelegateDefinition.WALLET_CHANGE,
            Delegates.observable(100f) { _, _, newValue ->
                this.gameActivity_textView_wallet.text = newValue.roundToDecimalAsString(2)
            }),
        Pair(
            DelegateDefinition.QUEUE_STATE,
            Delegates.observable(0) { _, _, newValue ->
                this.gameActivity_textView_receptionQueueSize.text = newValue.toString()
            })
    )

    private val progressUpdateDelegate =
        Delegates.observable(GameConfig.START_PROGRESSBAR_VALUE) { _, _, newValue ->
            this.gameActivity_progressBar.progress = newValue
        }
    private val energyConsumptionDelegate = Delegates.observable("") { _, _, newValue ->
        this.gameActivity_textView_currentEnergyDemand.text = newValue
    }
    private val cyclerStateDelegate =
        Delegates.observable(CyclingCharacterState.CYCLE_FAST_1) { _, _, newValue ->
            ResourcesUtils.setImageResourceFromBitmap(
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
        gameActivity_progressBar.max = GameConfig.MAX_PROGRESSBAR_VALUE
        this.gameActivity_constraintLayout.isSoundEffectsEnabled = false

        val context = GameContext.init(
            finishMethod = { finish() },
            delegatesMap
        )
        gameActivity_textView_wallet.text = context.wallet.check().roundToDecimalAsString(2)
        context.setup()
        context.runTickEngines()
    }

    override fun onDestroy() {
        GameContext.get().stopTickEngines()
        GameContext.clear()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 100) {
            GameContext.get().resumeTickEngines()
        }
    }
}

