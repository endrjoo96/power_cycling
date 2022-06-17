package com.nakolanie.powercycling

import kotlin.random.Random

class Room(val name: String) {
    constructor() : this("Room ${getNextInSequence()}")
    init {

    }

    val devices: MutableList<Device> = mutableListOf()
    private var peopleCurrently = 0
    private var peopleBooked = 0
    private var roomCapacity = 1

    fun getCurrentEnergyDemand(): Float {
        var powerConsumption = 0f
        letPeopleMoveInOrOutOfRoom()
        letPeopleManageDevices()

        println("--- Devices in current usage:")
        devices.forEach {
            powerConsumption += it.getCurrentPowerConsumption()
            if (it.enabled) println("------- ${it.name}")
        }

        return powerConsumption
    }

    private fun letPeopleMoveInOrOutOfRoom() {
        //each person in room have 5% of leaving
        var delta = 0
        for (person in 1..peopleCurrently) {
            if (Random.nextFloat() <= 0.50) {
                println("--- Someone left room")
                delta--
            }
        }

        //also, each person missing have 5% for returning
        for (person in 1..(peopleBooked - peopleCurrently)) {
            if (Random.nextFloat() <= 0.50) {
                println("--- Someone returned to room")
                delta++
            }
        }
        peopleCurrently += delta
        println("--- Current room state: (${peopleCurrently}/${peopleBooked})")
    }


    private fun letPeopleManageDevices() {

        //each person have 20% chance of enabling each device
        for (i in 1..peopleCurrently) {
            devices.forEach {
                if (Random.nextFloat() <= 0.20) it.enabled = true;
            }
        }

        //each device has 25% chance of being disabled by someone
        devices.forEach {
            if (Random.nextFloat() <= 0.25) it.enabled = false;
        }
    }

    fun upgradeMaxPeopleCount() {
        upgradeMaxPeopleCount(1)
    }

    fun upgradeMaxPeopleCount(by: Int) {
        roomCapacity += by
    }

    fun bookRoom(numberOfPeople: Int) {
        if (isBookable(numberOfPeople)) {
            peopleBooked = numberOfPeople
            peopleCurrently = numberOfPeople
        }
    }

    fun isBookable(numberOfPeople: Int): Boolean {
        return peopleBooked == 0
                && peopleCurrently == 0
                && numberOfPeople <= roomCapacity
    }

    fun unbookRoom() {
        peopleBooked = 0
        peopleCurrently = 0

    }

    fun insertDevice(device: Device) {
        devices.add(device)
    }

    companion object {
        private var ROOM_SEQUENCE = 1
        private fun getNextInSequence(): Int {
            return ROOM_SEQUENCE++
        }
        fun resetRoomSequence() {
            ROOM_SEQUENCE = 1
        }
    }
}