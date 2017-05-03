package org.etive.city4age.repository

class EventList {
    private list = null
    private Integer index = -1
    private Integer length = 0

    EventList(list) {
        if (list) {
            this.list = list
            this.length = list.size()
        }
    }

    def nextFound(beacon) {
        if (list.isEmpty) return null
        this.index++
        while ((this.index < this.length) && (this.list[this.index].eventType != 'found') &&
                (this.list[this.index].beacon.id != beacon.id)) this.index++
        if (this.index >= this.length) return null
        return this.list[this.index]
    }

    def nextLost() {
        if (list.isEmpty) return null
        this.index++
        while ((this.index < this.length) && (this.list[this.index].eventType != 'lost')) this.index++
        if (this.index >= this.length) return null
        return this.list[this.index]
    }

    def isEmpty() {
        return ((list) ? (this.index + 1 == this.length) : false)
    }
}
