package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class SleepRecordService {

    def createSleepRecord(item) {
        def dataMap = item.toMap()
        dataMap.date = Date.parse("yyyy-MM-dd", dataMap.date)
        def sleepRecord = new SleepRecord(dataMap)
        sleepRecord.save()
        return sleepRecord
    }
}
