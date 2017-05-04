package org.etive.city4age.repository

class BootStrap {

    def init = { servletContext ->
        def location = new Location(name: "Chemist", type: "chemist", latitude: "1.111", longitude: "-2.222").save(failOnError: true)
        new Beacon(beaconId: "c4a000000001", location: location).save(failOnError: true)
        new Beacon(beaconId: "c4a00000275c", location: location).save(failOnError: true)
    }
    def destroy = {
    }
}