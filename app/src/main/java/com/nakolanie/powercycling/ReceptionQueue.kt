package com.nakolanie.powercycling

import kotlin.properties.ReadWriteProperty

class ReceptionQueue<T> (
    queueSizeDelegate: ReadWriteProperty<Any?, Int>
) {
    private var queueSize: Int by queueSizeDelegate
    private val receptionQueue: MutableList<T> = mutableListOf()

    init {
        queueSize = receptionQueue.size
    }

    fun addToQueue(element: T) {
        receptionQueue.add(element)
        queueSize = receptionQueue.size
    }

    fun removeFromQueue(index: Int) {
        if(receptionQueue.size <= index) {
            return
        }
        receptionQueue.removeAt(index)
        queueSize = receptionQueue.size
    }

    fun removeFromQueue(element: T) {
        if (!receptionQueue.contains(element)) {
            return
        }
        removeFromQueue(receptionQueue.indexOf(element))
    }
}