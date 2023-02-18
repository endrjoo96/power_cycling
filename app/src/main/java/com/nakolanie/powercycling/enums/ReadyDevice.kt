package com.nakolanie.powercycling.enums

import com.nakolanie.powercycling.models.device.*

enum class ReadyDevice(val get: Device) {
    MICROWAVE(MicrowaveDevice()),
    KETTLE(KettleDevice()),
    VACUUM_CLEANER(VacuumCleanerDevice()),
    TV(TvDevice()),
    DESKTOP_PC(DesktopPcDevice()),
    LED_LIGHT(LightBulbDevice()),

}