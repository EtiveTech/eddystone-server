package org.etive.city4age.repository

class Location {

    String name
    String type
//    position (includes lat and long)

    static hasMany = [beacons: Beacon, poiEvents: PoiEvent]

    static constraints = {
        name blank: false, nullable: false
        type nullable: false
    }
}
