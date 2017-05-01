package org.etive.city4age.repository

class SleepRecordController {
    def sleepRecordService

    def index() {
        respond sleepRecordService.listSleepRecords()
    }
}
