package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class SleepRecordService {

    def createSleepRecord(item) {
        def sleepRecord = new SleepRecord(item.toMap())
        return sleepRecord.save()
    }

    def bulkCreate(items) {
        // persist a large number of ActivityRecords
        def sleepRecords = []
        for (item in items) {
            def sleepRecord = createSleepRecord(item)
            if (sleepRecord) sleepRecords << sleepRecord
        }
        return sleepRecords
    }

    @Transactional(readOnly = true)
    def listSleepRecords() {
        return SleepRecord.list()
    }

    @Transactional(readOnly = true)
    def forUpload() {
        return SleepRecord.where { uploaded == false }
    }

    def updateSleepRecord(sleepRecord) {
        return sleepRecord.save()
    }
}
