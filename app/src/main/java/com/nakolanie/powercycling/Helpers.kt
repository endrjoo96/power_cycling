package com.nakolanie.powercycling

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class Helpers {
    companion object {
        fun setImageResourceFromBitmap(activityCallback: AppCompatActivity, imageView: ImageView, resourceName: String){
            val resourceId = activityCallback.resources.getIdentifier(resourceName, "mipmap", activityCallback.packageName)
            imageView.setImageResource(resourceId)
        }

        fun Float.roundToDecimal(n: Int): Float{
            val exponent = Math.pow(10.toDouble(), n.toDouble());
            return (this * exponent).roundToInt() / exponent.toFloat()
        }

        fun Float.roundToDecimalAsString(n: Int): String{
            return String.format("%.${n}f", this)
        }
    }


}