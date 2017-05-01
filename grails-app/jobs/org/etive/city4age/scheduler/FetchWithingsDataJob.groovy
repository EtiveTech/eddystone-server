package org.etive.city4age.scheduler

class FetchWithingsDataJob {

    def careReceiverService
    def activityRecordService
    def sleepRecordService

    static triggers = {
      cron name: 'withingsTrigger', cronExpression: "0 46 * * * ?"
    }

    def execute() {
        // For all CareReceivers, grab their Withings data for the preceding day
        // Providing we don't have it yet

        def careReceivers = careReceiverService.listCareReceivers()
         for (receiver in careReceivers) {
            def data = receiver.updateWithingsData(new Date() - 1)

            def activities = activityRecordService.bulkCreate(data.activity)
            if (activities) receiver.activityDownloadDate = activities.last().date

            def sleeps = sleepRecordService.bulkCreate(data.sleep)
            if (sleeps) receiver.sleepDownloadDate = sleeps.last().date

            careReceiverService.updateCareReceiver(receiver)
        }
    }
}
