package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class SleepRecordService {

    def createSleepRecord(item) {
        def sleepRecord = new SleepRecord(item.toMap())
        sleepRecord.save()
        return sleepRecord
    }
}
