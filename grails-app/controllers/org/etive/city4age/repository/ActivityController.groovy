package org.etive.city4age.repository

class ActivityController {
    def index() {
        respond ActivityRecord.list()
    }
}
