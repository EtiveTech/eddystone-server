package org.etive.city4age.repository

class PoiEvent {
    String action
    Date timestamp
    Boolean uploaded = false
    List<ProximityEvent> sourceEvents = []

    static bepThreshold = System.getenv("BEP_THRESHOLD")
    static noiseThreshold = System.getenv("NOISE_THRESHOLD")
    static final String poiEnter = "POI_ENTER"
    static final String poiExit = "POI_EXIT"

    static hasMany = [proximityEvents: ProximityEvent]
    static belongsTo = [careReceiver: CareReceiver, location: Location]

    static transients = [ "sourceEvents" ]

    static constraints = {
        action nullable: false, blank: false
        timestamp nullabe: false
        careReceiver nullable: false
        location nullable: false
        uploaded nullable: false
    }

    static List<PoiEvent> findEvents(CareReceiver receiver, EventList list) {
        List<PoiEvent> poiEvents = []
        List<PoiEvent> exitStack = []
        ProximityEvent foundEvent, lostEvent
        Beacon beacon

        bepThreshold = (bepThreshold) ?: 60
        noiseThreshold = (noiseThreshold) ?: 5

        // The supplied list is a reversed list of proximity events
        // The result of this method - poiEvents - will have the order reversed again
        while (!list.isEmpty()) {
            lostEvent = list.nextLost()
            if (lostEvent) {
                // Have a lost event. Now look for the found event.
                beacon = lostEvent.beacon
                foundEvent = list.nextFound(beacon)
                if (foundEvent) {
                    // Have a lost event and a found event
                    def diff = lostEvent.timestamp - foundEvent.timestamp
                    if (diff <= bepThreshold * 1000) {
                        // If the found and lost events are too close together the CareReceiver can't have gone into a POI
                        if (diff > noiseThreshold * 1000) {
                            // Have a Beacon Event Pair (BEP) - found and lost events that are close together
                            def sourceEvents = [foundEvent, lostEvent]
                            if (exitStack) {
                                // Have a stacked exit event - may just have found the entry event
                                PoiEvent exitEvent = exitStack.pop()
                                if (exitEvent.location.id == beacon.location.id) {
                                    // The exit event and the new BEP are for the same location
                                    // Must be the enter event
                                    poiEvents.add(exitEvent)
                                    poiEvents.add(new PoiEvent(
                                            action: poiEnter,
                                            careReceiver: receiver,
                                            location: beacon.location,
                                            sourceEvents: sourceEvents))
                                } else {
                                    // Our new BEP is not in the same location as the stacked exit event
                                    if (beacon.location.container &&
                                            beacon.location.container.id == exitEvent.location.id) {
                                        // Forget about the exit event unless we have a POI inside a POI
                                        exitStack.push(exitEvent)
                                    }
                                    exitStack.push(new PoiEvent(
                                            action: poiExit,
                                            careReceiver: receiver,
                                            location: beacon.location,
                                            sourceEvents: sourceEvents))
                                }
                            } else {
                                // No stacked exit event so this must be an exit event
                                exitStack.push(new PoiEvent(
                                        action: poiExit,
                                        careReceiver: receiver,
                                        location: beacon.location,
                                        sourceEvents: sourceEvents))
                            }
                        }
                    } else {
                        // We have two distinct events
                        poiEvents.add(new PoiEvent(
                                action: "POI_EXIT",
                                careReceiver: receiver,
                                location: beacon.location,
                                sourceEvents: [lostEvent]))
                        poiEvents.add(new PoiEvent(
                                action: "POI_ENTRY",
                                careReceiver: receiver,
                                location: beacon.location,
                                sourceEvents: [foundEvent]))
                    }
                }
            }
        }
        return poiEvents
    }
}
