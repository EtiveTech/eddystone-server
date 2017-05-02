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
                interval_start: date + " 00:00",
                interval_end: (Date.parse("yyyy-MM-dd", date) + 1).format("yyyy-MM-dd") + " 00:00",
                payload: [
                        WALK_STEPS: steps,
                        WALK_DISTANCE: distance,
                        PHYSICALACTIVITY_SOFT_TIME: soft,
                        PHYSICALACTIVITY_MODERATE_TIME: moderate,
                        PHYSICALACTIVITY_INTENSE_TIME: intense,
                        PHYSICALACTIVITY_CALORIES: totalCalories
                ],
                extra: [
                        pilot: "birmingham",
                        dataSourceType: [ "external_dataset" ]
                ]
        ]
    }
}
