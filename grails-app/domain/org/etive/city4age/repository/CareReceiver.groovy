package org.etive.city4age.repository

import org.etive.city4age.withings.WithingsService

class CareReceiver {

    static private final String dayBeforeProjectStart = "2016-12-31"
    static private final String nullCity4AgeId = "<unknown>"

    String emailAddress
    String token
    Long logbookId
    String city4AgeId = nullCity4AgeId  // not given a proper id until data is uploaded
    Long withingsId
    String accessKey
    String accessSecret
    String activityRecordsDownloaded
    String sleepRecordsDownloaded
    Date eventsGenerated
    Boolean forTest = false // This Care Receiver's data is for test only and should not be uploaded
    Date dateCreated
    Date lastUpdated

    static hasMany = [device: Device, proximityEvents: ProximityEvent, poiEvents: PoiEvent,
                      activityRecords: ActivityRecord, sleepRecords: SleepRecord]

    static constraints = {
        emailAddress blank: false, nullable: false, email: true, unique: true
        token blank: false, nullable: false, unique: true
        proximityEvents nullable: true
        logbookId nullable: false, unique: true
        city4AgeId blank: false, nullable: true
        device nullable: true
        withingsId nullable: false
        accessKey blank: false, nullable: false
        accessSecret blank: false, nullable: false
        activityRecordsDownloaded nullable: true, blank: false
        sleepRecordsDownloaded nullable: true, blank: false
        eventsGenerated nullable: true
        forTest nullable: false
    }

    private fetchActivityData(Date startDate, Date endDate = null) {
        if (endDate && (startDate.format("yyyy-MM-dd") == endDate.format("yyyy-MM-dd"))) endDate = null
        return WithingsService.instance.fetchActivityData(this, startDate, endDate)
    }

    private fetchSleepData(Date startDate, Date endDate = null) {
        return WithingsService.instance.fetchSleepData(this, startDate, endDate)
    }

    def fetchWithingsData(Date startDate, Date endDate = null) {
        return [ activity: fetchActivityData(startDate, endDate), sleep: fetchSleepData(startDate + 1, endDate) ]
    }

    def updateWithingsData(Date endDate) {
        def activities = []
        def sleeps = []

        def sEndDate = endDate.format("yyyy-MM-dd")
        def sActivityDate = (activityRecordsDownloaded) ? activityRecordsDownloaded : dayBeforeProjectStart
        def activityDate = Date.parse("yyyy-MM-dd", sActivityDate) + 1
        def sSleepDate = (sleepRecordsDownloaded) ? sleepRecordsDownloaded : dayBeforeProjectStart
        def sleepDate = Date.parse("yyyy-MM-dd", sSleepDate) + 1

        if (sEndDate >= sActivityDate) activities = fetchActivityData(activityDate, endDate)
        if (sEndDate >= sSleepDate) sleeps = fetchSleepData(sleepDate, endDate)

        return [ activity: activities, sleep: sleeps ]
    }

    def getDataDate() {
        def dates = []

        if (this.poiEvents) dates << new Date(this.poiEvents[0].timestamp.getTime()).clearTime()
        if (this.sleepRecords) dates << Date.parse("yyyy-MM-dd", this.sleepRecords[0].date)
        if (this.activityRecords) dates << Date.parse("yyyy-MM-dd", this.activityRecords[0].date)

        dates.sort{ a, b -> a.getTime() <=> b.getTime() }

        return (dates) ? dates.first() : (new Date()).clearTime()
    }

    def hasCity4AgeId() {
        return (city4AgeId) ? (city4AgeId != nullCity4AgeId) : false
    }


    static getStartDate() {
        return Date.parse("yyyy-MM-dd", dayBeforeProjectStart) + 1
    }
}