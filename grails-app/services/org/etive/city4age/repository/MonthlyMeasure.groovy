package org.etive.city4age.repository

class MonthlyMeasure {

    String startDate
    Integer gpVisitsMonth
    Integer seniorCenterVisitsMonth
    CareReceiver careReceiver

    def formatForUpload() {
        return [
                user: careReceiver.city4AgeId,
                pilot: "BHX",
                interval_start: startDate,
                duration: "MON",
                payload: [
                        GP_VISITS_MONTH: [
                                value: gpVisitsMonth,
                                data_source_type: [ "sensors" ]
                        ],
                        SENIORCENTER_VISITS_MONTH: [
                                value: seniorCenterVisitsMonth,
                                data_source_type: [ "sensors" ]
                        ]
                ]
        ]
    }
}


