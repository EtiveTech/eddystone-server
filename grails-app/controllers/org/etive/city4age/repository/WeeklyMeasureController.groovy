package org.etive.city4age.repository


class WeeklyMeasureController {
    static responseFormats = ['json', 'xml']

		def weeklyMeasureService


    def show() {
        def receiver = CareReceiver.findById(params.receiverId)
        def list = weeklyMeasureService.listWeeklyMeasures(receiver)

        respond(list, status:200)
    }
}
