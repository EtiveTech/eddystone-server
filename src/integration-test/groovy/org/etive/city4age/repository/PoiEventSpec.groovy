package org.etive.city4age.repository

import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Stepwise
@Transactional
class PoiEventSpec extends Specification {
    def proximityEventService

    @Shared
    List<PoiEvent> list

    def setup() {
    }

    def cleanup() {
    }

    void "Initialise"() {
        given:
            String email = "eventlist1@test.org"
            ProximityEventList eventList = proximityEventService.forProcessing(email, "2017-06-20")
            list = PoiEvent.findEvents(email, eventList)
        expect:
            list.size() == 4
    }
}
