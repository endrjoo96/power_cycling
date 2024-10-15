package com.nakolanie.powercycling.activities.game

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.context.GameContext
import com.nakolanie.powercycling.extensions.GameAppCompatActivity
import com.nakolanie.powercycling.models.Device
import com.nakolanie.powercycling.models.Room
import kotlinx.android.synthetic.main.activity_game_manage_device.manageDevice_button_efficiencyClass
import kotlinx.android.synthetic.main.activity_game_manage_device.manageDevice_button_quality
import kotlinx.android.synthetic.main.activity_game_manage_device.manageDevice_textView_balance
import kotlinx.android.synthetic.main.activity_game_manage_device.manageDevice_textView_efficiencyClass
import kotlinx.android.synthetic.main.activity_game_manage_device.manageDevice_textView_efficiencyClassUpgradeCost
import kotlinx.android.synthetic.main.activity_game_manage_device.manageDevice_textView_notYourPropertyAlert
import kotlinx.android.synthetic.main.activity_game_manage_device.manageDevice_textView_overallEnergyConsumption
import kotlinx.android.synthetic.main.activity_game_manage_device.manageDevice_textView_quality
import kotlinx.android.synthetic.main.activity_game_manage_device.manageDevice_textView_qualityUpgradeCost
import kotlinx.android.synthetic.main.activity_game_manage_device.manageDevice_textView_title

class Game_ManageDevice : GameAppCompatActivity() {
    private lateinit var device: Device
    private lateinit var room: Room
    private var calculatedEfficiencyClassValue: Float = 0f
    private var calculatedQualityValue: Float = 0f

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
        recalculateValues()
        manageDevice_textView_balance.text = GameContext.get().wallet.checkAsString()
        manageDevice_textView_title.text = "${room.name} >> ${device.getName()}"
        manageDevice_textView_efficiencyClass.text = device.efficiencyClass.friendlyName
        manageDevice_textView_quality.text = device.quality.friendlyDescription
        manageDevice_textView_overallEnergyConsumption.text =
            "${(device.getPowerConsumption() * 1000).toInt()} W"
        manageDevice_textView_efficiencyClassUpgradeCost.text =
            getEfficiencyClassUpgradeValue().toString()
        manageDevice_textView_qualityUpgradeCost.text = getQualityUpgradeValue().toString()
        manageDevice_textView_notYourPropertyAlert.visibility = View.GONE

        if (device.isOwnedByPlayer) {
            setControlsBasedOnAvailableCash()
        } else {
            setControlsAsNotOwnProperty()
        }
    }

    private fun recalculateValues() {
        calculatedEfficiencyClassValue = calculateEfficiencyClassUpgrade()
        calculatedQualityValue = calculateQualityUpgrade()
    }

    private fun calculateQualityUpgrade(): Float {
//        val defaultMultiplicator = device.quality.priceMultiplicator;
//        if (defaultMultiplicator < 1) {
//        }
        // todo rozwiązanie tymczasowe
        return device.calculatePrice()
    }

    private fun calculateEfficiencyClassUpgrade(): Float {
        // todo rozwiązanie tymczasowe
        return device.calculatePrice()
    }

    private fun getEfficiencyClassUpgradeValue(): Float {
        return calculatedEfficiencyClassValue
    }

    private fun getQualityUpgradeValue(): Float {
        return calculatedQualityValue
    }

    private fun setControlsAsNotOwnProperty() {
        manageDevice_button_efficiencyClass.isEnabled = false
        manageDevice_button_quality.isEnabled = false
        manageDevice_textView_notYourPropertyAlert.visibility = View.VISIBLE
        manageDevice_textView_efficiencyClassUpgradeCost.text = "----"
        manageDevice_textView_qualityUpgradeCost.text = "----"
    }

    private fun setControlsBasedOnAvailableCash() {
        val wallet = GameContext.get().wallet.check()
        if (wallet < getEfficiencyClassUpgradeValue()) {
            manageDevice_button_efficiencyClass.isEnabled = false
            manageDevice_textView_efficiencyClassUpgradeCost.setTextColor(Color.parseColor("#AEAC2B2B"))
        }
        if (wallet < getQualityUpgradeValue()) {
            manageDevice_button_quality.isEnabled = false
            manageDevice_textView_qualityUpgradeCost.setTextColor(Color.parseColor("#AEAC2B2B"))
        }
    }

    fun onUpgradeEfficiencyClass(v: View) {
        device.upgradeEfficiencyClass()
        GameContext.get().wallet.pay(getEfficiencyClassUpgradeValue())
        refresh()
    }

    fun onUpgradeQuality(v: View) {
        device.upgradeQuality()
        GameContext.get().wallet.pay(getQualityUpgradeValue())
        refresh()
    }

}