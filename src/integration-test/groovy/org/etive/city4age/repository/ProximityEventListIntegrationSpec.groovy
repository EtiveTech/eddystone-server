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

    void "Handles drop-outs (1)"() {
        given:
            def proximityList = proximityEventService.forProcessing("eventlist3@test.org", "2017-10-22")
        when:
            eventList = new ProximityEventList(proximityList)
        then:
            eventList.size() == 14

            eventList.getEntry(0).datetime() == "2017-10-22T14:01:51Z"
            eventList.getEntry(1).datetime() == "2017-10-22T13:54:19Z"
            eventList.getEntry(2).datetime() == "2017-10-22T13:48:27Z"
            eventList.getEntry(3).datetime() == "2017-10-22T13:48:18Z"
            eventList.getEntry(4).datetime() == "2017-10-22T13:45:22Z"
            eventList.getEntry(5).datetime() == "2017-10-22T13:44:38Z"
            eventList.getEntry(6).datetime() == "2017-10-22T13:42:32Z"
            eventList.getEntry(7).datetime() == "2017-10-22T13:40:35Z"
            eventList.getEntry(8).datetime() == "2017-10-22T13:35:55Z"
            eventList.getEntry(9).datetime() == "2017-10-22T13:32:19Z"
            eventList.getEntry(10).datetime() == "2017-10-22T13:31:12Z"
            eventList.getEntry(11).datetime() == "2017-10-22T13:26:56Z"
            eventList.getEntry(12).datetime() == "2017-10-22T13:25:46Z"
            eventList.getEntry(13).datetime() == "2017-10-22T13:17:45Z"
    }

    void "Handles drop-outs (2)"() {
        given:
            def proximityList = proximityEventService.forProcessing("eventlist5@test.org", "2017-10-28")
        when:
            eventList = new ProximityEventList(proximityList)
        then:
            eventList.size() == 10

            eventList.getEntry(0).datetime() == "2017-10-28T14:43:15Z"
            eventList.getEntry(1).datetime() == "2017-10-28T14:42:56Z"
            eventList.getEntry(2).datetime() == "2017-10-28T14:42:24Z"
            eventList.getEntry(3).datetime() == "2017-10-28T14:39:39Z"
            eventList.getEntry(4).datetime() == "2017-10-28T14:39:10Z"
            eventList.getEntry(5).datetime() == "2017-10-28T14:36:31Z"
            eventList.getEntry(6).datetime() == "2017-10-28T14:16:19Z"
            eventList.getEntry(7).datetime() == "2017-10-28T14:13:09Z"
            eventList.getEntry(8).datetime() == "2017-10-28T14:12:38Z"
            eventList.getEntry(9).datetime() == "2017-10-28T14:10:54Z"
    }
}
