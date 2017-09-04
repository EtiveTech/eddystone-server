package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class ActivityRecordService {

    def createActivityRecord(item) {
        def activityRecord = new ActivityRecord(item.toMap())
        try {
            activityRecord = activityRecord.save()
        }
        catch (Exception e) {
            log.error(e.message)
            activityRecord = null
        }

        return activityRecord
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
    def listActivityRecords(CareReceiver receiver) {

        def query = (receiver) ? ActivityRecord.where{ careReceiver.id == receiver.id } : ActivityRecord
        return query.list(max: 500)
    }

    @Transactional(readOnly = true)
    def readyForUpload() {
        def query = ActivityRecord.where { uploaded == false }
        return query.list()
    }

    def persistChanges(activityRecord) {
        try {
            activityRecord = activityRecord.save()
        }
        catch (Exception e) {
            log.error(e.message)
            activityRecord = null
        }

        return activityRecord
    }

    @Transactional(readOnly = true)
    def firstActivityRecord(CareReceiver receiver) {
        def query = ActivityRecord.where{ careReceiver.id == receiver.id }
        def first = query.list(max: 1)
        return (first) ? first[0] : null
    }

}
