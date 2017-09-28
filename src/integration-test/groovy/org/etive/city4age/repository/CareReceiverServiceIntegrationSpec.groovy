package org.etive.city4age.repository

import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Stepwise
@Transactional
class CareReceiverServiceIntegrationSpec extends Specification {
    def careReceiverService
    def poiEventService

    def setup() {
    }

    def cleanup() {
    }

    void "Create Poi Events in database"() {
        given:
            def receiver = CareReceiver.findByEmailAddress("eventlist2@test.org")
            def date = new Date().parse("yyyy-MM-dd", "2017-06-25") // Data for 21-06-2017
            //careReceiverService.generatePoiEvents(receiver, date)
        when:
            def poiList = poiEventService.listPoiEvents(receiver)
        then:
            poiList.size() == 8

            poiList[7].action == "POI_ENTER"
            poiList[7].location.locationId == "MereGreenCommunityCentre"

            poiList[6].action == "POI_EXIT"
            poiList[6].location.locationId == "MereGreenCommunityCentre"

            poiList[5].action == "POI_ENTER"
            poiList[5].location.locationId == "Sainsburys, Mere Green"

            poiList[4].action == "POI_EXIT"
            poiList[4].location.locationId == "Sainsburys, Mere Green"

            poiList[3].action == "POI_ENTER"
            poiList[3].location.locationId == "MereGreenCommunityCentre"

            poiList[2].action == "POI_EXIT"
            poiList[2].location.locationId == "MereGreenCommunityCentre"

            poiList[1].action == "POI_ENTER"
            poiList[1].location.locationId == "MereGreenCommunityCentre"

            poiList[0].action == "POI_EXIT"
            poiList[0].location.locationId == "MereGreenCommunityCentre"

    }
}
