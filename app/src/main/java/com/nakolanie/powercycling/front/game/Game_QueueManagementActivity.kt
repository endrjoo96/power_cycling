package com.nakolanie.powercycling.front.game

import android.os.Bundle
import android.view.View
import com.nakolanie.powercycling.R
import com.nakolanie.powercycling.extensions.GameAppCompatActivity
import kotlinx.android.synthetic.main.activity_game_queue_management.*

class Game_QueueManagementActivity : GameAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_queue_management)

        addQueueToView()
    }

    private fun addQueueToView() {
        queueManagement_linearLayout_bundleList.removeAllViews()
        queueManagement_linearLayout_roomsList.removeAllViews()
    }

    fun onBundleClick(v: View) {
        queueManagement_linearLayout_roomsList.removeAllViews()
    }
}