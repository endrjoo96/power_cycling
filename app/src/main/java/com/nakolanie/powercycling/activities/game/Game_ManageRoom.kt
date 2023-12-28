package com.nakolanie.powercycling.activities.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.models.Room
import com.nakolanie.powercycling.context.GameContext
import com.nakolanie.powercycling.extensions.GameAppCompatActivity
import com.nakolanie.powercycling.models.Wallet
import com.nakolanie.powercycling.utils.MathUtils.Companion.roundToDecimalAsString
import kotlinx.android.synthetic.main.activity_game_manage_room.*

class Game_ManageRoom : GameAppCompatActivity() {
    private lateinit var room: Room
    private lateinit var wallet: Wallet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_manage_room)
        val roomIndex: Int = intent.extras!!.getInt("roomIndex")
        room = GameContext.get().rooms[roomIndex]
        wallet = GameContext.get().wallet

        manageRoom_textView_title.text = room.name

        refresh()
    }

    private fun refresh() {
        manageRoom_textView_balance.text = wallet.checkAsString()
        manageRoom_linearLayout_devicesList.removeAllViews()
        addButtonToUpgradeRoomCapacity()

        room.devices.forEachIndexed { index, device ->
            run {
                manageRoom_linearLayout_devicesList.addView(Button(this).also {
                    it.text = device.getName()
                    it.setOnClickListener { openDeviceSettings(index) }
                })
            }
        }

        addButtonToBuyDevice()


        /*TODO
            przycisk do dodawania nowych urządzeń
            Powinien wyświetlać kolejny ekran z wyborem urządzenia, jego parametrów jakości i
             energooszczędności oraz ceny
        */
    }

    private fun openDeviceSettings(index: Int) {
        val newDeviceIntent = Intent(this, Game_ManageDevice::class.java)
        newDeviceIntent.putExtra("roomIndex", intent.extras!!.getInt("roomIndex"))
        newDeviceIntent.putExtra("deviceIndex", index)
        startActivity(newDeviceIntent)
    }

    private fun addButtonToBuyDevice() {
        manageRoom_linearLayout_devicesList.addView(Button(this).also {
            it.text = "+ Dodaj urządzenie"
            it.setOnClickListener { p0 -> p0?.let { openNewItemModal(p0) } }
        })
    }

    private fun addButtonToUpgradeRoomCapacity() {
        val roomCapacity = room.getRoomCapacity()
        val upgradeCost = 10
        manageRoom_linearLayout_devicesList.addView(Button(this).also {
            it.text = String.format(
                "+ Dodaj miejsce [%d->%d] ($%d)",
                roomCapacity,
                roomCapacity + 1,
                upgradeCost
            )
            it.setOnClickListener {
                room.increaseCapacity()
                wallet.pay(upgradeCost)
                refresh()
            }
        })
    }

    private fun openNewItemModal(v: View) {

    }
}