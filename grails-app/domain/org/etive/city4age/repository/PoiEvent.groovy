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

    static Integer getId(instance) {
        if (instance) return instance.id
        return null
    }

    static private Boolean shouldActAsWalkBy(eventPair, exitStack) {
        return (exitStack && (exitStack.last().sameLocation(eventPair)))
    }

    static List<PoiEvent> findEvents(CareReceiver receiver, ProximityEventList list) {
        List<PoiEvent> poiEvents = []
        List<BeaconPair> exitStack = []
        BeaconPair pair

        // beaconPairs are in reverse order - this is historical - no real need now
        def beaconPairs = BeaconPair.findBeaconPairs(list)

        // The supplied mList is a reversed mList of proximity events
        // The result of this method - poiEvents - will have the order reversed again
        while (beaconPairs) {
            pair = beaconPairs.pop()
            if (pair.isWalkBy() || shouldActAsWalkBy(pair, exitStack)) {
                // If there is already an event pair on the stack then the new pair will always be treated as a POI_ENTER
                if (!exitStack) {
                    // No stacked exit event so this may be an exit event pair
                    exitStack.push(pair)
                }
                else {
                    // Have a stacked event pair - may just have found the entry event pair
                    def exitPair = exitStack.pop()
                    if (exitPair.sameLocation(pair)) {
                        // The exit pair and the new pair are for the same location
                        // Must be the enter event
                        def sourceEvents = pair.getSourceEvents()
                        def entryEvent = new PoiEvent(
                                action: poiEnter,
                                careReceiver: receiver,
                                location: pair.getLocation(),
                                sourceEvents: sourceEvents,
                                timestamp: ProximityEvent.getEntryTimestamp(sourceEvents))

                        sourceEvents = exitPair.getSourceEvents()
                        def exitEvent = new PoiEvent(
                                action: poiExit,
                                careReceiver: receiver,
                                location: exitPair.getLocation(),
                                sourceEvents: sourceEvents,
                                timestamp: ProximityEvent.getExitTimestamp(sourceEvents))

                        exitEvent.instance = entryEvent.instance = entryEvent
                        poiEvents.add(exitEvent)
                        poiEvents.add(entryEvent)
                    }
                    else {
                        // The new event pair is not in the same location as the stacked pair
                        if (pair.containedBy(exitPair)) exitStack.push(exitPair)
                        exitStack.push(pair)
                    }
                }
            }
            else {
                // Consider the event pair to contain two distinct events

                // Remove a stacked event pair unless it's for the containedBy location
                if (exitStack && !pair.containedBy(exitStack.last())) exitStack.pop()

                def sourceEvent = pair.getFoundEvent()
                def location = pair.getLocation()
                def entryEvent = new PoiEvent(
                        action: poiEnter,
                        careReceiver: receiver,
                        location: location,
                        sourceEvents: [sourceEvent],
                        timestamp: sourceEvent.timestamp)
                entryEvent.instance = entryEvent

                sourceEvent = pair.getLostEvent()
                def exitEvent = new PoiEvent(
                        action: poiExit,
                        instance: entryEvent,
                        careReceiver: receiver,
                        location: location,
                        sourceEvents: [sourceEvent],
                        timestamp: sourceEvent.timestamp)

                poiEvents.add(exitEvent)
                poiEvents.add(entryEvent)
            }
        }

        return poiEvents.sort{ a, b -> a.timestamp.getTime() <=> b.timestamp.getTime() }
    }

    def formatForUpload() {
        return [
                action: "eu:c4a:" + action,
                user: careReceiver.city4AgeId,
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
