package com.nakolanie.powercycling.front.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.models.Room
import com.nakolanie.powercycling.context.GameContext
import com.nakolanie.powercycling.extensions.GameAppCompatActivity
import kotlinx.android.synthetic.main.activity_game_rooms_management.*
import kotlin.math.pow


class Game_RoomsManagementActivity : GameAppCompatActivity() {
    private val rooms: MutableList<Room> = GameContext.get().rooms
    private var newRoomCost: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_rooms_management)

        calculateNewRoomCost()
        addRoomsToScrollView()
    }

    private fun calculateNewRoomCost() {
        newRoomCost = if (rooms.size == 1) 150
        else {
            (35 * (rooms.size - 1).toDouble().pow(2.toDouble()) + 200).toInt()
        }
    }
    // 150, 235, 340

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
        button.setText("+ nowy pokój ($${newRoomCost})")
        button.isEnabled = GameContext.get().wallet.isEnough(newRoomCost)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                p0?.let { onNewRoom() }
            }

        })
        roomManagement_linearLayout_roomsList.addView(button)
    }

    fun openRoomSettings(index: Int) {
        val newRoomIntent = Intent(this, Game_ManageRoom::class.java)
        newRoomIntent.putExtra("roomIndex", index)
        startActivity(newRoomIntent)
    }

    fun onNewRoom() {
        if (GameContext.get().wallet.isEnough(newRoomCost)) {
            rooms.add(Room())
            GameContext.get().wallet.pay(newRoomCost)
            calculateNewRoomCost()
            addRoomsToScrollView()
        }
    }
}