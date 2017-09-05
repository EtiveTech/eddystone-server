package org.etive.city4age.repository

class ProximityEventController {
    def proximityEventService
    def deviceService

    def index() {
        def receiver = CareReceiver.findById(params.receiverId)
        def list = proximityEventService.listProximityEvents(receiver)
        respond(list, status: 200)
    }

    def save() {
        def json = request.JSON
        def careReceiver = CareReceiver.findByToken(json.token as String)
        if (careReceiver) {
            def beacon = Beacon.findByBeaconId(json.beaconId as String)
            def device = Device.findByUniqueId(json.uuid as String)
            if (beacon && device && device.careReceiver == careReceiver) {
                def event = proximityEventService.createProximityEvent(careReceiver, beacon, device, json)
                // Note that we have heard from the device
                deviceService.updateLastContact(careReceiver, device, json)
                respond(event, status: 201)
            }
            else {
                response.sendError(409, "")
            }
        }
        else {
            response.sendError(403, "")
        }
    }
}
