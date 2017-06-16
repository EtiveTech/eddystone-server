package org.etive.city4age.repository

import grails.transaction.Transactional

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
            log.info("CareReceiver #" + poiEvent.careReceiver.id + ": Created " + poiEvent.action + " event for " + poiEvent.location.name)
        }
        return poiEvent
    }

    def persistChanges(poiEvent) {
        return poiEvent.save()
    }

    @Transactional(readOnly = true)
    def listPoiEvents(CareReceiver receiver) {
        def query = (receiver) ? PoiEvent.where{ careReceiver.id == receiver.id } : PoiEvent
        return query.list(offset: 0, max: 500, sort: "id", order: "desc")
    }

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
}