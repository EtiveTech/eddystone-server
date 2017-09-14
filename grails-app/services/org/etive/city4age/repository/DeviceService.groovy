package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class DeviceService {

    def createDevice(careReceiver, json) {
        def device = Device.findByUniqueId(json.uuid as String)
        if (device) {
            device.careReceiver = careReceiver
            device.osVersion = json.osVersion as String
            device.lastContact = new Date(json.timestamp as Long)
        } else {
            device = new Device(
                    operatingSystem: json.os,
                    osVersion: json.osVersion,
                    model: json.model,
                    uniqueId: json.uuid,
                    careReceiver: careReceiver,
                    lastContact: new Date(json.timestamp as Long)
            )
        }
        try {
            device = device.save()
        }
        catch (Exception e) {
            log.error(e.message)
            device = null
        }

        return device
    }

    def updateLastContact(Device device, Long timestamp) {
        device.lastContact = new Date(timestamp)
        try {
            device = device.save()
        }
        catch (Exception e) {
            log.error(e.message)
            device = null
        }
        return device
    }

    @Transactional(readOnly = true)
    def listDevices() {
        return Device.list()
    }
}
