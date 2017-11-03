package org.etive.city4age.repository

class PoiEvent {
    String action
    Date timestamp
    String instanceId
    Boolean uploaded = false
    Float rating
    Date dateCreated
    Date lastUpdated
    List<ProximityEvent> sourceEvents = []
    PoiEvent instance
    BeaconPair beaconPair

    static final String poiEnter = "POI_ENTER"
    static final String poiExit = "POI_EXIT"

    static hasMany = [proximityEvents: ProximityEvent]
    static belongsTo = [careReceiver: CareReceiver, location: Location]

    // The rating field is a transient for now to stop it changing the schema
    static transients = [ "sourceEvents", "instance", "beaconPair" ]

    static constraints = {
        action nullable: false, blank: false
        timestamp nullabe: false
        instanceId nullable: true
        careReceiver nullable: false
        location nullable: false
        uploaded nullable: false
        rating nullable: false
    }

    static Integer getId(instance) {
        if (instance) return instance.id
        return null
    }

    private static addVisit(pair, receiver, eventList) {
        // Set rating to 0.9 because, while we don't know the Care Receiver went into the store,
        // We know they hung around for a while
        def rating = 0.9
        def sourceEvent = pair.getFoundEvent()
        def location = pair.getLocation()
        def entryEvent = new PoiEvent(
                action: poiEnter,
                careReceiver: receiver,
                location: location,
                sourceEvents: [sourceEvent],
                timestamp: sourceEvent.timestamp,
                rating: rating,
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
                rating: rating,
                beaconPair: pair)

        eventList.add(exitEvent)
        eventList.add(entryEvent)
    }

    private static addWalkBys(entryPair, exitPair, receiver, eventList) {
        // We can't be sure that the Care Receiver actually went into the POI
        // However, the chances are pretty high
        def rating = 0.7
        if (entryPair.isVisit()) rating += 0.1
        if (exitPair.isVisit()) rating += 0.1

        def sourceEvents = entryPair.getSourceEvents()
        def entryEvent = new PoiEvent(
                action: poiEnter,
                careReceiver: receiver,
                location: entryPair.getLocation(),
                sourceEvents: sourceEvents,
                timestamp: ProximityEvent.getEntryTimestamp(sourceEvents),
                rating: rating,
                beaconPair: entryPair)

        sourceEvents = exitPair.getSourceEvents()
        def exitEvent = new PoiEvent(
                action: poiExit,
                careReceiver: receiver,
                location: exitPair.getLocation(),
                sourceEvents: sourceEvents,
                timestamp: ProximityEvent.getExitTimestamp(sourceEvents),
                rating: rating,
                beaconPair: exitPair)

        exitEvent.instance = entryEvent.instance = entryEvent
        eventList.add(exitEvent)
        eventList.add(entryEvent)
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
            if (stack.locationExists(pair)) {
                // This pair matches with a pair on the stack
                // There may be a number of unmatched entries before the location that's needed
                while (!stack.locationMatch(pair)) {
                    def popped = stack.pairPop()
                    if (popped.isVisit()) addVisit(popped, receiver, poiEvents)
                }
            }
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
                    addWalkBys(pair, exitPair, receiver, poiEvents)
                }
                else {
                    // The new event pair is not in the same location as the stacked pair
                    if (pair.containedBy(exitPair)) {
                        // Put exitPair back on the stack because the new pair is contained by it
                        stack.pairPush(exitPair)
                    }
                    else {
                        // If exitPair has a long duration create some POI events
                        // Otherwise it will be thrown away
                        if (exitPair.isVisit()) addVisit(exitPair, receiver, poiEvents)
                    }
                    stack.pairPush(pair)
                }
            }
        }

        // If there is anything remaining on the exit stack, see if events can be raised
        while (!stack.isEmpty()) {
            pair = stack.pairPop()
            if (pair.isVisit()) addVisit(pair, receiver, poiEvents)
        }

        poiEvents.sort{ a, b -> a.timestamp.getTime() <=> b.timestamp.getTime() }

        return poiEvents
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
                rating: rating,
                extra: [
                        name: location.name,
                        address: location.address
                ]
        ]
    }
}
