package org.etive.city4age.repository

class Beacon {

    String beaconId

    static hasMany = [events: Event]
    static belongsTo = [location: Location]

    static constraints = {
        beaconId blank: false, nullable: false
        events nullable: true
    }
}