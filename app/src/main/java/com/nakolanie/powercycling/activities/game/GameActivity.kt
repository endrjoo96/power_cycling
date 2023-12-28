package com.nakolanie.powercycling.activities.game

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.configs.GameConfig
import com.nakolanie.powercycling.context.GameContext
import com.nakolanie.powercycling.delegates.*
import com.nakolanie.powercycling.enums.DelegateDefinition
import com.nakolanie.powercycling.extensions.GameAppCompatActivity
import com.nakolanie.powercycling.utils.MathUtils.Companion.roundToDecimalAsString
import com.nakolanie.powercycling.utils.ResourcesUtils
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : GameAppCompatActivity() {

    private val delegateServicesMap: Map<DelegateDefinition, DelegateService<*>> = mapOf(
        ProgressUpdateDelegateProducer().produce { updatedValue ->
            this.gameActivity_progressBar.progress = updatedValue
        },
        EnergyConsumptionDelegateProducer().produce { updatedValue ->
            this.gameActivity_textView_currentEnergyDemand.text = updatedValue
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
        gameActivity_textView_wallet.text = context.wallet.checkAsString()
        context.setup()
        context.runTickEngines()

        //przycisk serwisowy, do usunięcia lub ukrycia
        registerDevButton()
    }

    //przycisk serwisowy, do usunięcia lub ukrycia
    @SuppressLint("ClickableViewAccessibility")
    private fun registerDevButton(){
        val upCountDownTimer: CountDownTimer = object : CountDownTimer(Long.MAX_VALUE, 80) {
            override fun onTick(l: Long) {
                GameContext.get().actionForTap()
            }
            override fun onFinish() {}
        }
        dev_autoclicker.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                upCountDownTimer.start()
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                upCountDownTimer.cancel()
            }
            true
        }
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

