package org.etive.city4age.scheduler

import org.etive.city4age.repository.EventList
import org.etive.city4age.repository.PoiEvent

class GenerateEventsJob {
    def proximityEventService
    def PoiEventService
    def careReceiverService

    static triggers = {
        cron name: 'poiTrigger', cronExpression: "0 0/5 * * * ?"
    }

    static boolean sameDay(Date date1, Date date2) {
        def first = (new Date(date1.getTime())).clearTime()
        def second = (new Date(date2.getTime())).clearTime()
        return first.compareTo(second)
    }

    def execute() {
        // execute job
        def careReceivers = careReceiverService.listCareReceivers()
        def today = (new Date()).clearTime()
        def yesterday = today - 1
        for (receiver in careReceivers) {
            Date start = (receiver.eventsGenerated) ? receiver.eventsGenerated.clearTime() : yesterday
            Date date = new Date(start.getTime())
            for (; date <= yesterday; date += 1) {
                EventList list = new EventList(proximityEventService.forProcessing(receiver, date))
                def events = PoiEvent.findEvents(receiver, list)
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
