package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class PoiEventService {
    def proximityEventService

    PoiEvent createPoiEvent(PoiEvent poiEvent) {
        def timestamp = null
        for (sourceEvent in poiEvent.sourceEvents) {
            if (!timestamp || (sourceEvent.timestamp.getTime() < timestamp.getTime())) timestamp = sourceEvent.timestamp
        }
        poiEvent.timestamp = timestamp
        poiEvent = poiEvent.save()

        if (poiEvent) {
            for (sourceEvent in poiEvent.sourceEvents) {
                sourceEvent.poiEvent = poiEvent
                proximityEventService.persistChanges(sourceEvent)
            }
            log.info("CareReceiver #" + poiEvent.careReceiver.id + ": Created " + poiEvent.action + " event for " + poiEvent.location.name)
        }

        return poiEvent
    }

//    PoiEvent createPoiEvent(String action, CareReceiver receiver, Location location, List<ProximityEvent> sourceEvents) {
//        def poiEvent = new PoiEvent(
//                action: action,
//                careReceiver: receiver,
//                location: location
//        )
//        def timestamp = null
//        for (sourceEvent in sourceEvents) {
//            sourceEvent.poiEvent = poiEvent
//            proximityEventService.persistChanges(sourceEvent)
//            if (!timestamp || (sourceEvent.timestamp < timestamp)) timestamp = sourceEvent.timestamp
//        }
//        poiEvent.timestamp = timestamp
//        log.info("CareReceiver #" + receiver.id + ": Created " + action + " event for " + location.name)
//        return poiEvent
//    }
//
//    def deletePoiEvent(event) {
//        def query = ProximityEvent.where { poiEvent.id == event.id }
//        def proximityEvents = query.list()
//        for (proximityEvent in proximityEvents) {
//            proximityEvent.poiEvent = null
//            proximityEventService.persistChanges(proximityEvent)
//        }
//        log.info("CareReceiver #" + event.careReceiver.id + ": Deleting " + event.action + " event for " + event.location.name)
//        event.delete()
//    }

    def persistChanges(poiEvent) {
        return poiEvent.save()
    }
}