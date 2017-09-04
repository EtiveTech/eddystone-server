package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class LocationService {

    @Transactional(readOnly = true)
    def listLocations() {
        return Location.list()
    }

    def createLocation(json) {
        def container = (json.containedBy) ? Location.findByLocationId(json.containedBy as String) : null
        if (json.containedBy && !container) return null

        def location = new Location(
                locationId: json.locationId as String,
                name: json.name as String,
                type: json.type as String,
                containedBy: container,
                address: json.address as String,
                latitude: json.latitude as Double,
                longitude: json.longitude as Double,
                radius: json.radius as Integer,
                regionId: json.regionId as String
        )
        try {
            location = location.save()
            Region.addLocation(location)
        }
        catch (Exception e) {
            log.error(e.message)
            location = null
        }

        return location
    }
}
