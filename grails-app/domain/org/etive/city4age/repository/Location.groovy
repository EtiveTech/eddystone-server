package org.etive.city4age.repository

class Location {
    String locationId
    String name
    String type
    Location container = null // Some locations will be inside others
    String address
    String latitude
    String longitude
    Date dateCreated
    Date lastUpdated

    static hasMany = [beacons: Beacon, poiEvents: PoiEvent]

    static constraints = {
        locationId blank: false, nullable: false
        name blank: false, nullable: false
        type nullable: false
        address blank: true, nullable: true
        latitude blank: false, nullable: false
        longitude blank: false, nullable: false
        container nullable: true
    }
}
