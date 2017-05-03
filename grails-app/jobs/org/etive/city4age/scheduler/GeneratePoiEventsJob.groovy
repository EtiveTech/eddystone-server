package org.etive.city4age.scheduler

import org.etive.city4age.repository.EventList
import org.etive.city4age.repository.PoiEvent

class GenerateEventsJob {
    def proximityEventService
    def PoiEventService

    static triggers = {
        cron name: 'poiTrigger', cronExpression: "0 0 1 * * ?"
    }

    def execute() {
        // execute job
        def careReceivers = careReceiverService.listCareReceivers()

        for (receiver in careReceivers) {
            EventList list = new EventList(proximityEventService.forCareReceiver(receiver, (new Date()) - 1))
            def events = PoiEvent.findEvents(receiver, list)
            for (event in events) {
                poiEventService.createPoiEvent(event)
            }
        }
    }
}
