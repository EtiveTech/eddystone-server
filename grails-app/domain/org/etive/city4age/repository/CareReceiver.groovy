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
    Date dateCreated
    Date lastUpdated

    static hasMany = [events: Event]

    static constraints = {
        emailAddress blank: false, nullable: false
        token blank: false, nullable: false
        events nullable: true
        logbookId nullable: false
        city4ageId blank: false, nullable: true
        withingsId nullable: false
        accessKey blank: false, nullable: false
        accessSecret blank: false, nullable: false
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
}