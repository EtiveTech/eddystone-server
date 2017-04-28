package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class ActivityRecordService {

    def createActivityRecord(item) {
        def activityRecord = new ActivityRecord(item.toMap())
        activityRecord.save()
        return activityRecord
    }
}
