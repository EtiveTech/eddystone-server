package org.etive.city4age.repository

class WeeklyMeasure {
    String startDate
    Integer pharmacyVisitsWeek
    Integer supermarketVisitsWeek
    Integer shopVisitsWeek
    Integer restaurantVisitsWeek
    CareReceiver careReceiver

    def formatForUpload() {
        return [
                user: careReceiver.city4AgeId,
                pilot: "BHX",
                interval_start: startDate,
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
                        ]
                ]
        ]
    }
}
