package org.etive.city4age.repository

class LocationController {

    def index() {
        respond Location.list()
    }
}
