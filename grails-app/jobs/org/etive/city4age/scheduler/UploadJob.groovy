package org.etive.city4age.scheduler

import grails.converters.JSON
import grails.util.Environment
import org.etive.city4age.repository.CentralRepositorySession

class UploadJob {

    def activityRecordService
    def sleepRecordService
    def poiEventService
    def careReceiverService
    def weeklyMeasureService
    def monthlyMeasureService

    private final String centralRepository = System.getenv("CENTRAL_ADDRESS")

    static triggers = {
        cron name: 'uploadTrigger', cronExpression: "0 30 3 * * ?"
//        cron name: 'uploadTrigger', cronExpression: "0 55 10 * * ?"
    }

    def execute() {
        // execute job

        if (!centralRepository){
            log.error("The address of the the Central Repository has not been set")
            return
        }

        def session = new CentralRepositorySession()

        // LOGIN
        def response = session.login()
        if (response != 200){
            log.error("Failed to login to the Central Repository (status = " + response + ")")
            return
        }

        log.info("Logged into the Central Repository")

        // Check that all Care Receivers have a City4AgeId
        def careReceivers = careReceiverService.listCareReceivers()
        log.info("Uploading data for " + careReceivers.size() + " Care Receivers")
        for (def careReceiver in careReceivers) {
            if (careReceiver.uploadable() && !careReceiver.hasCity4AgeId()) {
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

        def errorCount = 0

        def activities = activityRecordService.readyForUpload()
        if (activities) {
            log.info("Attempting to upload " + activities.size() + " Activity Measures")
            def count = 0
            for (def activity in activities) {
                if (activity.careReceiver.uploadable() && activity.careReceiver.hasCity4AgeId()) {
                    if (session.sendMeasure(activity.formatForUpload())) {
                        count += 1
                    }
                    else {
                        log.error("Unable to upload activity record with id " + activity.id)
                        errorCount += 1
                    }
                }
            }
            log.info("Uploaded " + count + " Activity Measures")
        }
        else {
            log.info("There are no Activity Measures to upload")
        }

        // Upload weekly measures - Pharmacy, Supermarket, Shops, Restaurants

        def careReceiversWeekly = careReceiverService.listCareReceivers()
            log.info("Attempting to upload Weekly Measures")
            def weeklyUploadCount = 0
            for (def careReceiver in careReceiversWeekly) {
                log.info("******** Upload weekly measure for care receiver " + careReceiver.city4AgeId + " ***********")
                if (careReceiver.uploadable() && careReceiver.hasCity4AgeId()) {

                    def weeklyMeasure = weeklyMeasureService.createWeeklyMeasure(careReceiver)

                    log.info("Attempting to upload Weekly Measures for " + careReceiver.city4AgeId)

                    if (session.sendMeasure(weeklyMeasure.formatForUpload())) {
                        weeklyUploadCount += 1
                    } else {
                        log.error("Unable to upload weekly measure for " + careReceiver.city4AgeId)
                        errorCount += 1
                    }

                }
                log.info("Uploaded " + weeklyUploadCount + " Weekly Measures")
                log.info("************ finished weekly measure upload **************")
            }


        // Upload monthly measures - GP visits, seniorCenter

        def careReceiversMonthly = careReceiverService.listCareReceivers()
        log.info("Attempting to upload Monthly Measures")
        def monthlyUploadCount = 0
        for (def careReceiver in careReceiversMonthly) {
            log.info("******** Upload monthly measure for care receiver " + careReceiver.city4AgeId + " ***********")
            if (careReceiver.uploadable() && careReceiver.hasCity4AgeId()) {

                def monthlyMeasure = monthlyMeasureService.createMonthlyMeasure(careReceiver)

                log.info("Attempting to upload monthly Measures for " + careReceiver.city4AgeId)

                if (session.sendMeasure(monthlyMeasure.formatForUpload())) {
                    monthlyUploadCount += 1
                } else {
                    log.error("Unable to upload monthly measure for " + careReceiver.city4AgeId)
                    errorCount += 1
                }
            }
            log.info("Uploaded " + monthlyUploadCount + " Monthly Measures")
            log.info("************ finished monthly measure upload **************")
        }

        // Upload sleep measures
        def sleeps = sleepRecordService.readyForUpload()
        if (sleeps) {
            log.info("Attempting to upload " + sleeps.size() + " Sleep Measures")
            def count = 0
            for (def sleep in sleeps) {
                if (sleep.careReceiver.uploadable() && sleep.careReceiver.hasCity4AgeId()) {
                    if (session.sendMeasure(sleep.formatForUpload())) {
                        sleep.uploaded = true
                        sleepRecordService.persistChanges(sleep)
                        count += 1
                    }
                    else {
                        log.error("Unable to upload sleep record with id " + sleep.id)
                        errorCount += 1
                    }
                }
            }
            log.info("Uploaded " + count + " Sleep Measures")
        }
        else {
            log.info("There are no Sleep Measures to upload")
        }


        // Upload events
        def events = poiEventService.readyForUpload()
        if (events) {
            log.info("Attempting to upload " + events.size() + " POI Events")
            def count = 0
            for (def event in events) {
                if (event.careReceiver.uploadable() && event.careReceiver.hasCity4AgeId()) {
                    if (session.sendAction(event.formatForUpload())) {
                        event.uploaded = true
                        poiEventService.persistChanges(event)
                        count += 1
                    }
                    else {
                        log.error("Unable to upload POI event with id " + event.id)
                        errorCount += 1
                    }
                }
            }
            log.info("Uploaded " + count + " POI Events")
        }
        else {
            log.info("There are no POI Events to upload")
        }

        // COMMIT the uploaded data if
        // first day of month

        //@todo do we commit if any errors in uploads, ie errorCount > 0

        def today = Calendar.getInstance();
        if (today.get(Calendar.DATE) == today.getActualMinimum(Calendar.DATE)) {
            if (session.commit())
                log.info("Commit of uploaded data completed")
            else
                log.error("Commit of uploaded data failed")
        }

        // LOGOUT
        session.logout()
        log.info("Logged out of the Central Repository")
    }
}
