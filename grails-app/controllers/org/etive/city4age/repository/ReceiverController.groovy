package org.etive.city4age.repository

import org.etive.city4age.withings.WithingsService

//import org.springframework.beans.factory.annotation.Value

class ReceiverController {
    def careReceiverService
    def activityRecordService
    def sleepRecordService

    private final String dlbServer = System.getenv("DLB_SERVER")
//    private final String dlbServer = "192.168.1.130"

    private final String localRepository = System.getenv("REPOSITORY_KEY")

    private final String projectStart = "01-01-2017"

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
                def data = receiver.fetchWithingsData(Date.parse("dd-MM-yyyy", projectStart), new Date() - 1)
                activityRecordService.bulkCreate(data.activity)
                sleepRecordService.bulkCreate(data.sleep)
                respond(receiver, status: 201)
            }
            else
                response.sendError(409, "")
        }
        else {
            response.sendError(403, "Forbidden")
        }
    }
}



