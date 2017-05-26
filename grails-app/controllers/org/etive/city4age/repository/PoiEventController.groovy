package org.etive.city4age.repository

class PoiEventController {
    def poiEventService
	
    def index() {
        def receiver = CareReceiver.findById(params.receiverId)
        def list = poiEventService.listPoiEvents(receiver)
        respond(list, status: 200)
    }
}
