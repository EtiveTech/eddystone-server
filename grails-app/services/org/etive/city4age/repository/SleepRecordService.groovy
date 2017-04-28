package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class SleepRecordService {

    def createSleepRecord(item) {
        def sleepRecord = new SleepRecord(item.toMap())
        sleepRecord.save()
        return sleepRecord
    }

    def bulkCreate(items) {
        // persist a large number of ActivityRecords
        def sleepRecords = []
        for (item in items) {
            sleepRecords << createSleepRecord(item)
        }
        return sleepRecords
    }
}
