package org.etive.city4age.repository

class SleepController {
    def index() {
        respond SleepRecord.list()
    }
}
