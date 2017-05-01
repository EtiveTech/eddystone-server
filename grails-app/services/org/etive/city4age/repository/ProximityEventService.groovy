package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class ProximityEventService {
    def deviceService

    def createEvent(json) {
        def careReceiver = CareReceiver.findByToken(json.token.toString())
        if (!careReceiver) return null // throw 403

        // Note that we have heard from the device
        def device = deviceService.updateLastContact(json.uuid.toString(), Long.valueOf(json.timestamp.toString()))

        def beacon = Beacon.findByBeaconId(json.beaconId.toString())
        if (!beacon) return null //throw 409

        def event = new ProximityEvent(
                eventType: json.eventType,
                timestamp: new Date(Long.valueOf(json.timestamp.toString())),
                rssi: json.rssi,
                parameter: (json.eventType == "found") ? json.txPower : json.rssiMax,
                beacon: beacon,
                careReceiver: careReceiver,
                device: device
        )
        event.save()
        return event
    }
}
