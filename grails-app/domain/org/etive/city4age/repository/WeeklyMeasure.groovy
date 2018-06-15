package org.etive.city4age.repository

class WeeklyMeasure {
    Date timestamp
    Boolean uploaded = false
    Date startDate
    Integer pharmacyVisitsWeek
    Integer supermarketVisitsWeek
    Integer shopVisitsWeek
    Integer restaurantVisitsWeek
    CareReceiver careReceiver


    static constraints = {

        careReceiver nullable: false
        startDate nullable: false
        uploaded nullable: false
        pharmacyVisitsWeek nullable: false
        supermarketVisitsWeek nullable: false
        shopVisitsWeek nullable: false
        restaurantVisitsWeek nullable: false
    }


    def formatForUpload() {
        return [
                user: careReceiver.city4AgeId,
                pilot: "BHX",
                interval_start: startDate.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("Europe/London")),
                duration: "1WK",
                payload: [
                        PHARMACY_VISITS_WEEK: [
                            value: pharmacyVisitsWeek,
                            data_source_type: [ "sensors" ]
                ],
                        SUPERMARKET_VISITS_WEEK: [
                                value: supermarketVisitsWeek,
                                data_source_type: [ "sensors" ]
                        ],
                        SHOPS_VISITS_WEEK: [
                                value: shopVisitsWeek,
                                data_source_type: [ "sensors" ]
                        ],
                        RESTAURANTS_VISITS_WEEK: [
                                value: restaurantVisitsWeek,
                                data_source_type: [ "sensors" ]
                        ],
                ]
        ]
    }
}
