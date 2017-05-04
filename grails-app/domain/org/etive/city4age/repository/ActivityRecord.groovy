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
        return [
                user: careReceiver.city4ageId,
                pilot: "BHX",
                interval_start: date + " 00:00",
                duration: "DAY",
                payload: [
                        WALK_STEPS: [
                                value: steps,
                                dataSourceType: [ "external_dataset" ]
                        ],
                        WALK_DISTANCE: [
                                value: distance,
                                dataSourceType: [ "external_dataset" ]
                        ],
                        PHYSICALACTIVITY_SOFT_TIME: [
                                value: soft,
                                dataSourceType: [ "external_dataset" ]
                        ],
                        PHYSICALACTIVITY_MODERATE_TIME: [
                                value: moderate,
                                dataSourceType: [ "external_dataset" ]
                        ],
                        PHYSICALACTIVITY_INTENSE_TIME: [
                                value: intense,
                                dataSourceType: [ "external_dataset" ]
                        ],
                        PHYSICALACTIVITY_CALORIES: [
                                value: totalCalories,
                                dataSourceType: [ "external_dataset" ]
                        ],
                ]
        ]
    }
}
