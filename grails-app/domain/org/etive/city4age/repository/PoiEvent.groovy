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
    BeaconPair beaconPair

    static final String poiEnter = "POI_ENTER"
    static final String poiExit = "POI_EXIT"

    static hasMany = [proximityEvents: ProximityEvent]
    static belongsTo = [careReceiver: CareReceiver, location: Location]

    static transients = [ "sourceEvents", "instance", "beaconPair" ]

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

    private static List<PoiEvent> finder(CareReceiver receiver, List<BeaconPair> list) {
        List<PoiEvent> poiEvents = []
        ExitStack stack = new ExitStack()
        BeaconPair pair
        List<BeaconPair> beaconPairs = list.clone().reverse()

        // The supplied mList is a reversed mList of proximity events
        // The result of this method - poiEvents - will have the order reversed again
        while (beaconPairs) {
            pair = beaconPairs.pop()
            stack.locationMatch(pair)  // If there's an entry with the same location put it on the top of the stack
            if (pair.isWalkBy() || stack.locationCheck(pair)) {
                // If there is already an event pair on the stack then the new pair will always be treated as a POI_ENTER
                if (stack.isEmpty()) {
                    // No stacked exit event so this may be an exit event pair
                    stack.pairPush(pair)
                }
                else {
                    // Have a stacked event pair - may just have found the entry event pair
                    def exitPair = stack.pairPop()
                    if (exitPair.sameLocation(pair)) {
                        // The exit pair and the new pair are for the same location
                        // Must be the enter event
                        def sourceEvents = pair.getSourceEvents()
                        def entryEvent = new PoiEvent(
                                action: poiEnter,
                                careReceiver: receiver,
                                location: pair.getLocation(),
                                sourceEvents: sourceEvents,
                                timestamp: ProximityEvent.getEntryTimestamp(sourceEvents),
                                beaconPair: pair)

                        sourceEvents = exitPair.getSourceEvents()
                        def exitEvent = new PoiEvent(
                                action: poiExit,
                                careReceiver: receiver,
                                location: exitPair.getLocation(),
                                sourceEvents: sourceEvents,
                                timestamp: ProximityEvent.getExitTimestamp(sourceEvents),
                                beaconPair: exitPair)

                        exitEvent.instance = entryEvent.instance = entryEvent
                        poiEvents.add(exitEvent)
                        poiEvents.add(entryEvent)
                    }
                    else {
                        // The new event pair is not in the same location as the stacked pair
                        if (pair.containedBy(exitPair)) stack.pairPush(exitPair)
                        stack.pairPush(pair)
                    }
                }
            }
            else {
                // Consider the event pair to contain two distinct events

                // Remove a stacked event pair unless it's for the containedBy location
                if (!stack.isEmpty() && !pair.containedBy(stack.pairPeek())) stack.pairPop()

                def sourceEvent = pair.getFoundEvent()
                def location = pair.getLocation()
                def entryEvent = new PoiEvent(
                        action: poiEnter,
                        careReceiver: receiver,
                        location: location,
                        sourceEvents: [sourceEvent],
                        timestamp: sourceEvent.timestamp,
                        beaconPair: pair)
                entryEvent.instance = entryEvent

                sourceEvent = pair.getLostEvent()
                def exitEvent = new PoiEvent(
                        action: poiExit,
                        instance: entryEvent,
                        careReceiver: receiver,
                        location: location,
                        sourceEvents: [sourceEvent],
                        timestamp: sourceEvent.timestamp,
                        beaconPair: pair)

                poiEvents.add(exitEvent)
                poiEvents.add(entryEvent)
            }
        }

        return poiEvents.sort{ a, b -> a.timestamp.getTime() <=> b.timestamp.getTime() }
    }

    static List<PoiEvent> findEvents(CareReceiver receiver, ProximityEventList list) {
        // beaconPairs are in reverse order - this is historical - no real need now
        List<BeaconPair> beaconPairs = BeaconPair.findBeaconPairs(list)
        return finder(receiver, beaconPairs)
    }

    static List<PoiEvent> findEvents(String receiverEmail, List<BeaconPair> beaconPairs) {
        def careReceiver = CareReceiver.findByEmailAddress(receiverEmail)
        return finder(careReceiver, beaconPairs)
    }

    static List<PoiEvent> findEvents(String receiverEmail, ProximityEventList list) {
        def careReceiver = CareReceiver.findByEmailAddress(receiverEmail)
        List<BeaconPair> beaconPairs = BeaconPair.findBeaconPairs(list)
        return finder(careReceiver, beaconPairs)
    }

    def formatForUpload() {
        return [
                action: "eu:c4a:" + action,
                user: careReceiver.city4AgeId,
                pilot: "BHX",
                location: "eu:c4a:" + location.type + ":" + location.locationId,
                position: location.latitude + " " + location.longitude,
                timestamp: timestamp.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("Europe/London")),
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
