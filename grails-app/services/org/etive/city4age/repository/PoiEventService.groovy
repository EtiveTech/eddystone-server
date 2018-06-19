package org.etive.city4age.repository

import grails.transaction.Transactional
import groovy.time.TimeCategory

@Transactional
class PoiEventService {
    def proximityEventService

    PoiEvent createPoiEvent(PoiEvent poiEvent) {
        poiEvent = poiEvent.save()
        if (poiEvent) {
            // The next line will only work if the ENTER event is processed before the EXIT
            poiEvent.instanceId = poiEvent.instance.id
            poiEvent.save()
            for (sourceEvent in poiEvent.sourceEvents) {
                sourceEvent.poiEvent = poiEvent
                proximityEventService.persistChanges(sourceEvent)
            }
            log.info("Created " + poiEvent.action + " event for " + poiEvent.location.name)
        }
        try {
            poiEvent = poiEvent.save()
        }
        catch (Exception e) {
            log.error(e.message)
            poiEvent = null
        }

        return poiEvent
    }

    def persistChanges(poiEvent) {
        try {
            poiEvent = poiEvent.save()
        }
        catch (Exception e) {
            log.error(e.message)
            poiEvent = null
        }

        return poiEvent
    }

    @Transactional(readOnly = true)
    def listPoiEvents(CareReceiver receiver) {
        def query = (receiver) ? PoiEvent.where{ careReceiver.id == receiver.id } : PoiEvent
        return query.list(offset: 0, max: 500, sort: "id", order: "desc")
    }

//    function to filter list of poi events by action and location
    @Transactional(readOnly = true)
    def filterPoiEvents(CareReceiver receiver, String locationType, start, finish) {

        // Filters query by relevant details
        log.info("attempting to return query size")
       def query = PoiEvent.where{ careReceiver.id == receiver.id &&
                                    action == "POI_ENTER" &&
                                    location.type == locationType &&
                                    timestamp > start &&
                                    timestamp < finish }
        Integer size = query.list().size()
        log.info("The total visits for " + locationType + " is " + size )
        log.info("The start time is " + start.toString())
//        return query.list().size()
        return size
    }

    // Gets the date for the Monday of the current week
    def getThisWeeksMondayDate(){
        def cal = Calendar.instance
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_WEEK, -1)
        }
        return cal.time
    }

    //Gets the date for the Monday of the previous week
    def getDateLastWeekMonday(thisWeekMonday){
        Date lastWeekMonday = thisWeekMonday - 7
        lastWeekMonday.set(second:0, minute:0, hourOfDay:0)
        return lastWeekMonday
    }

    // Gets the Date of the first of the current month
    def getTheDateOfTheFirstOfTheMonth(){
        def cal = Calendar.instance
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(second:0, minute:0, hourOfDay:0)
        return cal.time
    }

    // Gets the Date of the first of the previous month
    def getTheDateOfTheFirstOfLastMonth(thisMonthFirst){
        Date lastMonthFirst = thisMonthFirst
        Integer month = lastMonthFirst.month - 1
        lastMonthFirst.set(second:0, minute:0, hourOfDay:0, month:month)
        return lastMonthFirst
    }

//    // Gets the Date of the first of the previous month
//    def getTheDateOfTheFirstOfLastMonth(){
//        def cal = Calendar.instance
//        cal.set(Calendar.DAY_OF_MONTH, 1)
//        cal.add(Calendar.MONTH, -1)
//        cal.set(second:0, minute:0, hourOfDay:0)
//        return cal.time
//    }




    @Transactional(readOnly = true)
    def readyForUpload() {
        def query = PoiEvent.where{ uploaded == false }
        return query.list(sort: "timestamp", order: "asc")
    }

    @Transactional(readOnly = true)
    def firstPoiEvent(CareReceiver receiver) {
        def query = PoiEvent.where{ careReceiver.id == receiver.id }
        def first = query.list(max: 1)
        return (first) ? first[0] : null
    }

    def generatePoiEvents(CareReceiver receiver, Date start, Date end) {
        log.info("Generating POI Events for careReceiver #" + receiver.id)
        def date = new Date(start.getTime())
        def timestamp = null
        for (; date <= end; date += 1) {
            def list = proximityEventService.forProcessing(receiver, date)
            if (!list) continue
            ProximityEventList eventList = new ProximityEventList(list)
            log.info("Proximity events found for " + date.getDateString() + ": " + eventList.size())
            def events = PoiEvent.findEvents(receiver, eventList)
            log.info("POI events found for " + date.getDateString() + ": " + events.size())
            for (event in events) {
                def poiEvent = createPoiEvent(event)
                if (!poiEvent) break
                timestamp = poiEvent.timestamp
            }
        }
        return timestamp
    }
}