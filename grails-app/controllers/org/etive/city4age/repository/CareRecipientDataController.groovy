package org.etive.city4age.repository

import grails.rest.*
import grails.converters.*

class CareRecipientDataController {
    static responseFormats = ['json', 'xml']

    def index() {}

    def show() {
        def careRecipientData = CareRecipientData.findByCareReceiver(params.careReceiver as String)
        if (careRecipientData) respond (careRecipientData, [status: 200 ])
        else
            response.sendError(404, "Care Receiver not found")
    }

}
