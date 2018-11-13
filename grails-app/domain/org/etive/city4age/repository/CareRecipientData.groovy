package org.etive.city4age.repository

class CareRecipientData {
    String careReceiverId
    String dob
    String sex
    String livingStatus
    String occupation
    String previousOccupation
    String bloodPressure
    String height
    String weight



    static constraints = {
        careReceiverId nullable: true
        dob nullable: true
        sex nullable: true
        livingStatus nullable: true
        occupation nullable: true
        previousOccupation nullable: true
        bloodPressure nullable: true
        height nullable: true
        weight nullable: true
    }



}
