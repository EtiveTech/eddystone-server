package org.etive.city4age.repository

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
                user: careReceiver.city4AgeId,
                pilot: "BHX",
                interval_start: startDate.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("Europe/London")),
                interval_end: endDate.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("Europe/London")),
                payload: [
                        SLEEP_LIGHT_TIME: [
                                value: lightSleepDuration,
                                ddata_source_type: [ "external_dataset" ]
                        ],
                        SLEEP_DEEP_TIME: [
                                value: deepSleepDuration,
                                data_source_type: [ "external_dataset" ]
                        ],
                        SLEEP_WAKEUP_NUM: [
                                value: wakeupCount,
                                data_source_type: [ "external_dataset" ]
                        ],
                        SLEEP_AWAKE_TIME: [
                                value: wakeupDuration,
                                data_source_type: [ "external_dataset" ]
                        ],
                        SLEEP_TOSLEEP_TIME: [
                                value: durationToSleep,
                                data_source_type: [ "external_dataset" ]
                        ],
                ]
        ]
    }
}
