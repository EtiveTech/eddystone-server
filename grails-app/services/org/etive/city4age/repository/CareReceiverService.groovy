package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class CareReceiverService {

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
        careReceiver.save()
        return careReceiver
    }

    def persistChanges(careReceiver) {
        return careReceiver.save()
    }

}
