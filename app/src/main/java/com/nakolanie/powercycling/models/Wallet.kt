package com.nakolanie.powercycling.models

import com.nakolanie.powercycling.utils.MathUtils.Companion.roundToDecimal
import kotlin.properties.ReadWriteProperty

class Wallet(walletAmountDelegate: ReadWriteProperty<Any?, Float>){
    private var cash: Float by walletAmountDelegate

    fun put(amount: Number): Wallet {
        cash += amount.toFloat().roundToDecimal(2)
        return this
    }

    fun pay(amount: Number): Wallet {
        if (isEnough(amount)){
            cash -= amount.toFloat().roundToDecimal(2)
        }
        return this
    }

    fun check(): Float {
        return cash.roundToDecimal(2)
    }

    fun isEnough(amount: Number): Boolean {
        return amount.toFloat().roundToDecimal(2) <= cash
    }

}