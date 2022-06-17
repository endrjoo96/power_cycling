package com.nakolanie.powercycling.front

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.Room
import com.nakolanie.powercycling.front.game.GameActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup(){
    }

    fun onStartButtonClick(v: View){
        Room.resetRoomSequence()
        val newGameIntent = Intent(this, GameActivity::class.java)
        startActivity(newGameIntent)
    }

    fun onExitButtonClick(v: View){
        this.finish()
        exitProcess(0)
    }
}