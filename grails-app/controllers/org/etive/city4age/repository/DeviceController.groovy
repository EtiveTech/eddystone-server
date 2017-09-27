package org.etive.city4age.repository

class DeviceController {
    def deviceService
	
    def index() {
        respond deviceService.listDevices()
    }

    def save() {
        def json = request.JSON
        def careReceiver = CareReceiver.findByToken(json.token as String)
        if (careReceiver) {
            def device = deviceService.createDevice(careReceiver, json)
            if (device)
                respond(device, status: 201)
            else
                response.sendError(409, "")
        }
        else
            response.sendError(403, "")
    }

    def update() {
        def json = request.JSON
        def careReceiver = CareReceiver.findByToken(json.token as String)
        if (careReceiver) {
            def device = Device.findByUniqueId(params.uuid as String)
            if (device && device.careReceiver == careReceiver) {
                device = deviceService.updateLastContact(params.uuid as String, json.timestamp as Long)
                respond(device, status: 201)
            }
            else
                response.sendError(404, "")
        }
        else
            response.sendError(403, "")
    }
}
