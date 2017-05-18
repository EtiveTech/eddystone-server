package org.etive.city4age.scheduler

import org.etive.city4age.repository.EventList
import org.etive.city4age.repository.PoiEvent

class GenerateEventsJob {
    def proximityEventService
    def PoiEventService
    def careReceiverService

    static triggers = {
        cron name: 'poiTrigger', cronExpression: "0 30 2 * * ?"
    }

    def execute() {
        // execute job
        def careReceivers = careReceiverService.listCareReceivers()
        def today = (new Date()).clearTime()
        def yesterday = today - 1
        for (receiver in careReceivers) {
            Date start = receiver.eventsGenerated
            if (!start) {
                def first = proximityEventService.firstProximityEvent(receiver)
                start = (first) ? first.timestamp : yesterday
            }
            start.clearTime()
            Date date = new Date(start.getTime())
            for (; date <= yesterday; date += 1) {
                EventList list = new EventList(proximityEventService.forProcessing(receiver, date))
                def events = PoiEvent.findEvents(receiver, list)
                if (events) {
                    def timestamp = null
                    for (event in events) {
                        def poiEvent = poiEventService.createPoiEvent(event)
                        if (!poiEvent) break
                        timestamp = poiEvent.timestamp
                    }
                    if (timestamp) {
                        receiver.eventsGenerated = timestamp
                        careReceiverService.persistChanges(receiver)
                    }
                }
            }
        }
    }
}
