package org.etive.city4age.repository

import grails.transaction.Transactional

@Transactional
class WeeklyMeasureService {
    def poiEventService

//     Creates a Measure object for one care receiver with the full history
    WeeklyMeasure createWeeklyMeasure(CareReceiver careReceiver) {
        Date finish = poiEventService.getThisWeeksMondayDate()
        Date start = poiEventService.getDateLastWeekMonday(finish)
        String startDateString = start.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("Europe/London")).toString()
        log.info("******** Upload start time: " + startDateString + " ***********")
        Integer pharmacyVisits = poiEventService.filterPoiEvents(careReceiver, "Pharmacy", start, finish)
        Integer supermarketVisits = poiEventService.filterPoiEvents(careReceiver, "Supermarket", start, finish)
        Integer shopVisits = poiEventService.filterPoiEvents(careReceiver, "Shop", start, finish)
        Integer restaurantVisits = poiEventService.filterPoiEvents(careReceiver, "restaurant", start, finish)


        def weeklyMeasure = new WeeklyMeasure(

                startDate: startDateString,
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




}