package org.etive.city4age.repository

class Location {

    String name
    String type

    static hasMany = [beacons: Beacon]

    static constraints = {
        name blank: false, nullable: false
        type nullable: false
    }
}
