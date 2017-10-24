package org.etive.city4age.repository

import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Stepwise
@Transactional
class BeaconPairIntegrationSpec extends Specification {
    def proximityEventService

    @Shared
    def eventList
    @Shared
    def beaconPairList

    def setup() {
    }

    def cleanup() {
    }

    void "BeaconPair List initialised"() {
        given:
            eventList = proximityEventService.forProcessing("eventlist1@test.org", "2017-06-20")
            def list = new ProximityEventList(eventList)
            eventList = eventList.reverse()
        when:
            beaconPairList = BeaconPair.findBeaconPairs(list, true)
        then:
            beaconPairList.size() == 8
    }

    void "BeaconPair initialised"() {
        given:
            // Current state
            def firstPair = beaconPairList[0]
        expect:
            firstPair.getFoundEvent() == eventList[3]
            firstPair.getLostEvent() == eventList[0]
    }

    void "BeaconPair is not noise"() {
        given:
            // Current state
            def firstPair = beaconPairList[0]
        expect:
            firstPair.isNoise() == false
    }

    void "BeaconPair is walkby"() {
        given:
            // Current state
            def firstPair = beaconPairList[0]
        expect:
            firstPair.isWalkBy() == true
    }

    void "BeaconPair is visit"() {
        given:
            // Current state
            def firstPair = beaconPairList[0]
        expect:
            firstPair.isVisit() == false
    }

    void "Generates correct SourceEvents" () {
        given:
            // Current state
            def firstPair = beaconPairList[0]
            def sourceEvents = firstPair.getSourceEvents()
        expect:
            sourceEvents[0] == firstPair.getFoundEvent()
            sourceEvents[1] == firstPair.getLostEvent()
    }

    void "Another Pair at same Location"() {
        given:
            // Current state
            def firstPair = beaconPairList[0]
        when:
            def anotherPair = beaconPairList[1]
        then:
            anotherPair.getFoundEvent() == eventList[2]
            anotherPair.getLostEvent() == eventList[1]
            firstPair.sameLocation(anotherPair) == true
            firstPair.containedBy(anotherPair) == false
    }

    void "Location not contained by another"() {
        given:
            // Current state
            def firstPair = beaconPairList[0]
        when:
            def anotherPair = beaconPairList[1]
        then:
            firstPair.containedBy(anotherPair) == false
    }

    void "Another Pair inside First Pair"() {
        given:
            // Current state
            def firstPair = beaconPairList[0]
        when:
            def anotherPair = beaconPairList[1]
        then:
            firstPair.overlaps(anotherPair) == true
            anotherPair.overlaps(firstPair) == true
    }

    void "Another Pair overlaps First Pair"() {
        given:
            // Current state
            def firstPair = beaconPairList[2]
        when:
            def anotherPair = beaconPairList[3]
        then:
            firstPair.getFoundEvent() == eventList[6]
            firstPair.getLostEvent() == eventList[4]
            anotherPair.getFoundEvent() == eventList[7]
            anotherPair.getLostEvent() == eventList[5]
            firstPair.sameLocation(anotherPair) == true
            firstPair.overlaps(anotherPair) == true
            anotherPair.overlaps(firstPair) == true
    }

    void "Pairs do not overlap"() {
        given:
            // Current state
            def firstPair = beaconPairList[6]
        when:
            def anotherPair = beaconPairList[7]
        then:
            firstPair.getFoundEvent() == eventList[13]
            firstPair.getLostEvent() == eventList[12]
            anotherPair.getFoundEvent() == eventList[15]
            anotherPair.getLostEvent() == eventList[14]
            firstPair.sameLocation(anotherPair) == false
            firstPair.overlaps(anotherPair) == false
            anotherPair.overlaps(firstPair) == false
    }

    void "Location contained by another"() {
        given:
            // Current state
            def firstPair = beaconPairList[6]
        when:
            def anotherPair = beaconPairList[7]
        then:
            firstPair.containedBy(anotherPair) == true
    }

    void "BeaconPair List initialised (overlaps removed)"() {
        given:
            def list = new ProximityEventList(eventList.reverse())
        when:
            beaconPairList = BeaconPair.findBeaconPairs(list)
        then:
            beaconPairList.size() == 6
            beaconPairList[0].getLostEvent() == eventList[0]
            beaconPairList[1].getLostEvent() == eventList[4]
            beaconPairList[2].getLostEvent() == eventList[8]
            beaconPairList[3].getLostEvent() == eventList[9]
            beaconPairList[4].getLostEvent() == eventList[12]
            beaconPairList[5].getLostEvent() == eventList[14]
    }

    void "Handles overlapping beacon pairs"() {
        given:
            def proximityList = proximityEventService.forProcessing("eventlist4@test.org", "2017-10-22")
            def eventList = new ProximityEventList(proximityList)
        when:
            beaconPairList = BeaconPair.findBeaconPairs(eventList)
        then:
            beaconPairList.size() == 1

            beaconPairList[0].getFoundEvent().beacon.beaconId == "c4a000002771"
            beaconPairList[0].getFoundEvent().datetime() == "2017-10-22T14:06:38Z"
            beaconPairList[0].getFoundEvent() == proximityList[0]
            beaconPairList[0].getLostEvent() == proximityList[2]
    }

}
