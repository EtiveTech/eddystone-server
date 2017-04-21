package city4age.api

class Event {

    String eventType
    Date timestamp
    Integer rssi
    Integer parameter

    static belongsTo = [beacon: Beacon, careReceiver: CareReceiver, device: Device]

    static constraints = {
        eventType blank: false, nullable: false
        timestamp nullable: false
        rssi nullable: false
        parameter nullable: false
        beacon nullable: false
        careReceiver nullable: false
        device nullable: false
    }
}
