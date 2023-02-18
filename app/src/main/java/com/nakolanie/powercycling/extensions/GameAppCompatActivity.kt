package com.nakolanie.powercycling.extensions

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nakolanie.powercycling.context.GameContext
import com.nakolanie.powercycling.enums.EngineName
import com.nakolanie.powercycling.front.game.GameActivity
import com.nakolanie.powercycling.front.game.Game_QueueManagementActivity
import com.nakolanie.powercycling.front.game.Game_RoomsManagementActivity
import com.nakolanie.powercycling.models.Engine

open class GameAppCompatActivity : AppCompatActivity() {
    fun onBack(v: View) {
        setResult(100)
        this.finish()
    }

    fun onGameScreenTap(v: View) {
        GameContext.get().engineService.get<Engine>(EngineName.TAP_ENGINE).executeMethod()
    }

    fun onRoomButtonClick(v: View) {
        GameContext.get().pauseTickEngines()
        val newIntent = Intent(this, Game_RoomsManagementActivity::class.java)
        startActivityForResult(newIntent, 100)
    }

    fun onReceptionButtonClick(v: View) {
        GameContext.get().pauseTickEngines()
        val newIntent = Intent(this, Game_QueueManagementActivity::class.java)
        startActivityForResult(newIntent, 100)
    }
}