package org.etive.city4age.scheduler

import groovy.json.JsonOutput

class UploadToCentralRepositoryJob {

    def activityRecordService
    def sleepRecordService

    private final String centralRepository = System.getenv("CENTRAL_REPOSITORY")

    static triggers = {
        cron name: 'uploadTrigger', cronExpression: "0 53 * * * ?"
    }

    def execute() {
        // execute job

        if (!centralRepository) return
        
        // for all CareReceivers copy Activity/Sleep measures and POI events up to the Central Repository.

        def activities = activityRecordService.forUpload()
        for (activity in activities) {
            def json = JsonOutput.toJson(activity.forUpload())
            // send a POST request to the central repository with json as the payload
            // activity.uploaded = true
            // activityRecordService.update(activity)
        }
        def sleeps = sleepRecordService.forUpload()
        for (sleep in sleeps) {
            def json = JsonOutput.toJson(sleep.forUpload())
            // send a POST request to the central repository with json as the payload
            // sleep.uploaded = true
            // sleepRecordService.update(sleep)
        }
    }
}
