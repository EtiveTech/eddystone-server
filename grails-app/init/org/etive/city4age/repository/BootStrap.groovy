package org.etive.city4age.repository

class BootStrap {

    def init = { servletContext ->
        def shoppingCentre = new Location(name: "Harlequin Shopping Centre", type: "shoppingCentre", latitude: "3.333", longitude: "-4.444").save(failOnError: true)
        def chemist = new Location(name: "Chemist", type: "chemist",  container: shoppingCentre, latitude: "1.111", longitude: "-2.222").save(failOnError: true)
        new Beacon(beaconId: "c4a00000275c", location: chemist).save(failOnError: true)
        new Beacon(beaconId: "c4a00000274a", location: shoppingCentre).save(failOnError: true)
    }
    def destroy = {
    }
}