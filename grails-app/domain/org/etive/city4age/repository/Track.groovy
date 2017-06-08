package org.etive.city4age.repository

class Track {
    Double latitude
    Double longitude
    Integer accuracy
    Date timestamp
    Long timeAtLocation
    Character triggeredBy
    Date dateCreated
    Date lastUpdated

    static belongsTo = [careReceiver: CareReceiver, device: Device]

    static constraints = {
        latitude nullable: false
        longitude nullable: false
        accuracy nullable: false
        timestamp nullable: false
        timeAtLocation nullable: false
        triggeredBy nullable: false
        device nullable: true
    }
}