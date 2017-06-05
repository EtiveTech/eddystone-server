package org.etive.city4age.repository

class Track {
    Double accuracy
    Double latitude
    Double longitude
    Date timestamp
    Date dateCreated
    Date lastUpdated

    static belongsTo = [careReceiver: CareReceiver]

    static constraints = {
        accuracy nullable: false
        latitude nullable: false
        longitude nullable: false
        timestamp nullable: false
    }
}
