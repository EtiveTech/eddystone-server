//package org.etive.city4age.repository
//
//import grails.transaction.Transactional
//
//@Transactional
//class WeeklyMeasureService {
//    def poiEventService
//    def locationService
//
//    @Transactional
//    def listCareReceiverEvents(CareReceiver careReceiver) {
//        def poiEventList = poiEventService.listPoiEvents(careReceiver)
//        def query = poiEventList.where { action == "POI_ENTER" }
//        return query.list
//    }
//}