package org.etive.city4age.repository



class MonthlyMeasureController {
	static responseFormats = ['json', 'xml']

        def monthlyMeasureService
	

    def show() {
        def receiver = CareReceiver.findById(params.receiverId)
        def list = monthlyMeasureService.listMonthlyMeasures(receiver)

        respond(list, status:200)
    }
}
