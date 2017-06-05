package org.etive.city4age.repository

class TrackController {
    def trackService

    def save() {
        def json = request.JSON
        def receiver = (params.receiverId) ? CareReceiver.findById(params.receiverId) : CareReceiver.findByToken(json.token as String)

        if (receiver) {
            def track = trackService.createTrack(receiver, json)
            respond(track, status: 201)
        }
        else {
            response.sendError(403, "")
        }
    }
	
    def index() {
        def receiver = CareReceiver.findById(params.receiverId)
        def list = trackService.listTracks(receiver)
        respond(list, status: 200)
    }
}
