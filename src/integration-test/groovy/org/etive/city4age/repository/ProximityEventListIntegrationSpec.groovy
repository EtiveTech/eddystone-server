package org.etive.city4age.repository

import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Stepwise
@Transactional
class ProximityEventListIntegrationSpec extends Specification {
    def proximityEventService

    @Shared
    def list
    @Shared
    ProximityEventList eventList
    @Shared
    ProximityEvent event

    def setup() {
    }

    def cleanup() {
    }

    void "List initialised"() {
        given:
            list = proximityEventService.forProcessing("eventlist1@test.org", "2017-06-20")
        when:
            eventList = new ProximityEventList(list)
        then:
            list.size() > 0
            eventList.size() == list.size()
            eventList.getIndex() == -1
    }

    void "List is reversed"() {
        given:
            // state from the previous test
        when:
            event = eventList.nextLost()
        then:
            event.id == list.last().id
            event.eventType == "lost"
            event.beacon.beaconId == "c4a000002762"
            eventList.getIndex() == 0
    }

    void "Gets distant found event"() {
        given:
            // state from the previous test
            def id = event.id
        when:
            event = eventList.nextFound(event.beacon)
        then:
            event.eventType == "found"
            event.beacon.beaconId == "c4a000002762"
            event.id != id
            eventList.getIndex() == 0
    }

    void "Gets adjacent lost event"() {
        given:
            // state from the previous test
        when:
            event = eventList.nextLost()
        then:
            event.eventType == "lost"
            event.beacon.beaconId == "c4a000002759"
            eventList.getIndex() == 1
    }

    void "Gets adjacent found event"() {
        given:
            // state from the previous test
            def id = event.id
        when:
            event = eventList.nextFound(event.beacon)
        then:
            event.eventType == "found"
            event.beacon.beaconId == "c4a000002759"
            event.id != id
            eventList.getIndex() == 1
    }

    void "Gets distant lost event"() {
        given:
            // state from the previous test
        when:
            event = eventList.nextLost()
        then:
            event.eventType == "lost"
            event.beacon.beaconId == "c4a000002746"
            eventList.getIndex() == 4
    }
}
