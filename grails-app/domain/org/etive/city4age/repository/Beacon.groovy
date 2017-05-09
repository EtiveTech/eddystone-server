package org.etive.city4age.repository

class Beacon {

    String beaconId
    String description
    Date dateCreated
    Date lastUpdated

    static hasMany = [events: ProximityEvent]
    static belongsTo = [location: Location]

    static constraints = {
        beaconId blank: false, nullable: false
        description blank: false, nullable: false
        events nullable: true
    }
}