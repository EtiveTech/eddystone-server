package org.etive.city4age.scheduler

import grails.util.Environment
import org.etive.city4age.repository.CentralRepositorySession

class UploadJob {

    def activityRecordService
    def sleepRecordService
    def poiEventService
    def careReceiverService

    private final String centralRepository = System.getenv("CENTRAL_ADDRESS")

    static triggers = {
        cron name: 'uploadTrigger', cronExpression: "0 30 3 * * ?"
//        cron name: 'uploadTrigger', cronExpression: "0 21 * * * ?"
    }

    def uploadable(careReceiver) {
        return (Environment.current != Environment.PRODUCTION) ? true : !careReceiver.forTest
    }

    def execute() {
        // execute job

        if (!centralRepository){
            log.error("The address of the the Central Repository has not been set")
            return
        }

        def session = new CentralRepositorySession()

        // LOGIN
        def loggedIn = session.login()

        if (!loggedIn){
            log.error("Failed to login to the Central Repository")
            return
        }

        log.info("Logged into the Central Repository")

        // Check that all Care Receivers have a City4AgeId
        def careReceivers = careReceiverService.listCareReceivers()
        log.info("Uploading data for " + careReceivers.size() + " Care Receivers")
        for (def careReceiver in careReceivers) {
            if (!careReceiver.hasCity4AgeId()) {
                // Create a City4AgeId for this user
                def city4AgeId = session.getCity4AgeId(careReceiver.logbookId, careReceiver.getDataDate() as Date)
                if (city4AgeId) {
                    log.info("Obtained City4AgeId " + city4AgeId + " for Care Receiver with id " + careReceiver.id)
                    careReceiver.city4AgeId = city4AgeId
                    careReceiverService.persistChanges(careReceiver)
                }
                else {
                    log.error("Failed to get City4AgeId for Care Receiver with id " + careReceiver.id)
                }
            }
        }

        // for all CareReceivers copy Activity/Sleep measures and POI events up to the Central Repository.

        def activities = activityRecordService.readyForUpload()
        if (activities) {
            log.info("Attempting to upload " + activities.size() + " Activity Measures")
            def count = 0
            for (def activity in activities) {
                if ((activity.careReceiver.hasCity4AgeId()) && uploadable(activity.careReceiver)) {
                    if (session.sendMeasure(activity.formatForUpload())) {
                        activity.uploaded = true
                        activityRecordService.persistChanges(activity)
                        count += 1
                    }
                    else {
                        log.error("Unable to upload activity record with id " + activity.id)
                    }
                }
            }
            log.info("Uploaded " + count + " Activity Measures")
        }
        else {
            log.info("There are no Activity Measures to upload")
        }


        def sleeps = sleepRecordService.readyForUpload()
        if (sleeps) {
            log.info("Attempting to upload " + sleeps.size() + " Sleep Measures")
            def count = 0
            for (def sleep in sleeps) {
                if ((sleep.careReceiver.hasCity4AgeId()) && uploadable(sleep.careReceiver)) {
                    if (session.sendMeasure(sleep.formatForUpload())) {
                        sleep.uploaded = true
                        sleepRecordService.persistChanges(sleep)
                        count += 1
                    }
                    else {
                        log.error("Unable to upload sleep record with id " + sleep.id)
                    }
                }
            }
            log.info("Uploaded " + count + " Sleep Measures")
        }
        else {
            log.info("There are no Sleep Measures to upload")
        }


        def events = poiEventService.readyForUpload()
        if (events) {
            log.info("Attempting to upload " + events.size() + " POI Events")
            def count = 0
            for (def event in events) {
                if ((event.careReceiver.hasCity4AgeId()) && uploadable(event.careReceiver)) {
                    if (session.sendAction(event.formatForUpload())) {
                        event.uploaded = true
                        poiEventService.persistChanges(event)
                        count += 1
                    }
                    else {
                        log.error("Unable to upload POI event with id " + event.id)
                    }
                }
            }
            log.info("Uploaded " + count + " POI Events")
        }
        else {
            log.info("There are no POI Events to upload")
        }


        // LOGOUT
        session.logout()
        log.info("Logged out of the Central Repository")
    }
}
