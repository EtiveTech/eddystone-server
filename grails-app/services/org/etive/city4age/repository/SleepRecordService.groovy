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
    def listSleepRecords(CareReceiver receiver) {
        def query = (receiver) ? SleepRecord.where{ careReceiver.id == receiver.id } : SleepRecord
        return query.list(max: 500)
    }

    @Transactional(readOnly = true)
    def readyForUpload() {
        def query = SleepRecord.where{ uploaded == false }
        return query.list()
    }

    def persistChanges(sleepRecord) {
        return sleepRecord.save()
    }

    @Transactional(readOnly = true)
    def firstSleepRecord(CareReceiver receiver) {
        def query = SleepRecord.where{ careReceiver.id == receiver.id }
        def first = query.list(max: 1)
        return (first) ? first[0] : null
    }
}
