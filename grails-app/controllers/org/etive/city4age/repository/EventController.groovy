package org.etive.city4age.repository

class EventController {

    def index() {
        respond Event.list()
    }

    def save() {
        def json = request.JSON
        def careReceiver = CareReceiver.findByToken(json.token.toString())
        if (careReceiver) {
            // Note that we have heard from the device
            def device = Device.findByUniqueId(json.uuid.toString())
            if (device) {
                device.lastContact = new Date(Long.valueOf(json.timestamp.toString()))
                device.save(flush: true)
            }

            def beacon = Beacon.findByBeaconId(json.beaconId.toString())
            if (beacon) {
                def event = new Event(
                        eventType: json.eventType,
                        timestamp: new Date(Long.valueOf(json.timestamp.toString())),
                        rssi: json.rssi,
                        parameter: (json.eventType == "found") ? json.txPower : json.rssiMax,
                        beacon: beacon,
                        careReceiver: careReceiver,
                        device: device
                )
                event.save()
                respond(event, status: 201)
            }
            else {
                // The beacon looks like a City4Age beacon but it isn't in the database
                response.sendError(409, "")
            }
        }
        else {
            response.sendError(403, "")
        }
    }
}
