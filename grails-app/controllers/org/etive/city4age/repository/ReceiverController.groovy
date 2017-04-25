package org.etive.city4age.repository

//import org.springframework.beans.factory.annotation.Value

class ReceiverController {

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
            if (receiver) {
                if (!receiver.token) {
                    def token = null
                    while (!token) {
                        token = UUID.randomUUID().toString()
                        if (CareReceiver.findByToken(token)) token = null
                    }
                    receiver.token = token
                    receiver.save(flush: true)
                }
                respond receiver
            }
            else {
                response.sendError(404, "Care Receiver not found")
            }
        }
        else {
            response.sendError(403, "Forbidden")
        }
    }

    def save() {
        if (request.getRemoteAddr() == dlbServer) {
            def json = request.JSON
            def logbookId = Long.valueOf(json.logbookId.toString())
            def receiver = CareReceiver.findByLogbookId(logbookId)
            if (!receiver) {
                // Should get the City4Age Id now and save it with the rest of the info
                receiver = new CareReceiver(
                        logbookId: logbookId,
                        withingsId: Long.valueOf(json.withingsId.toString()),
                        emailAddress: json.emailAddress.toString(),
                        accessKey: json.accessKey.toString(),
                        accessSecret: json.accessSecret.toString()
                )
                receiver.save()
                respond(receiver, status: 201)
            }
            else {
                response.sendError(409, "Entry already exists")
            }
        }
        else {
            response.sendError(403, "Forbidden")
        }
    }
}



