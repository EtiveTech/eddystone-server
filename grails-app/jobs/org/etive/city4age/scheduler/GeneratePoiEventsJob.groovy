package org.etive.city4age.scheduler

class GenerateEventsJob {
    static triggers = {
        cron name: 'poiTrigger', cronExpression: "0 0 1 * * ?"
    }

    def execute() {
        // execute job
    }
}
