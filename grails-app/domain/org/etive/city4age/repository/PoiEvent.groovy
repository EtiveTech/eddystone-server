package org.etive.city4age.repository

class PoiEvent {
    String action
    Date timestamp
    String instanceId
    Boolean uploaded = false
    Date dateCreated
    Date lastUpdated
    List<ProximityEvent> sourceEvents = []
    PoiEvent instance

    static bepThreshold = System.getenv("BEP_THRESHOLD")
    static noiseThreshold = System.getenv("NOISE_THRESHOLD")
    static final String poiEnter = "POI_ENTER"
    static final String poiExit = "POI_EXIT"

    static hasMany = [proximityEvents: ProximityEvent]
    static belongsTo = [careReceiver: CareReceiver, location: Location]

    static transients = [ "sourceEvents", "instance" ]

    static constraints = {
        action nullable: false, blank: false
        timestamp nullabe: false
        instanceId nullable: true
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
        noiseThreshold = (noiseThreshold) ?: 2.5

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
                    def diff = lostEvent.timestamp.getTime() - foundEvent.timestamp.getTime()
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

                                    def entryEvent = new PoiEvent(
                                            action: poiEnter,
                                            careReceiver: receiver,
                                            location: beacon.location,
                                            sourceEvents: sourceEvents,
                                            timestamp: ProximityEvent.getTimestamp(sourceEvents))

                                    exitEvent.instance = entryEvent.instance = entryEvent
                                    poiEvents.add(exitEvent)
                                    poiEvents.add(entryEvent)
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
                                            sourceEvents: sourceEvents,
                                            timestamp: ProximityEvent.getTimestamp(sourceEvents)))
                                }
                            } else {
                                // No stacked exit event so this must be an exit event
                                exitStack.push(new PoiEvent(
                                        action: poiExit,
                                        careReceiver: receiver,
                                        location: beacon.location,
                                        sourceEvents: sourceEvents,
                                        timestamp: ProximityEvent.getTimestamp(sourceEvents)))
                            }
                        }
                    } else {
                        // We appear to have two distinct events

                        // If there is already an exit event on the stack then this could be a very long POI_ENTER
                        // Need to either remove the stacked exit or re-classify this pair ss a BEP
                        // Remove the stacked exit unless it's for the container location of this location until it is clear
                        // we need to do otherwise
                        if ((exitStack) &&
                                !(beacon.location.container && beacon.location.container.id == exitStack.last().location.id)) {
                            exitStack.pop()
                        }

                        def entryEvent = new PoiEvent(
                                action: poiEnter,
                                careReceiver: receiver,
                                location: beacon.location,
                                sourceEvents: [foundEvent],
                                timestamp: foundEvent.timestamp)
                        entryEvent.instance = entryEvent

                        def exitEvent = new PoiEvent(
                                action: poiExit,
                                instance: entryEvent,
                                careReceiver: receiver,
                                location: beacon.location,
                                sourceEvents: [lostEvent],
                                timestamp: lostEvent.timestamp)

                        poiEvents.add(exitEvent)
                        poiEvents.add(entryEvent)
                    }
                }
            }
        }
        return poiEvents.sort{ a, b -> a.timestamp.getTime() <=> b.timestamp.getTime() }
    }

    def formatForUpload() {
        return [
                action: "eu:c4a:" + action,
                user: careReceiver.city4ageId,
                pilot: "BHX",
                location: "eu:c4a:" + location.type + ":" + location.locationId,
                position: location.latitude + " " + location.longitude,
                timestamp: timestamp.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
                payload: [
                        instance_id: instanceId
                ],
                data_source_type: [ "sensors" ],
                extra: [
                        name: location.name,
                        address: location.address
                ]
        ]
    }
}
