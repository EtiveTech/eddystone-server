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
        for (receiver in careReceivers) {
            def yesterday = new Date() - 1
            def start = (receiver.eventsGenerated) ? receiver.eventsGenerated : yesterday
            start.upto(yesterday) { date ->
                EventList list = new EventList(proximityEventService.forProcessing(receiver, date ))
                def events = PoiEvent.findEvents(receiver, list).reverse()
                for (event in events) {
                    poiEventService.createPoiEvent(event)
                }
                receiver.eventsGenerated = events.last().timestamp
            }
            careReceiverService.persistChanges(receiver)
        }
    }
}
