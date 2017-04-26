/*
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 * Copyright: Etive Technologies Ltd. 2016 (www.etive.org). All rights reserved.
 * Created  : 06-Dec-2016
 * Author   : Craig, (c dot speedie at etive dot org)
 * Enquiries: please send any enquiries to hello at etive dot org
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */
package org.etive.city4age.withings.model

class WithingsSleep {
    private String date
    private Integer wakeupDuration
    private Integer wakeupCount
    private Integer lightSleepDuration
    private Integer deepSleepDuration
    private Integer durationToSleep

    def setDate(String date) {
        this.date = date
    }

    def getDate() {
        return this.date
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

    def toMap() {
        return [
                date: this.date,
                wakeupDuration: this.wakeupDuration,
                wakeupCount: this.wakeupCount,
                lightSleepDuration: this.lightSleepDuration,
                deepSleepDuration: this.deepSleepDuration,
                durationToSleep: this.durationToSleep
        ]
    }
}
