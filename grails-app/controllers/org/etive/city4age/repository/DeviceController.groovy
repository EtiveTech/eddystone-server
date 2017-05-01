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
        def device = deviceService.updateLastContact(request.JSON)
        if (device)
            respond(device, status: 201)
        else
            response.sendError(404, "")
    }
}
