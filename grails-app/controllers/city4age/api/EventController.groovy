package city4age.api

class EventController {

    def index() {
        respond Event.list()
    }

    def save() {
        def json = request.JSON
        def careReceiver = CareReceiver.findByToken(json.token)
        if (careReceiver) {
            // Note that we have heard from the device
            def device = Device.findByUniqueID(json.uuid)
            device.lastContact = new Date(Long.valueOf(json.timestamp))
            device.save()

            def beacon = Beacon.findByBeaconId(json.beaconId)
            def event = new Event(
                    eventType: json.eventType,
                    timestamp: new Date(Long.valueOf(json.timestamp)),
                    rssi: json.rssi,
                    parameter: (json.eventType == "found") ? json.txPower : json.rssiMax,
                    beacon: beacon,
                    careReceiver: careReceiver,
                    device: device
            ).save()
            respond(event, status: 201)
        }
        else {
            response.sendError(403, "")
        }
    }
}
