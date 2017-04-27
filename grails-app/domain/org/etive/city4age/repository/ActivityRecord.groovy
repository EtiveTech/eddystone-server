package org.etive.city4age.repository

class ActivityRecord {
    Date date
    Float calories
    Float totalCalories
    Float distance
    Integer soft
    Integer moderate
    Integer intense
    Integer steps

    static belongsTo = [careReceiver: CareReceiver]

    static constraints = {
        date nullable: false
        calories nullable: false
        totalCalories nullable: false
        distance nullable: false
        soft nullable: false
        moderate nullable: false
        intense nullable: false
        steps nullable: false
        careReceiver nullable: false
    }
}
