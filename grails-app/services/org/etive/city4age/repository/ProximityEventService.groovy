package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class ProximityEventService {
    def deviceService

    def createProximityEvent(json) {
        def careReceiver = CareReceiver.findByToken(json.token as String)
        if (!careReceiver) return null // throw 403

        // Note that we have heard from the device
        def device = deviceService.updateLastContact(json)

        def beacon = Beacon.findByBeaconId(json.beaconId.toString())
        if (!beacon) return null //throw 409

        def event = new ProximityEvent(
                eventType: json.eventType,
                timestamp: new Date(json.timestamp as Long),
                rssi: json.rssi as Integer,
                parameter: ((json.eventType == "found") ? json.txPower : json.rssiMax) as Integer,
                beacon: beacon,
                careReceiver: careReceiver,
                device: device
        )
        event.save()
        return event
    }

    def persistChanges(proximityEvent) {
        return proximityEvent.save()
    }

    @Transactional(readOnly = true)
    def listProximityEvents() {
        return ProximityEvent.list()
    }

    @Transactional(readOnly = true)
    def forCareReceiver(CareReceiver receiver, Date date) {
        def copyDate = new Date(date.getTime())
        copyDate.clearTime()
        def earliest = copyDate.getTime()
        def latest = (copyDate + 1).getTime()

        def query = ProximityEvent.where{ careReceiver.id == receiver.id && timestamp >= earliest && timestamp < latest }
        return query.listOrderByTimestamp(order: "desc")
    }
}
