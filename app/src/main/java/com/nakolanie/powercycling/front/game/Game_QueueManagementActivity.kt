package com.nakolanie.powercycling.front.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.nakolanie.powercycling.models.QueueBundle
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.models.Room
import com.nakolanie.powercycling.context.GameContext
import com.nakolanie.powercycling.extensions.GameAppCompatActivity
import kotlinx.android.synthetic.main.activity_game_queue_management.*

class Game_QueueManagementActivity : GameAppCompatActivity() {
    private val queue: List<QueueBundle> = GameContext.get().queueService.getQueue()
    private val rooms: MutableList<Room> = GameContext.get().rooms
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_queue_management)
        inflater =
            applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        addQueueToView()
    }

    private fun addQueueToView() {
        queueManagement_linearLayout_bundleList.removeAllViews()
        queueManagement_linearLayout_roomsList.removeAllViews()

        queue.forEachIndexed { index, bundle ->
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
                bundlePatienceTextView.text = (bundle.getPatience()/10).toString()

                bundle.minimalDevicesRequirements.forEachIndexed {index, device ->
                    val textView = TextView(this@Game_QueueManagementActivity)
                    textView.text = String.format("%s (%s)", device.name, device.quality.friendlyDescription)
                    requiredDevicesLinearLayout.addView(textView, index)
                }

                queueManagement_linearLayout_bundleList.addView(view, index)
            }
        }
    }

    fun onBundleClick(v: View) {
        queueManagement_linearLayout_roomsList.removeAllViews()
    }
}