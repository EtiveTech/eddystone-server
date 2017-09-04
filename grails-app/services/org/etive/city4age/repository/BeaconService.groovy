package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class BeaconService {

    @Transactional(readOnly = true)
    def listBeacons() {
        return Beacon.list()
    }

    def createBeacon(json) {
        def location = Location.findByLocationId(json.locationId as String)
        if (!location) return null

        def beacon = new Beacon(
                beaconId: json.beaconId as String,
                description: json.description as String,
                location: location
        )
        try {
            beacon = beacon.save()
        }
        catch (Exception e) {
            log.error(e.message)
            beacon = null
        }

        return beacon
    }
}
