package org.etive.city4age.repository

class CareRecipientProfileController {

    def index() { }

    def show() {
        def careRecipientProfile = CareRecipientProfile.findByCareReceiver(params.careReceiver as String)
        if (careRecipientProfile)
            respond (careRecipientProfile, [status: 200])
        else
            response.sendError(404, "Care Receiver not found")
    }
}
