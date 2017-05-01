package org.etive.city4age.repository

import org.etive.city4age.withings.WithingsService

class CareReceiver {

    String emailAddress
    String token
    Long logbookId
    String city4ageId
    Long withingsId
    String accessKey
    String accessSecret
    private String activityDownloadDate = null
    private String sleepDownloadDate = null
    Date dateCreated
    Date lastUpdated

    static hasMany = [proximityEvents: ProximityEvent, activityRecords: ActivityRecord, sleepRecords: SleepRecord]
    static hasOne = [device: Device]

    static constraints = {
        emailAddress blank: false, nullable: false, email: true
        token blank: false, nullable: false, unique: true
        proximityEvents nullable: true
        logbookId nullable: false
        city4ageId blank: false, nullable: true
        device nullable: true
        withingsId nullable: false
        accessKey blank: false, nullable: false
        accessSecret blank: false, nullable: false
        activityDownloadDate nullable: true
        sleepDownloadDate nullable: true
    }

    private fetchActivityData(Date startDate, Date endDate = null) {
        return WithingsService.instance.fetchActivityData(this, startDate, endDate)
    }

    private fetchSleepData(Date startDate, Date endDate = null) {
        return WithingsService.instance.fetchSleepData(this, startDate, endDate)
    }

    def fetchWithingsData(Date startDate, Date endDate = null) {
        return [ activity: fetchActivityData(startDate, endDate), sleep: fetchSleepData(startDate, endDate) ]
    }

    def updateWithingsData(Date endDate) {
        return [
                activity: fetchActivityData(Date.parse("dd-MM-yyyy", this.activityDownloadDate), endDate),
                sleep: fetchSleepData(Date.parse("dd-MM-yyyy", this.sleepDownloadDate), endDate)
        ]
    }

    def setActivityDownloadDate(String date) {
        this.activityDownloadDate = date
        return this.save(failOnError: true)
    }

    String getActivityDownloadDate() {
        return this.activityDownloadDate
    }

    def setSleepDownloadDate(String date) {
        this.sleepDownloadDate = date
        return this.save(failOnError: true)
    }

    String getSleepDownloadDate() {
        return this.sleepDownloadDate
    }

}