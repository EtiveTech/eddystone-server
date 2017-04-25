package org.etive.city4age.repository

class CareReceiver {

    String emailAddress
    String token
    Long logbookId
    String city4ageId
    Long withingsId
    String accessKey
    String accessSecret
    Date dateCreated
    Date lastUpdated

    static hasMany = [events: Event]

    static constraints = {
        emailAddress blank: false, nullable: false
        token nullable: true
        events nullable: true
        logbookId nullable: false
        city4ageId blank: false, nullable: true
        withingsId nullable: false
        accessKey blank: false, nullable: false
        accessSecret blank: false, nullable: false
    }
}