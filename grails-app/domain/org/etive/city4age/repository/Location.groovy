package org.etive.city4age.repository

class Location {

    String name
    String type
    Location container = null // Some locations will be inside others
//    position (includes lat and long)

    static hasMany = [beacons: Beacon, poiEvents: PoiEvent]

    static constraints = {
        name blank: false, nullable: false
        type nullable: false
        container nullable: true
    }
}
