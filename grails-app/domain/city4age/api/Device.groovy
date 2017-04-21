package city4age.api

class Device {
    String operatingSystem
    String osVersion
    String model
    String uniqueID
    Date lastContact

    static belongsTo = [careReceiver: CareReceiver]

    static constraints = {
        operatingSystem blank: false, nullable: false
        osVersion blank: false, nullable: false
        model blank: false, nullable: false
        uniqueID blank: false, nullable: false
        careReceiver nullable: false
        lastContact nullable: false
    }
}


