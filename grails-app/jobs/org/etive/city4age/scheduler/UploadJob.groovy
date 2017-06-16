package org.etive.city4age.scheduler

import org.etive.city4age.repository.CentralRepositorySession

class UploadJob {

    def activityRecordService
    def sleepRecordService
    def poiEventService
    def careReceiverService

    private final String centralRepository = System.getenv("CENTRAL_ADDRESS")

    static triggers = {
//        cron name: 'uploadTrigger', cronExpression: "0 30 3 * * ?"
        cron name: 'uploadTrigger', cronExpression: "0 27 * * * ?"
    }

    def execute() {
        // execute job

        if (!centralRepository) return

        def session = new CentralRepositorySession()

        // LOGIN
        def loggedIn = session.login()

        if (!loggedIn) return

        // Check that all Care Receivers have a City4AgeId
        def careReceivers = careReceiverService.listCareReceivers()
        for (def careReceiver in careReceivers) {
            if (!careReceiver.hasCity4AgeId()) {
                // Create a City4AgeId for this user
                def city4AgeId = session.getCity4AgeId(careReceiver.logbookId, careReceiver.getDataDate() as Date)
                if (city4AgeId) {
                    careReceiver.city4AgeId = city4AgeId
                    careReceiverService.persistChanges(careReceiver)
                }
            }
        }

        // for all CareReceivers copy Activity/Sleep measures and POI events up to the Central Repository.

        def activities = activityRecordService.readyForUpload()
        for (def activity in activities) {
            if ((activity.careReceiver.hasCity4AgeId()) && !activity.careReceiver.forTest) {
                if (session.sendMeasure(activity.formatForUpload())) {
                    activity.uploaded = true
                    activityRecordService.persistChanges(activity)
                }
            }
        }

        def sleeps = sleepRecordService.readyForUpload()
        for (def sleep in sleeps) {
            if ((sleep.careReceiver.hasCity4AgeId()) && !sleep.careReceiver.forTest) {
                if (session.sendMeasure(sleep.formatForUpload())) {
                    sleep.uploaded = true
                    sleepRecordService.persistChanges(sleep)
                }
            }
        }

        def events = poiEventService.readyForUpload()
        for (def event in events) {
            if ((event.careReceiver.hasCity4AgeId()) && !event.careReceiver.forTest) {
                if (session.sendAction(event.formatForUpload())) {
                    event.uploaded = true
                    poiEventService.persistChanges(event)
                }
            }
        }

        // LOGOUT
        session.logout()
    }
}
