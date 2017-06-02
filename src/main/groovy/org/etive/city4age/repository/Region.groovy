package org.etive.city4age.repository

class Region {
    private static List<Region> allRegions = []
    private static final DEFAULT_RADIUS = 25
    private String mName
    private List<Location> mLocations
    private mCentre
    private mRadius
    private Boolean mSingleton
    private Date mTimestamp

    Region(Location location) {
        mLocations = [ location ]
        mCentre = mRadius = null
        mSingleton = false
        mName = ""
        mTimestamp = new Date()
        if (location) {
            mSingleton = !location.regionId
            if (mSingleton) {
                mName = "_" + location.locationId
                mCentre = toLatLng(location.latitude, location.longitude)
                mRadius = (location.radius) ? location.radius : DEFAULT_RADIUS
            }
            else {
                mName = location.regionId
            }
        }
    }

    def add(Location location) {
        if (location && !mSingleton) {
            // Reset the centre and radius
            if (mCentre || mRadius) mCentre = mRadius = null
            mLocations.add(location)
            mTimestamp = new Date()
            return true
        }
        return false
    }

    def getName() {
        return mName
    }

    def getCentre() {
        if (!mCentre) mCentre = calculateCentre()
        return mCentre
    }

    def getRadius() {
        if (!mRadius) {
            if (!mCentre) mCentre = calculateCentre()
            mRadius = calculateRadius()
        }
        return mRadius
    }

    private static toLatLng(latitude, longitude) {
        return [ lat: latitude as Double, lng: longitude as Double ]
    }

    private static Double toRadians(x) {
        return x.toDouble() * Math.PI / 180
    }

    private static Double toDegrees(x) {
        return x.toDouble() * 180 / Math.PI
    }

    private calculateCentre() {
        def count = mLocations.size()

        if (count == 1) {
            return toLatLng(mLocations[0].latitude, mLocations[0].longitude)
        }

        Double x = 0
        Double y = 0
        Double z = 0

        for (def location in mLocations) {
            def latitude = toRadians(location.latitude)
            def longitude = toRadians(location.longitude)

            x += Math.cos(latitude) * Math.cos(longitude)
            y += Math.cos(latitude) * Math.sin(longitude)
            z += Math.sin(latitude)
        }

        x = x / count
        y = y / count
        z = z / count

        def centreLongitude = toDegrees(Math.atan2(y, x))
        def centreLatitude = toDegrees(Math.atan2(z, Math.sqrt(x * x + y * y)))

        return toLatLng(centreLatitude, centreLongitude)
    }

    private calculateRadius() {
        def radius = 0
        for (def location in mLocations) {
            // Haversine formula:
            // a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
            // c = 2 ⋅ atan2( √a, √(1−a) )
            // d = R ⋅ c
            // where φ is latitude, λ is longitude, R is earth’s radius
            // note that angles need to be in radians to pass to trig functions!

            def lat1 = toRadians(mCentre.latitude)
            def lat2 = toRadians(location.latitude)
            def latDelta = toRadians(location.latitude - mCentre.latitude)
            def lngDelta = toRadians(location.longitude - mCentre.longitude)

            def a = Math.sin(latDelta/2) * Math.sin(latDelta/2) +
                    Math.cos(lat1) * Math.cos(lat2) *
                    Math.sin(lngDelta/2) * Math.sin(lngDelta/2)
            def c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
            def distance = 6371e3 * c // in metres
            if (distance > radius) radius = distance
        }
        if (radius == 0) radius = DEFAULT_RADIUS
        return radius
    }

    static list() {
        return allRegions
    }

    static findByName(name) {
        if (name) {
            for (def group in allRegions) {
                if (group.mName == name) return group
            }
        }
        return null
    }

    static addLocation(Location location) {
        if (location) {
            def group = findByName(location.regionId)
            if (group)
                group.add(location)
            else
                allRegions.add(new Region(location))
        }
    }

    static addLocations(List<Location> locations) {
        for (def location in locations) {
            def group = findByName(location.regionId)
            if (group)
                group.add(location)
            else
                allRegions.add(new Region(location))
        }
    }

    static forDownload() {
        def list = []
        Date timestamp = new Date(0)
        for (def region in allRegions) {
            if (timestamp.getTime() < region.mTimestamp.getTime()) timestamp = region.mTimestamp
            list.add([
                    point: region.getCentre(),
                    radius: region.getRadius()
            ])
        }
        return [
                changed: timestamp.getTime(),
                regions: list
        ]
    }
}
