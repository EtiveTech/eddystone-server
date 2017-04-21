package city4age.api

import grails.converters.*

class BeaconController {

    def index() {
        respond Beacon.list()
    }
}

