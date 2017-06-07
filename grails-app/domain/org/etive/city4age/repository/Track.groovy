package org.etive.city4age.repository

class Track {
    Integer accuracy
    Double latitude
    Double longitude
    Date timestamp
    Integer battery
    Long timeAtLocation
    Character trigger
    Date dateCreated
    Date lastUpdated

    static belongsTo = [careReceiver: CareReceiver, device: Device]

    static constraints = {
        accuracy nullable: false
        latitude nullable: false
        longitude nullable: false
        battery nullable: false
        timestamp nullable: false
        timeAtLocation nullable: false
        trigger nullable: false
        device nullable: true
    }
}
