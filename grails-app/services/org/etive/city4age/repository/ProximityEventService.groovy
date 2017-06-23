package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class ProximityEventService {
    def deviceService

    def createProximityEvent(CareReceiver careReceiver, Beacon beacon, json) {
        // Note that we have heard from the device
        def device = deviceService.updateLastContact(json)
        def event = new ProximityEvent(
                eventType: json.eventType,
                timestamp: new Date(json.timestamp as Long),
                rssi: json.rssi as Integer,
                parameter: ((json.eventType == "found") ? json.txPower : json.rssiMax) as Integer,
                beacon: beacon,
                careReceiver: careReceiver,
                device: device
        )
        return event.save()
    }

    def createProximityEvent(CareReceiver careReceiver, json) {
        def beacon = Beacon.findByBeaconId(json.beaconId.toString())
        return createProximityEvent(careReceiver, beacon, json)
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

        def query = ProximityEvent.where{
            careReceiver.id == receiver.id &&
                    timestamp >= early &&
                    timestamp < late &&
                    poiEvent == null
        }
        return query.list(sort: "timestamp", order: "asc")
    }

    @Transactional(readOnly = true)
    def firstProximityEvent(CareReceiver receiver) {
        def query = ProximityEvent.where{ careReceiver.id == receiver.id }
        def first = query.list(offset: 0, max: 1, sort: "timestamp", order: "asc")
        return (first) ? first[0] : null
    }
}
