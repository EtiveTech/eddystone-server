package city4age.api

class DeviceController {
	
    def index() {
        respond Device.list()
    }

    def save() {
        def json = request.JSON
        def careReceiver = CareReceiver.findByToken(json.token.toString())
        if (careReceiver) {
            def device = Device.findByUniqueId(json.uuid.toString())
            if (device) {
                device.careReceiver = careReceiver
                device.osVersion = json.osVersion.toString()
                device.lastContact = new Date(Long.valueOf(json.timestamp.toString()))
            }
            else {
                device = new Device(
                        operatingSystem: json.os,
                        osVersion: json.osVersion,
                        model: json.model,
                        uniqueId: json.uuid,
                        careReceiver: careReceiver,
                        lastContact: new Date(Long.valueOf(json.timestamp.toString()))
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
        def careReceiver = CareReceiver.findByToken(json.token.toString())
        if (careReceiver) {
            def device = Device.findByUniqueId(params.uuid.toString())
            if (device) {
                device.lastContact = new Date(Long.valueOf(json.timestamp.toString()))
                device.save()
                respond(device, status: 201)
            }
            else {
                response.sendError(404, "")
            }
        }
        else {
            response.sendError(403, "")
        }
    }
}
