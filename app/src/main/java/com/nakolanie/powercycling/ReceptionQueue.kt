package com.nakolanie.powercycling

import java.util.function.Consumer
import kotlin.properties.ReadWriteProperty

class ReceptionQueue(
    queueSizeDelegate: ReadWriteProperty<Any?, Int>
) {
    private var queueSize: Int by queueSizeDelegate
    private val receptionQueue: MutableList<QueueBundle> = mutableListOf()

    init {
        queueSize = receptionQueue.size
    }

    fun addToQueue(element: QueueBundle) {
        receptionQueue.add(element)
        queueSize = receptionQueue.size
    }

    fun removeFromQueue(index: Int) {
        if (receptionQueue.size <= index) {
            return
        }
        receptionQueue.removeAt(index)
        queueSize = receptionQueue.size
    }

    fun removeFromQueue(element: QueueBundle) {
        if (!receptionQueue.contains(element)) {
            return
        }
        removeFromQueue(receptionQueue.indexOf(element))
    }

    fun removeFromQueue(elementList: List<QueueBundle>) {
        elementList.forEach { removeFromQueue(it) }
    }

    fun removePatienceAndReturnEntriesWithoutPatience(): MutableList<QueueBundle> {
        val markedToRemove: MutableList<QueueBundle> = mutableListOf()
        for (bundle in receptionQueue){
            bundle.lowerWaitingPatience()
            if (bundle.bundleLostPatience()){
                markedToRemove.add(bundle)
            }
        }
        return markedToRemove
    }
}