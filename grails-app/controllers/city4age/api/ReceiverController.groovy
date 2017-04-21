package city4age.api

class ReceiverController {

    def index() {
        // Remove for production
        respond CareReceiver.list()
    }

    def show() {
        if (params.key == Keys.localRepository) {
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
}
