package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class MonthlyMeasureService {
    def poiEventService

//     Creates a Measure object for one care receiver with the full history
    def createMonthlyMeasure(CareReceiver careReceiver) {
        Date finish = poiEventService.getTheDateOfTheFirstOfTheMonth()
        Date start = poiEventService.getTheDateOfTheFirstOfLastMonth(finish)

        def monthlyMeasure = new WeeklyMeasure(
                startDate: start,
                uploaded: false,
                careReceiver: careReceiver,
                gpVisitsWeek: poiEventService.filterPoiEvents(careReceiver, "GP", start, finish),
                seniorcenterVisitsWeek: poiEventService.filterPoiEvents(careReceiver, "SeniorCenter", start, finish),

        )
        return monthlyMeasure
    }



    def persistChanges(monthlyMeasure) {
        try {
            monthlyMeasure = monthlyMeasure.save()
        }
        catch (Exception e) {
            log.error(e.message)
            monthlyMeasure = null
        }

        return monthlyMeasure
    }

}