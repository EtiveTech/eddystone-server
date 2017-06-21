package org.etive.city4age.repository

class ActivityRecord {
    String date
    Float calories
    Float totalCalories
    Float distance
    Integer soft
    Integer moderate
    Integer intense
    Integer steps
    Boolean uploaded = false
    Date dateCreated
    Date lastUpdated

    static belongsTo = [careReceiver: CareReceiver]

    static constraints = {
        date blank: false, nullable: false, unique: 'careReceiver'
        calories nullable: false
        totalCalories nullable: false
        distance nullable: false
        soft nullable: false
        moderate nullable: false
        intense nullable: false
        steps nullable: false
        uploaded nullable: false
        careReceiver nullable: false
    }

    def formatForUpload() {
        Date intervalStart = Date.parse("yyyy-MM-dd", date)
        return [
                user: careReceiver.city4AgeId,
                pilot: "BHX",
                interval_start: intervalStart.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("Europe/London")),
                duration: "DAY",
                payload: [
                        WALK_STEPS: [
                                value: steps,
                                data_source_type: [ "external_dataset" ]
                        ],
                        WALK_DISTANCE: [
                                value: distance,
                                data_source_type: [ "external_dataset" ]
                        ],
                        PHYSICALACTIVITY_SOFT_TIME: [
                                value: soft,
                                data_source_type: [ "external_dataset" ]
                        ],
                        PHYSICALACTIVITY_MODERATE_TIME: [
                                value: moderate,
                                data_source_type: [ "external_dataset" ]
                        ],
                        PHYSICALACTIVITY_INTENSE_TIME: [
                                value: intense,
                                data_source_type: [ "external_dataset" ]
                        ],
                        PHYSICALACTIVITY_CALORIES: [
                                value: totalCalories,
                                data_source_type: [ "external_dataset" ]
                        ]
                ]
        ]
    }
}
