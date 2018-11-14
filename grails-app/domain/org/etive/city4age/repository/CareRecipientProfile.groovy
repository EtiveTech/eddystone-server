package org.etive.city4age.repository

class CareRecipientProfile {
    String careReceiver
    String dob
    String sex
    String livingStatus
    String occupation
    String previousOccupation
    String bloodPressure
    String height
    String weight

    static constraints = {
        careReceiver nullable: true
        dob nullable: true
        sex nullable: true
        livingStatus nullable: true
        occupation nullable: true
        previousOccupation nullable: true
        bloodPressure nullable: true
        height nullable: true
        weight nullable: true
    }

    static Long getId(careRecipientData) {
        if (!careRecipientData) return null
        return careRecipientData.id
    }
}
