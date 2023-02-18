package com.nakolanie.powercycling.utils

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

abstract class ResourcesUtils {
    companion object {
        fun setImageResourceFromBitmap(
            activityCallback: AppCompatActivity,
            imageView: ImageView,
            resourceName: String
        ) {
            val resourceId = activityCallback.resources.getIdentifier(
                resourceName,
                "mipmap",
                activityCallback.packageName
            )
            imageView.setImageResource(resourceId)
        }
    }
}