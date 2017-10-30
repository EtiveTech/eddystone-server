package org.etive.city4age.repository

import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Stepwise
@Transactional
class PoiEventIntegrationSpec extends Specification {
    static String email1 = "eventlist1@test.org"
    static String email2 = "eventlist2@test.org"

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
            proximityList = proximityEventService.forProcessing(email1, "2017-06-20")
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
            poiList = PoiEvent.findEvents(email1, beaconPairs)
        expect:
            poiList.size() == 4

            poiList[0].action == "POI_ENTER"
            poiList[0].location.locationId == "MereGreenCommunityCentre"
            poiList[0].beaconPair == beaconPairs[5]
            poiList[0].sourceEvents[0] == proximityList[2]
            poiList[0].sourceEvents[1] == proximityList[3]
            poiList[0].rating.toString() == "0.8"

            poiList[1].action == "POI_ENTER"
            poiList[1].location.locationId == "MereGreenLibrary"
            poiList[1].beaconPair == beaconPairs[4]
            poiList[1].sourceEvents[0] == proximityList[4]
            poiList[1].sourceEvents[1] == proximityList[5]
            poiList[0].rating.toString() == "0.8"

            poiList[2].action == "POI_EXIT"
            poiList[2].location.locationId == "MereGreenLibrary"
            poiList[2].beaconPair == beaconPairs[3]
            poiList[2].sourceEvents[0] == proximityList[6]
            poiList[2].sourceEvents[1] == proximityList[8]
            poiList[0].rating.toString() == "0.8"

            poiList[3].action == "POI_EXIT"
            poiList[3].location.locationId == "MereGreenCommunityCentre"
            poiList[3].beaconPair == beaconPairs[2]
            poiList[3].sourceEvents[0] == proximityList[7]
            poiList[3].sourceEvents[1] == proximityList[9]
            poiList[0].rating.toString() == "0.8"
    }

    void "Right events from visit"() {
        given:
            // Current state
            poiList = PoiEvent.findEvents(email1, beaconPairs[4..5])
        expect:
            poiList.size() == 2

            poiList[0].action == "POI_ENTER"
            poiList[0].location.locationId == "MereGreenCommunityCentre"
            poiList[0].beaconPair == beaconPairs[5]
            poiList[0].sourceEvents[0] == proximityList[2]
            poiList[0].rating.toString() == "0.9"

            poiList[1].action == "POI_EXIT"
            poiList[1].location.locationId == "MereGreenCommunityCentre"
            poiList[1].beaconPair == beaconPairs[5]
            poiList[1].sourceEvents[0] == proximityList[3]
            poiList[1].rating.toString() == "0.9"
    }

    void "Orders badly ordered list"() {
        given:
            proximityList = proximityEventService.forProcessing(email2, "2017-06-21")
        expect:
            proximityList.size() == 24
            proximityList[0].datetime() == "2017-06-21T08:55:45Z"
            proximityList[1].datetime() == "2017-06-21T08:56:36Z"
            proximityList[2].datetime() == "2017-06-21T08:59:52Z"
            proximityList[3].datetime() == "2017-06-21T09:00:03Z"
            proximityList[4].datetime() == "2017-06-21T09:01:02Z"
            proximityList[5].datetime() == "2017-06-21T09:01:06Z"
            proximityList[6].datetime() == "2017-06-21T09:01:30Z"
            proximityList[7].datetime() == "2017-06-21T09:01:51Z"
            proximityList[8].datetime() == "2017-06-21T09:02:42Z"
            proximityList[9].datetime() == "2017-06-21T09:02:47Z"
            proximityList[10].datetime() == "2017-06-21T09:03:01Z"
            proximityList[11].datetime() == "2017-06-21T09:03:09Z"
            proximityList[12].datetime() == "2017-06-21T09:03:37Z"
            proximityList[13].datetime() == "2017-06-21T09:03:39Z"
            proximityList[14].datetime() == "2017-06-21T09:20:06Z"
            proximityList[15].datetime() == "2017-06-21T09:20:26Z"
            proximityList[16].datetime() == "2017-06-21T12:34:24Z"
            proximityList[17].datetime() == "2017-06-21T12:35:22Z"
            proximityList[18].datetime() == "2017-06-21T13:13:53Z"
            proximityList[19].datetime() == "2017-06-21T13:16:42Z"
            proximityList[20].datetime() == "2017-06-21T14:39:11Z"
            proximityList[21].datetime() == "2017-06-21T14:39:27Z"
            proximityList[22].datetime() == "2017-06-21T14:39:31Z"
            proximityList[23].datetime() == "2017-06-21T14:39:48Z"
    }

    void "Handles badly ordered list"() {
        given:
            proximityList = proximityEventService.forProcessing(email2, "2017-06-21")
            def eventList = new ProximityEventList(proximityList)
        when:
            beaconPairs = BeaconPair.findBeaconPairs(eventList)
        then:
            beaconPairs.size() == 8

            // Mere Green Community Centre
            beaconPairs[0].getFoundEvent() == proximityList[21]
            beaconPairs[0].getLostEvent() == proximityList[23]
            !beaconPairs[0].isVisit()
            beaconPairs[0].getLocation().locationId == "MereGreenCommunityCentre"

            // Mere Green Library
            beaconPairs[1].getFoundEvent() == proximityList[20]
            beaconPairs[1].getLostEvent() == proximityList[22]
            !beaconPairs[1].isVisit()
            beaconPairs[1].getLocation().locationId == "MereGreenLibrary"

            // Mere Green Community Centre
            beaconPairs[2].getFoundEvent() == proximityList[18]
            beaconPairs[2].getLostEvent() == proximityList[19]
            beaconPairs[2].isVisit()
            beaconPairs[2].getLocation().locationId == "MereGreenCommunityCentre"

            // Mere Green Community Centre
            beaconPairs[3].getFoundEvent() == proximityList[16]
            beaconPairs[3].getLostEvent() == proximityList[17]
            !beaconPairs[3].isVisit()
            beaconPairs[3].getLocation().locationId == "MereGreenCommunityCentre"

            // Mere Green Community Centre
            beaconPairs[4].getFoundEvent() == proximityList[14]
            beaconPairs[4].getLostEvent() == proximityList[15]
            !beaconPairs[4].isVisit()
            beaconPairs[4].getLocation().locationId == "MereGreenCommunityCentre"

            // Sainsburys Mere Green
            beaconPairs[5].getFoundEvent() == proximityList[4] // 7
            beaconPairs[5].getLostEvent() == proximityList[13]
            beaconPairs[5].isVisit()
            beaconPairs[5].getLocation().locationId == "SainsburysMereGreen"

            // Mere Green Community Centre
            beaconPairs[6].getFoundEvent() == proximityList[2]
            beaconPairs[6].getLostEvent() == proximityList[3]
            !beaconPairs[6].isVisit()
            beaconPairs[6].getLocation().locationId == "MereGreenCommunityCentre"

            // Mere Green Community Centre
            beaconPairs[7].getFoundEvent() == proximityList[0]
            beaconPairs[7].getLostEvent() == proximityList[1]
            !beaconPairs[7].isVisit()
            beaconPairs[7].getLocation().locationId == "MereGreenCommunityCentre"
    }

}
