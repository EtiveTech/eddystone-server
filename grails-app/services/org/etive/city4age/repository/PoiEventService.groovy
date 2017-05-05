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
            // The next line will only work if the ENTER event is processed before the EXIT
            poiEvent.instanceId = poiEvent.instance.id
            poiEvent.save()
            for (sourceEvent in poiEvent.sourceEvents) {
                sourceEvent.poiEvent = poiEvent
                proximityEventService.persistChanges(sourceEvent)
            }
            log.info("CareReceiver #" + poiEvent.careReceiver.id + ": Created " + poiEvent.action + " event for " + poiEvent.location.name)
        }

        return poiEvent
    }

    def persistChanges(poiEvent) {
        return poiEvent.save()
    }

    @Transactional(readOnly = true)
    def listPoiEvents(CareReceiver receiver) {
        // Only list events for today to keep the amount of data down
        def early = (new Date()).clearTime()
        def late = early + 1

        def query
        if (receiver)
            query = PoiEvent.where{ careReceiver.id == receiver.id && timestamp >= early && timestamp < late }
        else
            query = PoiEvent.where{ timestamp >= early && timestamp < late }
        return query.list(max: 500)
    }

    @Transactional(readOnly = true)
    def readyForUpload() {
        def query = PoiEvent.where{ uploaded == false }
        return query.list(order: timestamp)
    }
}