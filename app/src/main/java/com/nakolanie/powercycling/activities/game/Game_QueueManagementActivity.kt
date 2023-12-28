package com.nakolanie.powercycling.activities.game

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.context.GameContext
import com.nakolanie.powercycling.extensions.GameAppCompatActivity
import com.nakolanie.powercycling.models.QueueBundle
import com.nakolanie.powercycling.models.Room
import com.nakolanie.powercycling.services.QueueService
import kotlinx.android.synthetic.main.activity_game_queue_management.*

class Game_QueueManagementActivity : GameAppCompatActivity() {
    private val queueService: QueueService = GameContext.get().queueService
    private val rooms: MutableList<Room> = GameContext.get().rooms
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_queue_management)
        inflater =
            applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        refresh()
    }

    private fun refresh() {
        queueManagement_linearLayout_bundleList.removeAllViews()
        queueManagement_linearLayout_roomsList.removeAllViews()

        queueService.getQueue().forEachIndexed { index, bundle ->
            run {
                val view =
                    inflater.inflate(R.layout.queuebundle_button_control_queue_management, null)

                val bundleSize =
                    view.findViewById<TextView>(R.id.queueBundleControl_textView_bundleSize)
                val bundlePatienceTextView =
                    view.findViewById<TextView>(R.id.queueBundleControl_textView_bundlePatience)
                val bundlePatienceProgressBar =
                    view.findViewById<ProgressBar>(R.id.queueBundleControl_progressBar_bundlePatience)
                val button =
                    view.findViewById<Button>(R.id.queueBundleControl_button)
                val requiredDevicesLinearLayout =
                    view.findViewById<LinearLayout>(R.id.queueBundleControl_linearLayout_requiredDevicesTextViewList)

                bundleSize.text = bundle.bundleSize.toString()
                bundlePatienceProgressBar.progress = bundle.getPatience()
                bundlePatienceTextView.text = (bundle.getPatience() / 10).toString()

                bundle.minimalDevicesRequirements.forEachIndexed { index, device ->
                    val textView = TextView(this@Game_QueueManagementActivity)
                    textView.text = device.toString()
                    requiredDevicesLinearLayout.addView(textView, index)
                }

                queueManagement_linearLayout_bundleList.addView(view, index)

                button.setOnClickListener {
                    for (bundleViewIdx in 0 until queueManagement_linearLayout_bundleList.childCount) {
                        queueManagement_linearLayout_bundleList
                            .getChildAt(bundleViewIdx)
                            .findViewById<Button>(R.id.queueBundleControl_button)
                            .alpha = 0f
                    }
                    button.alpha = 0.1f
                    onBundleClick(it, bundle)
                }
            }
        }
    }

    fun onBundleClick(v: View, bundle: QueueBundle) {
        queueManagement_linearLayout_roomsList.removeAllViews()
        rooms.forEachIndexed { index, room ->
            val roomView =
                inflater.inflate(R.layout.room_button_control_queue_management, null)

            val roomName =
                roomView.findViewById<TextView>(R.id.roomControl_textView_roomName)
            val roomOccupied =
                roomView.findViewById<TextView>(R.id.roomControl_textView_occupied)
            val roomDevices =
                roomView.findViewById<LinearLayout>(R.id.roomControl_linearLayout_roomDevices)
            val roomButton =
                roomView.findViewById<Button>(R.id.roomControl_button)

            roomName.text = String.format("%s (dla %d osób)", room.name, room.getRoomCapacity())
            room.devices.forEachIndexed { index, device ->
                val textView = TextView(this@Game_QueueManagementActivity)
                textView.text = device.toString()
                roomDevices.addView(textView, index)
            }
            roomButton.setOnClickListener {
                room.bookRoom(bundle)
                queueService.removeFromQueue(bundle)
                if (queueService.getQueue().isEmpty()){
                    onBack(it)
                }
                refresh()
            }

            roomButton.isEnabled =
                if (room.isBookable(bundle)) {
                    roomButton.setBackgroundColor(COLOR_GREEN)
                    true
                } else {
                    if (room.isOccupied()) {
                        roomOccupied.visibility = View.VISIBLE
                    }
                    roomButton.setBackgroundColor(COLOR_RED)
                    false
                }
            queueManagement_linearLayout_roomsList.addView(roomView, index)
        }
    }

    private val COLOR_RED = Color.parseColor("#ff7676")
    private val COLOR_GREEN = Color.parseColor("#76ff76")
}