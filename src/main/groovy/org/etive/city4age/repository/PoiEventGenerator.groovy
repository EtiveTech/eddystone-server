package org.etive.city4age.repository

import grails.util.Holders

class PoiEventGenerator {
    // Explicit dependency injection
    private careReceiverService = Holders.grailsApplication.mainContext.getBean('careReceiverService')
    private proximityEventService = Holders.grailsApplication.mainContext.getBean('proximityEventService')
    private poiEventService = Holders.grailsApplication.mainContext.getBean('poiEventService')

    private careReceivers
    private Date yesterday

    PoiEventGenerator() {
        careReceivers = careReceiverService.listCareReceivers()
        Date today = (new Date()).clearTime()
        yesterday = today - 1
    }

    def generateEvents() {
        for (CareReceiver receiver in careReceivers) {
            Date start = receiver.eventsGenerated
            if (start) {
                start += 1  // Day after the last event was processed
            }
            else {
                def first = proximityEventService.firstProximityEvent(receiver)
                start = (first) ? first.timestamp : yesterday
            }
            start.clearTime()   // The very start of the day
            Date date = new Date(start.getTime())
            def timestamp = null
            for (; date <= yesterday; date += 1) {
                def list = proximityEventService.forProcessing(receiver, date)
                if (!list) continue
                ProximityEventList eventList = new ProximityEventList(list)
                def events = PoiEvent.findEvents(receiver, eventList)
                if (events) {
                    for (event in events) {
                        def poiEvent = poiEventService.createPoiEvent(event)
                        if (!poiEvent) break
                        timestamp = poiEvent.timestamp
                    }
                }
            }
            if (timestamp) {
                receiver.eventsGenerated = timestamp
                careReceiverService.persistChanges(receiver)
            }
        }
    }
}
