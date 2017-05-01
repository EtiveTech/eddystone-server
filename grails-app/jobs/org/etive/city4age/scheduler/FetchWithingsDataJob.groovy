package org.etive.city4age.scheduler

class FetchWithingsDataJob {
    static triggers = {
      cron name: 'withingsTrigger', cronExpression: "0 0 1 * * ?"
    }

    def execute() {
        // For all CareReceivers, grab their Withings data for the preceding day
        // Providing we don't have it yet
    }
}
