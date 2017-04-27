package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class ActivityRecordService {

    def createActivityRecord(item) {
        def dataMap = item.toMap()
        dataMap.date = Date.parse("yyyy-MM-dd", dataMap.date)
        def activityRecord = new ActivityRecord(dataMap)
        activityRecord.save()
        return activityRecord
    }
}
