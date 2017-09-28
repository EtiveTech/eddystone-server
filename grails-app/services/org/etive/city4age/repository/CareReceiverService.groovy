package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class CareReceiverService {
    def proximityEventService
    def poiEventService

    @Transactional(readOnly = true)
    def listCareReceivers() {
        return CareReceiver.list()
    }

    def createCareReceiver(json) {

        def logbookId = json.logbookId as Long
        def careReceiver = CareReceiver.findByLogbookId(logbookId)
        if (careReceiver) return null

        // Should maybe get the City4Age Id now and save it with the rest of the info

        def token = null
        while (!token) {
            token = UUID.randomUUID().toString()
            if (CareReceiver.findByToken(token)) token = null
        }

        careReceiver = new CareReceiver(
                logbookId: logbookId,
                withingsId: json.withingsId as Long,
                emailAddress: json.emailAddress as String,
                accessKey: json.accessKey as String,
                accessSecret: json.accessSecret as String,
                token: token,
                forTest: (json.forTest) ? json.forTest : false
        )
        if (json.city4ageId) careReceiver.city4AgeId = json.city4ageId
        try {
            careReceiver = careReceiver.save()
        }
        catch (Exception e) {
            log.error(e.message)
            careReceiver = null
        }

        return careReceiver
    }

    def persistChanges(careReceiver) {
        try {
            careReceiver = careReceiver.save()
        }
        catch (Exception e) {
            log.error(e.message)
            careReceiver = null
        }
        return careReceiver
    }

    def generatePoiEvents(careReceiver, endDate) {
        def startDate = careReceiver.eventsGenerated
        if (startDate) {
            startDate += 1  // Day after the last event was processed
        }
        else {
            def first = proximityEventService.firstProximityEvent(careReceiver)
            startDate = (first) ? first.timestamp : endDate
        }
        startDate.clearTime()   // The very start of the day
        if (startDate <= endDate) {
            def timestamp = poiEventService.generatePoiEvents(careReceiver, startDate, endDate)
            if (timestamp) {
                careReceiver.eventsGenerated = timestamp
                persistChanges(careReceiver)
            }
        }
    }
}
