package com.nakolanie.powercycling.front.game

import android.content.Intent
import android.os.Bundle
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.configs.GameConfig
import com.nakolanie.powercycling.context.GameContext
import com.nakolanie.powercycling.delegates.*
import com.nakolanie.powercycling.enums.CyclingCharacterState
import com.nakolanie.powercycling.enums.DelegateDefinition
import com.nakolanie.powercycling.extensions.GameAppCompatActivity
import com.nakolanie.powercycling.utils.MathUtils.Companion.roundToDecimalAsString
import com.nakolanie.powercycling.utils.ResourcesUtils
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.properties.Delegates

class GameActivity : GameAppCompatActivity() {

    private val delegateServicesMap: Map<DelegateDefinition, DelegateService<*>> = mapOf(
        ProgressUpdateDelegateProducer().produce { updatedValue ->
            gameActivity_progressBar.progress = updatedValue
        },
        EnergyConsumptionDelegateProducer().produce { updatedValue ->
            gameActivity_textView_currentEnergyDemand.text = updatedValue
        },
        CyclingCharacterStateDelegateProducer().produce { updatedValue ->
            ResourcesUtils.setImageResourceFromBitmap(
                this,
                gameActivity_imageView_character,
                updatedValue.mipmapPath
            )
        },
        WalletStateDelegateProducer().produce { updatedValue ->
            this.gameActivity_textView_wallet.text = updatedValue.roundToDecimalAsString(2)
        },
        QueueStateDelegateProducer().produce { updatedValue ->
            this.gameActivity_textView_receptionQueueSize.text = updatedValue.toString()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        gameActivity_progressBar.max = GameConfig.MAX_PROGRESSBAR_VALUE
        this.gameActivity_constraintLayout.isSoundEffectsEnabled = false

        val context = GameContext.init(
            finishMethod = { finish() },
            delegateServicesMap
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

