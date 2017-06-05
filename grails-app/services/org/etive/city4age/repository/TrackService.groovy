package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class TrackService {

    def createTrack(CareReceiver receiver, json) {
        if (json._type == "location") {
            // This is an OwnTracks record
            def track = new Track(
                    latitude: json.lat as Double,
                    longitude: json.lon as Double,
                    accuracy: Math.round(json.acc as Double),
                    battery: json.batt as Integer,
                    timestamp: new Date(json.tst.toLong() * 1000),
                    careReceiver: receiver
            )
            return track.save()
        }
    }

    @Transactional(readOnly = true)
    def listTracks(CareReceiver receiver) {

        def query = (receiver) ? Track.where{ careReceiver.id == receiver.id } : Track
        return query.list(max: 500)
    }
}