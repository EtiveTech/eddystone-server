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
}
