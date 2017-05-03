package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class ActivityRecordService {

    def createActivityRecord(item) {
        def activityRecord = new ActivityRecord(item.toMap())
        return activityRecord.save()
    }

    def bulkCreate(items) {
        // persist a large number of ActivityRecords
        def activityRecords = []
        for (item in items) {
            def activityRecord = createActivityRecord(item)
            if (activityRecord) activityRecords << activityRecord
        }
        return activityRecords
    }

    @Transactional(readOnly = true)
    def listActivityRecords() {
        return ActivityRecord.list()
    }

    @Transactional(readOnly = true)
    def readyForUpload() {
        def query = ActivityRecord.where { uploaded == false }
        return query.list()
    }

    def persistChanges(activityRecord) {
        return activityRecord.save()
    }

}
