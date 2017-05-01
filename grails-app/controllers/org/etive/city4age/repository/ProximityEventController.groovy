package org.etive.city4age.repository

class ProximityEventController {
    def eventService

    def index() {
        respond ProximityEvent.list()
    }

    def save() {
        def event = eventService.createEvent(request.JSON)
        if (event)
            respond(event, status: 201)
        else
            response.sendError(409, "")
    }
}
