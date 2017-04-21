package city4age.api

import grails.converters.JSON

class LocationController {

    def index() {
        respond Location.list()
    }
}
