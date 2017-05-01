package org.etive.city4age.repository

class SleepRecord {
    String date
    Integer wakeupDuration
    Integer wakeupCount
    Integer lightSleepDuration
    Integer deepSleepDuration
    Integer durationToSleep
    Boolean uploaded = false

    static belongsTo = [careReceiver: CareReceiver]

    static constraints = {
        date blank: false, nullable: false
        wakeupDuration nullable: false
        wakeupCount nullable: false
        lightSleepDuration nullable: false
        deepSleepDuration nullable: false
        durationToSleep nullable: false
        uploaded nullable: false
    }
}
