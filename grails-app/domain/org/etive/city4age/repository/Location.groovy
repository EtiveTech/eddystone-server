package org.etive.city4age.repository

class Location {

    String name
    String type
    Location container = null // Some locations will be inside others
//    position (includes lat and long)
    String latitude
    String longitude
    Date dateCreated
    Date lastUpdated

    static hasMany = [beacons: Beacon, poiEvents: PoiEvent]

    static constraints = {
        name blank: false, nullable: false
        type nullable: false
        latitude blank: false, nullable: false
        longitude blank: false, nullable: false
        container nullable: true
    }
}
