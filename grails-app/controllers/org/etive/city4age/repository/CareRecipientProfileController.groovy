package org.etive.city4age.repository


import grails.rest.*
import grails.converters.*

class CareRecipientProfileController {
	static responseFormats = ['json', 'xml']
	
    def index() { }

    def show() {
        def careRecipientProfile = CareRecipientProfile.findByCareReceiver(params.careReceiver as String)
        if (careRecipientProfile)
            respond (careRecipientProfile, [status: 200])
        else
            response.sendError(404, "Care Receiver not found")
    }
}
