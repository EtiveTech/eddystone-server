package org.etive.city4age.repository

import javax.servlet.http.HttpServletRequest

class TrackController {
    def trackService

    def save() {
        def json = request.JSON
        def receiver = null
        if (request.getAuthType() == HttpServletRequest.BASIC_AUTH) {
            // A request from an OwnTracks client
            def credentials = request.getHeader("Authorization")?.trim()?.split(" ")[1]
            def username = credentials.decodeBase64().toString().split(":")[0]
            receiver = CareReceiver.findByEmailAddress(username)
        }
        else {
            // The ability to use a db id to post a track event is crazy - will be removed
            receiver = (json.token) ? CareReceiver.findByToken(json.token as String) : CareReceiver.findById(params.receiverId)
        }

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
