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

    static belongsTo = [careReceiver: CareReceiver]

    static constraints = {
        date blank: false, nullable: false
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
