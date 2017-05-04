package org.etive.city4age.repository

class ProximityEventController {
    def proximityEventService

    def index() {
        def receiver = CareReceiver.findById(params.receiverId)
        def list = proximityEventService.listProximityEvents(receiver)
        respond(list, status: 200)
    }

    def save() {
        def json = request.JSON
        def careReceiver = CareReceiver.findByToken(json.token.toString())
        if (careReceiver) {
            def beacon = Beacon.findByBeaconId(json.beaconId.toString())
            if (beacon) {
                def event = proximityEventService.createProximityEvent(careReceiver, beacon, request.JSON)
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
