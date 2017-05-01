package org.etive.city4age

class GenerateEventsJob {
    static triggers = {
        cron name: 'withingsTrigger', cronExpression: "0 0 1 * * ?"
    }

    def execute() {
        // execute job
    }
}
