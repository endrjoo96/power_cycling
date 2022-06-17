package com.nakolanie.powercycling.front.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.nakolanie.powercycling.Device
import com.nakolanie.powercycling.EfficiencyClass
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.Room
import kotlinx.android.synthetic.main.activity_game__rooms_management.*


class Game_RoomsManagementActivity : AppCompatActivity() {
    private val rooms: MutableList<Room> = GameActivity.GET_CONTEXT().rooms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game__rooms_management)

        addRoomsToScrollView()
    }

    private fun addRoomsToScrollView() {
        roomManagement_linearLayout_roomsList.removeAllViews()
        rooms.forEachIndexed { index, room ->
            run {
                val button = Button(this)
                button.setText(room.name)
                button.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        p0?.let { openRoomSettings(index) }
                    }

                })
                roomManagement_linearLayout_roomsList.addView(button)
            }
        }
        val button = Button(this)
        button.setText("+ nowy pokój")
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                p0?.let { onNewRoom() }
            }

        })
        roomManagement_linearLayout_roomsList.addView(button)
    }

    fun onBack(v: View) {
        setResult(100)
        this.finish()
    }

    fun openRoomSettings(index: Int) {
        val newRoomIntent = Intent(this, Game_ManageRoom::class.java)
        newRoomIntent.putExtra("roomIndex", index)
        startActivity(newRoomIntent)
    }

    fun onNewRoom() {
        rooms.add(Room().apply {
            this.insertDevice(Device("Microwave", EfficiencyClass.D, 0.9f))
            this.insertDevice(Device("Electric kettle", EfficiencyClass.A, 1f))
            this.insertDevice(Device("TV", EfficiencyClass.B, 0.2f))
            this.insertDevice(Device("Desktop computer", EfficiencyClass.C, 0.5f))
            this.insertDevice(Device("LED light bulb", EfficiencyClass.Ap, 0.005f))
            this.upgradeMaxPeopleCount(2)
            this.bookRoom(3)
        })
        addRoomsToScrollView()
    }
}