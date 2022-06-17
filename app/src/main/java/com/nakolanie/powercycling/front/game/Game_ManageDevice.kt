package com.nakolanie.powercycling.front.game

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nakolanie.powercycling.Device
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.Room
import kotlinx.android.synthetic.main.activity_game_manage_device.*

class Game_ManageDevice : AppCompatActivity() {
    private lateinit var device: Device
    private lateinit var room: Room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_manage_device)
        val roomIndex: Int = intent.extras!!.getInt("roomIndex")
        val deviceIndex: Int = intent.extras!!.getInt("deviceIndex")

        room = GameActivity.GET_CONTEXT().rooms[roomIndex]
        device = room.devices[deviceIndex]
        refresh()
    }

    private fun refresh() {
        manageDevice_textView_title.setText("${room.name} >> ${device.name}")
        manageDevice_textView_efficiencyClass.setText(device.efficiencyClass.friendlyName)
        manageDevice_textView_overallEnergyConsumption.setText("${(device.getPowerConsumption()*1000).toInt()} W")
        setControlsBasedOnOwnership()
    }

    fun setControlsBasedOnOwnership() {
        manageDevice_button_efficiencyClass.isEnabled = device.isOwnedByPlayer
        manageDevice_textView_notYourPropertyAlert.visibility =
            if (device.isOwnedByPlayer) View.GONE else View.VISIBLE
    }

    fun onUpgradeEfficiencyClass(v: View) {
        device.upgradeEfficiencyClass()
        refresh()
    }

    fun onBack(v: View) {
        this.finish()
    }
}