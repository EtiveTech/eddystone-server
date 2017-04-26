package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class SleepRecordService {

    def createSleepRecord(dataMap, careReceiver) {
        dataMap.careReceiver = careReceiver
        def sleepRecord = new SleepRecord(dataMap)
        sleepRecord.save()
        return sleepRecord
    }
}
