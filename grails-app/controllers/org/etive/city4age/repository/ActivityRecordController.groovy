package org.etive.city4age.repository

class ActivityRecordController {
    def activityRecordService

    def index() {
        def receiver = CareReceiver.findById(params.receiverId)
        def list = activityRecordService.listActivityRecords(receiver)
        respond(list, status: 200)
    }
}
