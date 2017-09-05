package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class ProximityEventService {
    def deviceService

    def createProximityEvent(CareReceiver careReceiver, Beacon beacon, Device device, json) {

        def event = new ProximityEvent(
                eventType: json.eventType,
                timestamp: new Date(json.timestamp as Long),
                rssi: json.rssi as Integer,
                parameter: ((json.eventType == "found") ? json.txPower : json.rssiMax) as Integer,
                beacon: beacon,
                careReceiver: careReceiver,
                device: device
        )
        try {
            event = event.save()
        }
        catch (Exception e) {
            log.error(e.message)
            event = null
        }
        return event
    }

    def createProximityEvent(CareReceiver careReceiver, json) {
        def beacon = Beacon.findByBeaconId(json.beaconId.toString())
        def device = Device.findByUniqueId(json.uuid.toString())
        return createProximityEvent(careReceiver, beacon, device, json)
    }

    def persistChanges(proximityEvent) {
        return proximityEvent.save()
    }

    @Transactional(readOnly = true)
    def listProximityEvents(CareReceiver receiver) {
        def query = (receiver) ? ProximityEvent.where{ careReceiver.id == receiver.id } : ProximityEvent
        return query.list(offset: 0, max: 500, sort: "id", order: "desc")
    }

    @Transactional(readOnly = true)
    def forProcessing(CareReceiver receiver, Date date) {
        def early = (new Date(date.getTime())).clearTime()
        def late = early + 1

        // Query ignores any links to poiEvents as it simplifies testing
        def query = ProximityEvent.where{
            careReceiver.id == receiver.id &&
                    timestamp >= early &&
                    timestamp < late
        }
        return query.list(sort: "timestamp", order: "asc")
    }

    @Transactional(readOnly = true)
    def forProcessing(String receiverEmail, String strDate) {
        def careReceiver = CareReceiver.findByEmailAddress(receiverEmail)
        def date = new Date().parse("yyyy-MM-dd", strDate)
        if (!careReceiver || !date) return null
        return forProcessing(careReceiver, date)
    }

    @Transactional(readOnly = true)
    def firstProximityEvent(CareReceiver receiver) {
        def query = ProximityEvent.where{ careReceiver.id == receiver.id }
        def first = query.list(offset: 0, max: 1, sort: "timestamp", order: "asc")
        return (first) ? first[0] : null
    }
}
