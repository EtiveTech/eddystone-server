/*
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 * Copyright: Etive Technologies Ltd. 2016 (www.etive.org). All rights reserved.
 * Created  : 06-Dec-2016
 * Author   : Craig, (c dot speedie at etive dot org)
 * Enquiries: please send any enquiries to hello at etive dot org
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */
package org.etive.city4age.withings.model

import org.etive.city4age.repository.CareReceiver

class WithingsSleep {
    private String date
    private Date startDate
    private Date endDate
    private Integer wakeupDuration
    private Integer wakeupCount
    private Integer lightSleepDuration
    private Integer deepSleepDuration
    private Integer durationToSleep
    private CareReceiver careReceiver

    def setDate(String date) {
        this.date = date
    }

    def getDate() {
        return this.date
    }

    def setStartDate(Long startDate) {
        this.startDate = new Date(startDate * 1000)
    }

    def setEndDate(Long endDate) {
        this.endDate = new Date(endDate * 1000)
    }

    def setWakeupDuration(Integer wakeupDuration) {
        this.wakeupDuration = wakeupDuration
    }

    def setWakeupCount(Integer wakeupCount) {
        this.wakeupCount = wakeupCount
    }

    def setLightSleepDuration(Integer lightSleepDuration) {
        this.lightSleepDuration = lightSleepDuration
    }

    def setDeepSleepDuration(Integer deepSleepDuration) {
        this.deepSleepDuration = deepSleepDuration
    }

    def setDurationToSleep(durationToSleep) {
        this.durationToSleep = durationToSleep
    }

    def setCareReceiver(CareReceiver careReceiver) {
        this.careReceiver = careReceiver
    }

    def toMap() {
        return [
                date: this.date,
                startDate: this.startDate,
                endDate: this.endDate,
                wakeupDuration: this.wakeupDuration,
                wakeupCount: this.wakeupCount,
                lightSleepDuration: this.lightSleepDuration,
                deepSleepDuration: this.deepSleepDuration,
                durationToSleep: this.durationToSleep,
                careReceiver: this.careReceiver
        ]
    }
}
