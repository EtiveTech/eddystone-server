package org.etive.city4age.repository


class CareRecipientDataController {

    def show() {
        def careRecipientData = CareRecipientData.findByCareReceiverId(params.receiverId as String)
        if (careRecipientData)
            respond (careRecipientData, [status: 200 ])
        else
            response.sendError(404, "Care Receiver not found")
    }


}
