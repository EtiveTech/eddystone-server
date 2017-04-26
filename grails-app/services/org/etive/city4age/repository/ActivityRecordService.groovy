package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class ActivityRecordService {

    def createActivityRecord(dataMap, careReceiver) {
        dataMap.careReceiver = careReceiver
        def activityRecord = new ActivityRecord(dataMap)
        activityRecord.save()
        return activityRecord
    }
}
