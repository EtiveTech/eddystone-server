package city4age.api

import grails.rest.*
import grails.converters.*

class DeviceController {
	
    def index() {
        respond Device.list()
    }

    def save() {
        def json = request.JSON
        def careReceiver = CareReceiver.findByToken(json.token)
        if (careReceiver) {
            def device = Device.findByUniqueID(json.uuid)
            if (device) {
                device.careReceiver = careReceiver
                device.osVersion = json.osVersion
                device.lastContact = new Date(Long.valueOf(json.timestamp))
            }
            else {
                device = new Device(
                        operatingSystem: json.os,
                        osVersion: json.osVersion,
                        model: json.model,
                        uniqueID: json.uuid,
                        careReceiver: careReceiver,
                        lastContact: new Date(Long.valueOf(json.timestamp))
                )
            }
            device.save()
            respond(device, status: 201)
        }
        else {
            response.sendError(403, "")
        }
    }

    def update() {
        def json = request.JSON
        def careReceiver = CareReceiver.findByToken(json.token)
        if (careReceiver) {
            def device = Device.findByUuid(params.uuid.toString())
            if (device) {
                device.lastContact = new Date(Long.valueOf(json.timestamp))
                device.save()
            }
            else {
                response.sendError(403, "")
            }
        }
        else {
            response.sendError(404, "")
        }
    }
}
