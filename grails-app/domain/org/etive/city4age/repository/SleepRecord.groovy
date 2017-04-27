package org.etive.city4age.repository

class SleepRecord {
    Date date
    Integer wakeupDuration
    Integer wakeupCount
    Integer lightSleepDuration
    Integer deepSleepDuration
    Integer durationToSleep

    static belongsTo = [careReceiver: CareReceiver]

    static constraints = {
        date nullable: false
        wakeupDuration nullable: false
        wakeupCount nullable: false
        lightSleepDuration nullable: false
        deepSleepDuration nullable: false
        durationToSleep nullable: false
    }
}
