package org.etive.city4age.repository

class Location {
    String locationId
    String name
    String type
    Location containedBy = null // Some locations will be inside others
    String address
    Double latitude
    Double longitude
    Integer radius
    String regionId
    Date dateCreated
    Date lastUpdated

    static hasMany = [beacons: Beacon, poiEvents: PoiEvent]

    static constraints = {
        locationId blank: false, nullable: false, unique: true
        name blank: false, nullable: false
        type blank: false, nullable: false
        address blank: true, nullable: true
        latitude nullable: false
        longitude nullable: false
        radius nullable: true
        regionId blank: false, nullable: true
        containedBy nullable: true
    }
}
