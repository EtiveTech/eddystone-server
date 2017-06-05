package org.etive.city4age.scheduler

import groovy.json.JsonOutput
import groovyx.net.http.HTTPBuilder

class UploadJob {

    def activityRecordService
    def sleepRecordService
    def poiEventService
    def careReceiverService

    private final String centralRepository = System.getenv("CENTRAL_REPOSITORY")

    static triggers = {
        cron name: 'uploadTrigger', cronExpression: "0 30 3 * * ?"
    }

//    private getDataDate(receiver) {
//        def dates = []
//
//        def dbEntry = poiEventService.firstPoiEvent(receiver)
//        if (dbEntry) dates.add(dbEntry.timestamp.clearTime())
//
//        dbEntry = sleepRecordService.firstSleepRecord(receiver)
//        if (dbEntry) dates.add(Date.parse("yyyy-MM-dd", dbEntry.date))
//
//        dbEntry = activityRecordService.firstActivityRecord(receiver)
//        if (dbEntry) dates.add(Date.parse("yyyy-MM-dd", dbEntry.date))
//
//        dates.sort{ a, b -> a.getTime() <=> b.getTime() }
//
//        return (dates) ? dates.first() : (new Date()).clearTime()
//    }

    def execute() {
        // execute job

        if (!centralRepository) return

        // LOGIN
        // GET /api/0.1/login

        // Create the request
//        def client = new HttpClientFactory().createHttpClient()
//        def request = new HttpRequest

        // Check that all Care Receivers have a City4AgeId
        def careReceivers = careReceiverService.listCareReceivers()
        for (def careReceiver in careReceivers) {
            if (!careReceiver.hasCity4AgeId()) {
                // Create a City4AgeId for this user
                def validFrom = careReceiver.getDataDate()
                def id = careReceiver.logbookId
                def payload = [
                        pilot_user_source_id: id,
                        username: "bhx" + id,
                        password: "bhx" + id,
                        valid_from: validFrom.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                ]

                // Send the request with payload
            }
        }

        // for all CareReceivers copy Activity/Sleep measures and POI events up to the Central Repository.

        def activities = activityRecordService.readyForUpload()
        for (def activity in activities) {
            if ((activity.careReceiver.hasCity4AgeId()) && !activity.careReceiver.forTest) {
                def json = JsonOutput.toJson(activity.formatForUpload())
                // send a POST request to the central repository with json as the payload
                // POST /api/0.1/add_measure
                activity.uploaded = true
                activityRecordService.persistChanges(activity)
            }
        }
        def sleeps = sleepRecordService.readyForUpload()
        for (def sleep in sleeps) {
            if ((sleep.careReceiver.hasCity4AgeId()) && !sleep.careReceiver.forTest) {
                def json = JsonOutput.toJson(sleep.formatForUpload())
                // send a POST request to the central repository with json as the payload
                // POST /api/0.1/add_measure
                sleep.uploaded = true
                sleepRecordService.persistChanges(sleep)
            }
        }
        def events = poiEventService.readyForUpload()
        for (def event in events) {
            if ((event.careReceiver.hasCity4AgeId()) && !event.careReceiver.forTest) {
                def json = JsonOutput.toJson(event.formatForUpload())
                // send a POST request to the central repository with json as the payload
                // POST /api/0.1/add_action
                event.uploaded = true
                poiEventService.persistChanges(event)
            }
        }

        // LOGOUT
        // GET /api/0.1/logout
    }
}
