package org.etive.city4age.repository

class Device {
    String operatingSystem
    String osVersion
    String model
    String uniqueId
    Date lastContact
    Date dateCreated
    Date lastUpdated

    static belongsTo = [careReceiver: CareReceiver]
    static hasMany = [proximityEvents: ProximityEvent]

    static constraints = {
        operatingSystem blank: false, nullable: false
        osVersion blank: false, nullable: false
        model blank: false, nullable: false
        uniqueId blank: false, nullable: false
        careReceiver nullable: false
        lastContact nullable: false
    }

    static Long getId(device) {
        if (!device) return null
        return device.id
    }
}


