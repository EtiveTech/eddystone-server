package org.etive.city4age.repository

class BeaconController {

    def index() {
        respond Beacon.list()
    }
}

