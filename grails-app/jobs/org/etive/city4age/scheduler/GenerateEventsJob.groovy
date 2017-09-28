package org.etive.city4age.scheduler

//import org.etive.city4age.repository.PoiEventGenerator

class GenerateEventsJob {
    def careReceiverService

    static triggers = {
        cron name: 'poiTrigger', cronExpression: "0 30 2 * * ?"
//        cron name: 'poiTrigger', cronExpression: "0 11 * * * ?"
    }

    def execute() {
        // execute job
        def careReceivers = careReceiverService.listCareReceivers()
        def today = (new Date()).clearTime()
        def yesterday = today - 1
        for (receiver in careReceivers) {
            log.info("Generating POI events for " + careReceivers.size() + " Care Receivers")
            careReceiverService.generatePoiEvents(receiver, yesterday)
        }
    }
}
