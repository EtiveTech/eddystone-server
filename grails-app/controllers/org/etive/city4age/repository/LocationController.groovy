package org.etive.city4age.repository

class LocationController {
    def locationService

    def index() {
        respond locationService.listLocations()
    }

    def save() {
        def remoteAddr = request.getRemoteAddr()
        if (remoteAddr == request.getLocalAddr()) {
            def location = locationService.createLocation(request.JSON)

            if (location)
                respond (location, [ status: 201 ])
            else
                response.sendError(409, "Location may exist already")
        }
        else {
            response.sendError(403, "Forbidden address: " + remoteAddr)
        }
    }
}
