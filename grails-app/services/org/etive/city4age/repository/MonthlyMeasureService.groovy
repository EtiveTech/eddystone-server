package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class MonthlyMeasureService {
    def poiEventService

//     Creates a Measure object for one care receiver with the full history
    def createMonthlyMeasure(CareReceiver careReceiver) {
        Date finish = poiEventService.getTheDateOfTheFirstOfTheMonth()
        Date start = poiEventService.getTheDateOfTheFirstOfLastMonth()
        Integer gpVisits = poiEventService.filterPoiEvents(careReceiver, "GP", start, finish)
        Integer seniorCenterVisits = poiEventService.filterPoiEvents(careReceiver, "SeniorCenter", start, finish)



        def monthlyMeasure = new MonthlyMeasure(
                startDate: start,
                uploaded: false,
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