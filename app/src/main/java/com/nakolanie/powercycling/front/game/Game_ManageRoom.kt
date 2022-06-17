package com.nakolanie.powercycling.front.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.Room
import kotlinx.android.synthetic.main.activity_game_manage_room.*

class Game_ManageRoom : AppCompatActivity() {
    private lateinit var room: Room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_manage_room)
        val roomIndex: Int = intent.extras!!.getInt("roomIndex")

        room = GameActivity.GET_CONTEXT().rooms[roomIndex]

        manageRoom_textView_title.setText(room.name)

        addDevicesToScrollView()
    }

    fun onBack(v: View) {
        this.finish()
    }

    private fun addDevicesToScrollView() {
        manageRoom_linearLayout_devicesList.removeAllViews()
        room.devices.forEachIndexed { index, device ->
            run {
                val button = Button(this)
                button.setText(device.name)
                button.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        p0?.let { openDeviceSettings(index) }
                    }

                })
                manageRoom_linearLayout_devicesList.addView(button)
            }
        }

        //TODO przycisk do dodawania nowych urządzeń
    }

    private fun openDeviceSettings(index: Int) {
        val newDeviceIntent = Intent(this, Game_ManageDevice::class.java)
        newDeviceIntent.putExtra("roomIndex", intent.extras!!.getInt("roomIndex"))
        newDeviceIntent.putExtra("deviceIndex", index)
        startActivity(newDeviceIntent)
    }
}