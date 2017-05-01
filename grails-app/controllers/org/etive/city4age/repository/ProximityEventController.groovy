package org.etive.city4age.repository

class ProximityEventController {
    def proximityEventService

    def index() {
        respond proximityEventService.listProximityEvents()
    }

    def save() {
        def event = proximityEventService.createProximityEvent(request.JSON)
        if (event)
            respond(event, status: 201)
        else
            response.sendError(409, "")
    }
}
