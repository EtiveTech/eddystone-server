package org.etive.city4age.repository

import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Stepwise
@Transactional
class PoiEventGeneratorIntegrationSpec extends Specification {
    def poiEventService

    def setup() {
    }

    def cleanup() {
    }

    void "Create Poi Events in database"() {
        given:
            new PoiEventGenerator().generateEvents()
        when:
            def poiList = poiEventService.listPoiEvents()
        then:
            poiList.size() == 4

            poiList[3].action == "POI_ENTER"
            poiList[3].location.locationId == "MereGreenCommunityCentre"

            poiList[2].action == "POI_ENTER"
            poiList[2].location.locationId == "MereGreenLibrary"

            poiList[1].action == "POI_EXIT"
            poiList[1].location.locationId == "MereGreenLibrary"

            poiList[0].action == "POI_EXIT"
            poiList[0].location.locationId == "MereGreenCommunityCentre"

    }

    void "Try to Create same Poi Events in database"() {
        given:
            new PoiEventGenerator().generateEvents()
        when:
            def poiList = poiEventService.listPoiEvents()
        then:
            poiList.size() == 4

    }
}
