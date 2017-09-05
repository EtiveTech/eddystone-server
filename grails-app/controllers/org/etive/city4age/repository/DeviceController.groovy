package org.etive.city4age.repository

class DeviceController {
    def deviceService
	
    def index() {
        respond deviceService.listDevices()
    }

    def save() {
        def device = deviceService.createDevice(request.JSON)
        if (device)
            respond(device, status: 201)
        else
            response.sendError(409, "")
    }

    def update() {
        def json = request.JSON
        def careReceiver = CareReceiver.findByToken(json.token as String)
        if (careReceiver) {
            def device = Device.findByUniqueId(json.uuid as String)
            if (device && device.careReceiver == careReceiver) {
                deviceService.updateLastContact(careReceiver, device, json)
                respond(device, status: 201)
            }
            else
                response.sendError(404, "")
        }
        else
            response.sendError(403, "")
    }
}
