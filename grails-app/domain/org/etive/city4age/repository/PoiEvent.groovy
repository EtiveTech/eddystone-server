package org.etive.city4age.repository

class PoiEvent {
    String action
    Boolean uploaded = false

    static hasMany = [proximityEvents: ProximityEvent]
    static belongsTo = [careReceiver: CareReceiver, location: Location]

    static constraints = {
        action nullable: false, blank: false
        careReceiver nullable: false
        location nullable: false
        uploaded nullable: false
    }
}
