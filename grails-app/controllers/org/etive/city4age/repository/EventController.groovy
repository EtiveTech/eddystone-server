package org.etive.city4age.repository

class EventController {
    def eventService

    def index() {
        respond Event.list()
    }

    def save() {
        def event = eventService.createEvent(request.JSON)
        if (event)
            respond(event, status: 201)
        else
            response.sendError(409, "")
    }
}
