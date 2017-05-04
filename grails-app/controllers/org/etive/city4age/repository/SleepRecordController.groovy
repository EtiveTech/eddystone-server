package org.etive.city4age.repository

class SleepRecordController {
    def sleepRecordService

    def index() {
        def receiver = CareReceiver.findById(params.receiverId)
        def list = sleepRecordService.listSleepRecords(receiver)
        respond(list, status: 200)
    }
}
