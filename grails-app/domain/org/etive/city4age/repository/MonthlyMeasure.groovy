package org.etive.city4age.repository

class MonthlyMeasure {

    Boolean uploaded = false
    Date startDate
    Integer gpVisitsMonth
    Integer seniorCenterVisitsMonth
    CareReceiver careReceiver

    static constraints = {

        careReceiver nullable: false
        startDate nullable: false
        uploaded nullable: false
        gpVisitsMonth nullable: false
        seniorCenterVisitsMonth nullable: false
    }


    def formatForUpload() {
        return [
                user: careReceiver.city4AgeId,
                pilot: "BHX",
                interval_start: startDate.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("Europe/London")),
                duration: "1M",
                payload: [
                        GP_VISITS_MONTH: [
                            value: gpVisitsMonth,
                            data_source_type: [ "sensors" ]
                ],
                        SENIORCENTER_VISITS_MONTH: [
                                value: seniorCenterVisitsMonth,
                                data_source_type: [ "sensors" ]
                        ],
                ]
        ]
    }
}
