package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class MonthlyMeasureService {
    def poiEventService

//     Creates a Measure object for one care receiver with the full history
    MonthlyMeasure createMonthlyMeasure(CareReceiver careReceiver) {
        Date finish = poiEventService.getTheDateOfTheFirstOfTheMonth()
        Date start = poiEventService.getTheDateOfTheFirstOfLastMonth(finish)
        String startDateString = start.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("Europe/London")).toString()
        Integer gpVisits = poiEventService.filterPoiEvents(careReceiver, "GP", start, finish)
        Integer seniorCenterVisits = poiEventService.filterPoiEvents(careReceiver, "SeniorCenter", start, finish)

        def monthlyMeasure = new MonthlyMeasure(
                startDate: startDateString,
                careReceiver: careReceiver,
                gpVisitsMonth: gpVisits,
                seniorCenterVisitsMonth: seniorCenterVisits
        )
        return monthlyMeasure
    }

    @Transactional(readOnly = true)
    def listMonthlyMeasures(CareReceiver receiver) {
        def list = createMonthlyMeasure(receiver)
        return list
    }


}