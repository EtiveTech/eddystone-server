package org.etive.city4age.repository

class BeaconPair {

    static private walkByThreshold = System.getenv("WALKBY_THRESHOLD") ?: 60 // seconds
    // It takes 1.4 seconds from first seeing a beacon until it is reported
    // It takes 6 * 1.4 (8.4) seconds for a lost beacon to be reported as lost
    // Thus a beacon that only appears for 8.4 seconds has only been visible for 1.4 seconds
    // If the noise floor is 2.5 seconds, that equates to a beacon that is reported for 8.4 + (2.5 - 1.4) == 9.5
    static private noiseThreshold = System.getenv("NOISE_THRESHOLD") ?: 9.5 // seconds

    private ProximityEvent mFound = null
    private ProximityEvent mLost = null
    private mFoundTimestamp = 0
    private mLostTimestamp = 0
    private mDurationOfProximity = 0
    private mLocationId = -1
    private mContainingLocationId = -1
    private mBeaconId = -1

    BeaconPair(ProximityEvent found, ProximityEvent lost) {

        if (found && lost && found.beacon.id == lost.beacon.id) {
            mFound = found
            mLost = lost
            mFoundTimestamp = found.timestamp.getTime()
            mLostTimestamp = lost.timestamp.getTime()
            mDurationOfProximity = mLostTimestamp - mFoundTimestamp
            def beacon = found.beacon
            mBeaconId = beacon.id
            def location = beacon.location
            mLocationId = location.id
            if (location.containedBy) mContainingLocationId = location.containedBy.id
        }
    }

    Boolean overlaps(BeaconPair another) {
        return ((another.mFoundTimestamp >= mFoundTimestamp && another.mFoundTimestamp <= mLostTimestamp) ||
                (another.mLostTimestamp >= mFoundTimestamp && another.mLostTimestamp <= mLostTimestamp) ||
                (another.mFoundTimestamp <= mFoundTimestamp && another.mLostTimestamp >= mLostTimestamp))
    }

    Boolean sameLocation(BeaconPair another) {
        return (mLocationId == another.mLocationId)
    }

    Boolean containedBy(BeaconPair another) {
        return (mContainingLocationId > 0 && mContainingLocationId == another.mLocationId)
    }

    Boolean isNoise() {
        return (mDurationOfProximity <= noiseThreshold * 1000)
    }

    Boolean isWalkBy() {
        return ((mDurationOfProximity <= walkByThreshold * 1000) && !isNoise())
    }

    Boolean isVisit() {
        return (mDurationOfProximity > walkByThreshold * 1000)
    }

    Boolean shorterStayThan(BeaconPair another) {
        return mDurationOfProximity < another.mDurationOfProximity;
    }

    Boolean longerStayThan(BeaconPair another) {
        return mDurationOfProximity > another.mDurationOfProximity;
    }

    Location getLocation() {
        return (mFound) ? mFound.beacon.location : null
    }

    def getSourceEvents() {
        return (mFound && mLost) ? [mFound, mLost] : []
    }

    def getFoundEvent() {
        return mFound
    }

    def getLostEvent() {
        return mLost
    }

    static List<BeaconPair> findBeaconPairs(ProximityEventList list, Boolean keepOverlaps = false) {
        ProximityEvent foundEvent, lostEvent
        List<BeaconPair> eventPairs = []

        while (!list.isEmpty()) {
            lostEvent = list.nextLost()
            if (lostEvent) {
                // Have a lost event. Now look for the found event.
                foundEvent = list.nextFound(lostEvent.beacon)
                if (foundEvent) {
                    def eventPair = new BeaconPair(foundEvent, lostEvent)
                    if (!eventPair.isNoise()) {
                        def previous = (eventPairs) ? eventPairs.last() : null
                        if (previous && previous.sameLocation(eventPair) && previous.overlaps(eventPair) && !keepOverlaps) {
                            // Don't want to keep overlaps so keep the pair that have the longest duration
                            if (previous.shorterStayThan(eventPair)) {
                                eventPairs.pop() // Lose the previous pair
                                eventPairs.push(eventPair)
                            }
                        }
                        else {
                            eventPairs.push(eventPair)
                        }
                    }
                }
            }
        }
        return eventPairs
    }
}
