package org.etive.city4age.repository

import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Stepwise
@Transactional
class PoiEventIntegrationSpec extends Specification {
    static String email = "eventlist1@test.org"

    def proximityEventService

    @Shared
    def proximityList
    @Shared
    List<BeaconPair> beaconPairs
    @Shared
    List<PoiEvent> poiList

    def setup() {
    }

    def cleanup() {
    }

    void "Initialise Beacon Pairs"() {
        given:
            proximityList = proximityEventService.forProcessing(email, "2017-06-20")
            def eventList = new ProximityEventList(proximityList)
            beaconPairs = BeaconPair.findBeaconPairs(eventList)
        expect:
            beaconPairs.size() == 6

            // Sainsbury's Mere Green
            beaconPairs[0].getFoundEvent() == proximityList[14]
            beaconPairs[0].getLostEvent() == proximityList[17]
            !beaconPairs[0].isVisit()
            beaconPairs[0].getLocation().locationId == "SainsburysMereGreen"

            // Boots Mere Green
            beaconPairs[1].getFoundEvent() == proximityList[11]
            beaconPairs[1].getLostEvent() == proximityList[13]
            !beaconPairs[1].isVisit()
            beaconPairs[1].getLocation().locationId == "BootsMereGreen"

            // Mere Green Community Centre
            beaconPairs[2].getFoundEvent() == proximityList[7]
            beaconPairs[2].getLostEvent() == proximityList[9]
            !beaconPairs[2].isVisit()
            beaconPairs[2].getLocation().locationId == "MereGreenCommunityCentre"

            // Mere Green Library
            beaconPairs[3].getFoundEvent() == proximityList[6]
            beaconPairs[3].getLostEvent() == proximityList[8]
            beaconPairs[3].isVisit()
            beaconPairs[3].getLocation().locationId == "MereGreenLibrary"

            // Mere Green Library
            beaconPairs[4].getFoundEvent() == proximityList[4]
            beaconPairs[4].getLostEvent() == proximityList[5]
            !beaconPairs[4].isVisit()
            beaconPairs[4].getLocation().locationId == "MereGreenLibrary"

            // Mere Green Community Centre
            beaconPairs[5].getFoundEvent() == proximityList[2]
            beaconPairs[5].getLostEvent() == proximityList[3]
            beaconPairs[5].isVisit()
            beaconPairs[5].getLocation().locationId == "MereGreenCommunityCentre"
    }

    void "Right events from full list"() {
        given:
            // Current state
            poiList = PoiEvent.findEvents(email, beaconPairs)
        expect:
            poiList.size() == 4

            poiList[0].action == "POI_ENTER"
            poiList[0].location.locationId == "MereGreenCommunityCentre"
            poiList[0].beaconPair == beaconPairs[5]
            poiList[0].sourceEvents[0] == proximityList[2]
            poiList[0].sourceEvents[1] == proximityList[3]

            poiList[1].action == "POI_ENTER"
            poiList[1].location.locationId == "MereGreenLibrary"
            poiList[1].beaconPair == beaconPairs[4]
            poiList[1].sourceEvents[0] == proximityList[4]
            poiList[1].sourceEvents[1] == proximityList[5]

            poiList[2].action == "POI_EXIT"
            poiList[2].location.locationId == "MereGreenLibrary"
            poiList[2].beaconPair == beaconPairs[3]
            poiList[2].sourceEvents[0] == proximityList[6]
            poiList[2].sourceEvents[1] == proximityList[8]

            poiList[3].action == "POI_EXIT"
            poiList[3].location.locationId == "MereGreenCommunityCentre"
            poiList[3].beaconPair == beaconPairs[2]
            poiList[3].sourceEvents[0] == proximityList[7]
            poiList[3].sourceEvents[1] == proximityList[9]
    }

    void "Right events from visit"() {
        given:
            // Current state
            poiList = PoiEvent.findEvents(email, beaconPairs[4..5])
        expect:
            poiList.size() == 2

            poiList[0].action == "POI_ENTER"
            poiList[0].location.locationId == "MereGreenCommunityCentre"
            poiList[0].beaconPair == beaconPairs[5]
            poiList[0].sourceEvents[0] == proximityList[2]

            poiList[1].action == "POI_EXIT"
            poiList[1].location.locationId == "MereGreenCommunityCentre"
            poiList[1].beaconPair == beaconPairs[5]
            poiList[1].sourceEvents[0] == proximityList[3]
    }
}
