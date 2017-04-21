package city4age.api

class CareReceiver {

    String emailAddress
    String token
    Long personId
    String city4ageId

    static hasMany = [events: Event]

    static constraints = {
        emailAddress blank: false, nullable: false
        token nullable: true
        events nullable: true
        personId nullable: true
        city4ageId nullable: true
    }
}