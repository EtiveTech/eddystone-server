package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class CareReceiverService {

    def createCareReceiver(json) {

        def logbookId = Long.valueOf(json.logbookId.toString())
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
                withingsId: Long.valueOf(json.withingsId.toString()),
                emailAddress: json.emailAddress.toString(),
                accessKey: json.accessKey.toString(),
                accessSecret: json.accessSecret.toString(),
                token: token
        )
        receiver.save()
        return receiver
    }
}
