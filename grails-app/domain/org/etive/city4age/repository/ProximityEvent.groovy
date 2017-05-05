package org.etive.city4age.repository

class ProximityEvent {

    String eventType
    Date timestamp
    Integer rssi
    Integer parameter
    Date dateCreated
    Date lastUpdated

    static belongsTo = [beacon: Beacon, careReceiver: CareReceiver, device: Device, poiEvent: PoiEvent]

    static constraints = {
        eventType blank: false, nullable: false
        timestamp nullable: false
        rssi nullable: false
        parameter nullable: false
        beacon nullable: false
        careReceiver nullable: false
        device nullable: false
        poiEvent nullable: true
    }

    static Date getTimestamp(List<ProximityEvent> sourceEvents) {
        def timestamp = null
        for (sourceEvent in sourceEvents) {
            if (!timestamp || (sourceEvent.timestamp.getTime() < timestamp.getTime())) timestamp = sourceEvent.timestamp
        }
        return timestamp
    }
}
