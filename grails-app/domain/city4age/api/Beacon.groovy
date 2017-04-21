package city4age.api

class Beacon {

    String beaconId

    static hasMany = [events: Event]
    static belongsTo = [location: Location]

    static constraints = {
        beaconId blank: false, nullable: false
        events nullable: true
    }
}