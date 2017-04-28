package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class CareReceiverService {

    def createCareReceiver(json) {

        def logbookId = json.logbookId as Long
        def receiver = CareReceiver.findByLogbookId(logbookId)
        if (receiver) return null

        // Should maybe get the City4Age Id now and save it with the rest of the info

        def token = null
        while (!token) {
            token = UUID.randomUUID().toString()
            if (CareReceiver.findByToken(token)) token = null
        }

        receiver = new CareReceiver(
                logbookId: logbookId,
                withingsId: json.withingsId as Long,
                emailAddress: json.emailAddress as String,
                accessKey: json.accessKey as String,
                accessSecret: json.accessSecret as String,
                token: token
        )
        receiver.save()
        return receiver
    }
}
