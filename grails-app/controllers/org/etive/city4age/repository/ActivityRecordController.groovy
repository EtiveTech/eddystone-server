package org.etive.city4age.repository

class ActivityRecordController {
    def activityRecordService

    def index() {
        respond activityRecordService.listActivityRecords()
    }
}
