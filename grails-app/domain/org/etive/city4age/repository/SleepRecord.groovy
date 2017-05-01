package org.etive.city4age.repository

import javafx.scene.effect.Light

class SleepRecord {
    String date
    Date startDate
    Date endDate
    Integer wakeupDuration
    Integer wakeupCount
    Integer lightSleepDuration
    Integer deepSleepDuration
    Integer durationToSleep
    Boolean uploaded = false

    static belongsTo = [careReceiver: CareReceiver]

    static constraints = {
        date blank: false, nullable: false, unique: 'careReceiver'
        startDate nullable: false
        endDate nullable: false
        wakeupDuration nullable: false
        wakeupCount nullable: false
        lightSleepDuration nullable: false
        deepSleepDuration nullable: false
        durationToSleep nullable: false
        uploaded nullable: false
        careReceiver nullable: false
    }

    def forUpload() {
        return [
                user: careReceiver.city4ageId,
                interval_start: startDate.format("yyyy-MM-dd hh:mm"),
                interval_end: endDate.format("yyyy-MM-dd hh:mm"),
                payload: [
                        SLEEP_LIGHT_TIME: lightSleepDuration,
                        SLEEP_DEEP_TIME: deepSleepDuration,
                        SLEEP_WAKEUP_NUM: wakeupCount,
                        SLEEP_AWAKE_TIME: wakeupDuration,
                        SLEEP_TOSLEEP_TIME: durationToSleep
                ],
                extra: [
                        pilot: "birmingham",
                        dataSourceType: [ "external_dataset" ]
                ]
        ]
    }
}
