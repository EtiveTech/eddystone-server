package city4age.api

class Location {

    String name
    String type

    static hasMany = [beacons: Beacon]

    static constraints = {
        name blank: false, nullable: false
        type nullable: false
    }
}
