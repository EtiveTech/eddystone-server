package org.etive.city4age.repository

class EventList {
    private list = null
    private Integer index = -1
    private Integer length = 0

    EventList(list) {
        if (list) {
            this.list = list.reverse()
            this.length = list.size()
        }
    }

    def nextFound(beacon) {
        if (isEmpty()) return null
        index += 1
        while ((index < length) && (list[index].eventType != 'found') &&
                (list[index].beacon.id != beacon.id)) index += 1
        if (index >= length) return null
        return list[index]
    }

    def nextLost() {
        if (isEmpty()) return null
        index += 1
        while ((index < length) && (list[index].eventType != 'lost')) index += 1
        if (index >= length) return null
        return list[index]
    }

    def isEmpty() {
        return ((list && length > 0) ? (index + 1 == length) : true)
    }
}
