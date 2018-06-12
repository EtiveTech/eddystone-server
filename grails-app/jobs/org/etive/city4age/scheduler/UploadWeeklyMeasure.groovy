package org.etive.city4age.scheduler

import org.etive.city4age.repository.CentralRepositorySession

class UploadWeeklyMeasure {

    def activityRecordService


    private final String centralRepository = System.getenv("CENTRAL_ADDRESS")

    static triggers = {
        cron name: 'uploadWeeklyTrigger', cronExpression: "0 45 2 * * MON"
    }


}
