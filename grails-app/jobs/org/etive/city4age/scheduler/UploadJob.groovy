package org.etive.city4age.scheduler

import groovy.json.JsonOutput

class UploadJob {

    def activityRecordService
    def sleepRecordService
    def poiEventService

    private final String centralRepository = System.getenv("CENTRAL_REPOSITORY")

    static triggers = {
        cron name: 'uploadTrigger', cronExpression: "0 30 3 * * ?"
    }

    def execute() {
        // execute job

        if (!centralRepository) return

        // LOGIN
        // GET /api/0.1/login

        // for all CareReceivers copy Activity/Sleep measures and POI events up to the Central Repository.

        def activities = activityRecordService.readyForUpload()
        for (activity in activities) {
            if (!activity.careReceiver.forTest) {
                def json = JsonOutput.toJson(activity.formatForUpload())
                // send a POST request to the central repository with json as the payload
                // POST /api/0.1/add_measure
                activity.uploaded = true
                activityRecordService.persistChanges(activity)
            }
        }
        def sleeps = sleepRecordService.readyForUpload()
        for (sleep in sleeps) {
            if (!sleep.careReceiver.forTest) {
                def json = JsonOutput.toJson(sleep.formatForUpload())
                // send a POST request to the central repository with json as the payload
                // POST /api/0.1/add_measure
                sleep.uploaded = true
                sleepRecordService.persistChanges(sleep)
            }
        }
        def events = poiEventService.readyForUpload()
        for (event in events) {
            if (!event.careReceiver.forTest) {
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
