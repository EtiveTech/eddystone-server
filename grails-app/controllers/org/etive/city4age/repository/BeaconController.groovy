package org.etive.city4age.repository

class BeaconController {
    def beaconService

    def index() {
        respond beaconService.listBeacons()
    }

    def save() {
        def remoteAddr = request.getRemoteAddr()
        if (remoteAddr == request.getLocalAddr()) {
            def beacon = beaconService.createBeacon(request.JSON)

            if (beacon)
                respond (beacon, [ status: 201 ])
            else
                response.sendError(409, "Beacon may exist already")
        }
        else {
            response.sendError(403, "Forbidden address: " + remoteAddr)
        }
    }
}

