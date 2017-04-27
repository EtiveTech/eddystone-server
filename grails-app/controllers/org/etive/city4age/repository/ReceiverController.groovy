package org.etive.city4age.repository

import org.etive.city4age.withings.WithingsService

//import org.springframework.beans.factory.annotation.Value

class ReceiverController {
    def careReceiverService
    def activityRecordService

//    @Value('${dlb.server}')
    private final String dlbServer = System.getenv("DLB_SERVER")

//    @Value('${repository.key}')
    private final String localRepository = System.getenv("REPOSITORY_KEY")

    def index() {
        // Remove for production
        respond CareReceiver.list()
    }

    def show() {
        if (params.key == localRepository) {
            def receiver = CareReceiver.findByEmailAddress(params.email.toString())
            if (receiver)
                respond(receiver, status: 200)
            else
                response.sendError(404, "Care Receiver not found")
        }
        else {
            response.sendError(403, "Forbidden")
        }
    }

    def save() {
        if (request.getRemoteAddr() == dlbServer) {
            def receiver = careReceiverService.createCareReceiver(request.JSON)
            if (receiver) {
                respond(receiver, status: 201)
                def ws = new WithingsService()
                def data = ws.getActivityData(receiver, (new Date()) - 90, new Date())
                for (datum in data) {
                    activityRecordService.createActivityRecord(datum)
                }
                def sleep = ws.getSleepData(receiver, (new Date()) - 90, (new Date()) - 1)
            }
            else
                response.sendError(409, "")
        }
        else {
            response.sendError(403, "Forbidden")
        }
    }
}



