package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class WeeklyMeasureService {
    def poiEventService

//     Creates a Measure object for one care receiver with the full history
    def createWeeklyMeasure(CareReceiver careReceiver) {
        Date finish = poiEventService.getThisWeeksMondayDate()
        Date start = poiEventService.getDateLastWeekMonday(finish)
        Integer pharmacyVisits = poiEventService.filterPoiEvents(careReceiver, "Pharmacy", start, finish)
        Integer supermarketVisits = poiEventService.filterPoiEvents(careReceiver, "Supermarket", start, finish)
        Integer shopVisits = poiEventService.filterPoiEvents(careReceiver, "Shop", start, finish)
        Integer restaurantVisits = poiEventService.filterPoiEvents(careReceiver, "restaurant", start, finish)


        def weeklyMeasure = new WeeklyMeasure(

                startDate: start,
                uploaded: false,
                careReceiver: careReceiver,
                pharmacyVisitsWeek: pharmacyVisits,
                supermarketVisitsWeek: supermarketVisits,
                shopVisitsWeek: shopVisits,
                restaurantVisitsWeek: restaurantVisits

        )
        return weeklyMeasure
    }


    @Transactional(readOnly = true)
    def listWeeklyMeasures(CareReceiver receiver) {
        def list = createWeeklyMeasure(receiver)
        return list
    }


    def persistChanges(weeklyMeasure) {
        try {
            weeklyMeasure = weeklyMeasure.save()
        }
        catch (Exception e) {
            log.error(e.message)
            weeklyMeasure = null
        }

        return weeklyMeasure
    }

}