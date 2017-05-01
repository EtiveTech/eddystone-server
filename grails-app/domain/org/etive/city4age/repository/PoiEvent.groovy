package org.etive.city4age.repository

class PoiEvent {
    String action

    static hasMany = [proximityEvents: ProximityEvent]
    static belongsTo = [careReceiver: CareReceiver, location: Location]

    static constraints = {
        action nullable: false, blank: false
        proximityEvents nullable: false
        careReceiver nullable: false
        location nullable: false
    }
}
