package org.etive.city4age.scheduler

//import org.etive.city4age.repository.PoiEventGenerator

class GenerateEventsJob {
    def careReceiverService

    static triggers = {
//        cron name: 'poiTrigger', cronExpression: "0 30 2 * * ?"
        cron name: 'poiTrigger', cronExpression: "0 12 * * * ?"
    }

    def execute() {
        // execute job
        def careReceivers = careReceiverService.listCareReceivers()
        def today = (new Date()).clearTime()
        def yesterday = today - 1
        log.info("Generating POI events for " + careReceivers.size() + " Care Receivers")
        for (receiver in careReceivers) {
            careReceiverService.generatePoiEvents(receiver, yesterday)
        }
    }
}
