package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class TrackService {

    def createTrack(CareReceiver receiver, json) {
        if (json._type == "location") {
            // This is an OwnTracks record
            def track = new Track(
                    accuracy: json.acc as Double,
                    latitude: json.lat as Double,
                    longitude: json.long as Double,
                    timestamp: new Date(json.tst.toLong() * 1000)
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