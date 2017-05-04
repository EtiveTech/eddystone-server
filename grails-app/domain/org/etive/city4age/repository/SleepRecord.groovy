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
    Date dateCreated
    Date lastUpdated

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

    def formatForUpload() {
        return [
                user: careReceiver.city4ageId,
                pilot: "BHX",
                interval_start: startDate.format("yyyy-MM-dd hh:mm"),
                interval_end: endDate.format("yyyy-MM-dd hh:mm"),
                payload: [
                        SLEEP_LIGHT_TIME: [
                                value: lightSleepDuration,
                                dataSourceType: [ "external_dataset" ]
                        ],
                        SLEEP_DEEP_TIME: [
                                value: deepSleepDuration,
                                dataSourceType: [ "external_dataset" ]
                        ],
                        SLEEP_WAKEUP_NUM: [
                                value: wakeupCount,
                                dataSourceType: [ "external_dataset" ]
                        ],
                        SLEEP_AWAKE_TIME: [
                                value: wakeupDuration,
                                dataSourceType: [ "external_dataset" ]
                        ],
                        SLEEP_TOSLEEP_TIME: [
                                value: durationToSleep,
                                dataSourceType: [ "external_dataset" ]
                        ],
                ]
        ]
    }
}
