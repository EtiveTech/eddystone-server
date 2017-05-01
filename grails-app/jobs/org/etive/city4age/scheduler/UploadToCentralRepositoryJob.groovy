package org.etive.city4age.scheduler

import groovy.json.JsonOutput

class UploadToCentralRepositoryJob {

    def activityRecordService
    def sleepRecordService

    static triggers = {
        cron name: 'uploadTrigger', cronExpression: "0 0 * * * ?"
    }

    def execute() {
        // execute job
        // for all CareReceivers copy Activity/Sleep measures and POI events up to the Central Repository.

        def activities = activityRecordService.forUpload()
        for (activity in activities) {
            def json = JsonOutput.toJson(activity.forUpload())
        }
        def sleeps = sleepRecordService.forUpload()
        for (sleep in sleeps) {
            def json = JsonOutput.toJson(sleep.forUpload())
        }
    }
}
