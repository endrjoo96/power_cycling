package com.nakolanie.powercycling.activities.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.models.Room
import com.nakolanie.powercycling.context.GameContext
import com.nakolanie.powercycling.extensions.GameAppCompatActivity
import com.nakolanie.powercycling.models.device.Device
import kotlinx.android.synthetic.main.activity_game_manage_device.*
import kotlinx.android.synthetic.main.activity_game_manage_room.*

class Game_ManageDevice : GameAppCompatActivity() {
    private lateinit var device: Device
    private lateinit var room: Room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_manage_device)
        val roomIndex: Int = intent.extras!!.getInt("roomIndex")
        val deviceIndex: Int = intent.extras!!.getInt("deviceIndex")

        room = GameContext.get().rooms[roomIndex]
        device = room.devices[deviceIndex]
        refresh()
    }

    @SuppressLint("SetTextI18n")
    private fun refresh() {
        manageDevice_textView_balance.text = GameContext.get().wallet.check().toString()
        manageDevice_textView_title.text = "${room.name} >> ${device.getName()}"
        manageDevice_textView_efficiencyClass.text = device.efficiencyClass.friendlyName
        manageDevice_textView_quality.text = device.quality.friendlyDescription
        manageDevice_textView_overallEnergyConsumption.text = "${(device.getPowerConsumption()*1000).toInt()} W"
        manageDevice_textView_efficiencyClassUpgradeCost.text = calculateEfficiencyClassUpgrade().toString()
        manageDevice_textView_qualityUpgradeCost.text = calculateQualityUpgrade().toString()

        setControlsBasedOnOwnership()
    }

    private fun calculateQualityUpgrade(): Float {
//        val defaultMultiplicator = device.quality.priceMultiplicator;
            return device.calculatePrice()
//        if (defaultMultiplicator < 1) {
//        }
    }

    private fun calculateEfficiencyClassUpgrade(): Float {
        return device.calculatePrice()
    }

    private fun setControlsBasedOnOwnership() {
        manageDevice_button_efficiencyClass.isEnabled = device.isOwnedByPlayer
        manageDevice_button_quality.isEnabled = device.isOwnedByPlayer
        manageDevice_textView_notYourPropertyAlert.visibility =
            if (device.isOwnedByPlayer) View.GONE else View.VISIBLE
        if(!device.isOwnedByPlayer) {
            manageDevice_textView_efficiencyClassUpgradeCost.text = "----"
            manageDevice_textView_qualityUpgradeCost.text = "----"
        }
    }

    fun onUpgradeEfficiencyClass(v: View) {
        device.upgradeEfficiencyClass()
        refresh()
    }

    fun onUpgradeQuality(v: View) {
        device.upgradeQuality()
        refresh()
    }

}