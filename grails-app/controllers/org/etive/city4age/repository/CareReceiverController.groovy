package org.etive.city4age.repository
//import org.springframework.beans.factory.annotation.Value

class CareReceiverController {
    def careReceiverService
    def activityRecordService
    def sleepRecordService

    private final String dlbServer = System.getenv("DLB_SERVER")
//    private final String dlbServer = "192.168.1.130"

    private final String localRepository = System.getenv("REPOSITORY_KEY")

    def index() {
        // Remove for production
        respond careReceiverService.listCareReceivers()
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
                def data = receiver.fetchWithingsData(CareReceiver.getStartDate(), new Date() - 1)

                def activities = activityRecordService.bulkCreate(data.activity)
                if (activities) receiver.activityDownloadDate = activities.last().date

                def sleeps = sleepRecordService.bulkCreate(data.sleep)
                if (sleeps) receiver.sleepDownloadDate = sleeps.last().date

                receiver = careReceiverService.updateCareReceiver(receiver)
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



