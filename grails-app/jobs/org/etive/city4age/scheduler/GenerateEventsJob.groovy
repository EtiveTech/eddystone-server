package org.etive.city4age.scheduler

import org.etive.city4age.repository.PoiEventGenerator

class GenerateEventsJob {

    static triggers = {
        cron name: 'poiTrigger', cronExpression: "0 30 2 * * ?"
//        cron name: 'poiTrigger', cronExpression: "0 11 * * * ?"
    }

    def execute() {
        // execute job
        new PoiEventGenerator().generateEvents()
    }
}
