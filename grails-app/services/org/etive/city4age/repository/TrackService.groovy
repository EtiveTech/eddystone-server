package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class TrackService {

    private static Boolean isLocation(json) {
        return (json._type == "location")
    }

    private static Boolean isStationary(json) {
        return (json._type == "stationary")
    }

    def createTrack(CareReceiver receiver, json) {
        if (isLocation(json) || isStationary(json)) {
            def track = new Track(
                    latitude: json.lat as Double,
                    longitude: json.lon as Double,
                    accuracy: Math.round(json.acc as Double),
                    timestamp: new Date((json.tst * 1000) as Long),
                    timeAtLocation: ((json.time) ? json.time : 0) as Long,
                    triggeredBy: json.t as Character,
                    careReceiver: receiver,
                    device: null
            )

            if (isStationary(json)) {
                // This event was generated from an Android device
                track.device = Device.findByUniqueId(json.uuid)
            }

            return track.save()
        }
    }

    @Transactional(readOnly = true)
    def listTracks(CareReceiver receiver) {
        def query = (receiver) ? Track.where{ careReceiver.id == receiver.id } : Track
        return query.list(offset: 0, max: 500, sort: "id", order: "desc")
    }

    @Transactional(readOnly = true)
    def forProcessing(CareReceiver receiver, Date date) {
        def early = (new Date(date.getTime())).clearTime()
        def late = early + 1

        def query = Track.where{
            careReceiver.id == receiver.id &&
                    timestamp >= early &&
                    timestamp < late
        }
        return query.list()
    }
}