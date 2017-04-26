/*
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 * Copyright: Etive Technologies Ltd. 2016 (www.etive.org). All rights reserved.
 * Created  : 14-Dec-2016
 * Author   : Craig, (c dot speedie at etive dot org)
 * Enquiries: please send any enquiries to hello at etive dot org
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */

package org.etive.city4age.withings

import grails.converters.JSON;
import org.etive.city4age.withings.model.WithingsSleep;
import org.json.simple.parser.ParseException

class WithingsSleepParser {

    static List<WithingsSleep> parse(response) {
        def withingsSleeps = []

        try {
            def series = JSON.parse(response).body.series
            for (item in series) {
                def withingsSleep = new WithingsSleep()
                withingsSleep.with {
                    setDate(item.date as String)
                    setWakeupDuration(item.data.wakeupduration as Integer)
                    setWakeupCount(item.data.wakeupcount as Integer)
                    setLightSleepDuration(item.data.lightsleepduration as Integer)
                    setDeepSleepDuration(item.data.deepsleepduration as Integer)
                    setDurationToSleep(item.data.durationtosleep as Integer)
                }
                withingsSleeps << withingsSleep
            }
            withingsSleeps.sort {a, b -> a.getDate() <=> b.getDate()}
        } catch (ParseException e) {
        }

        return withingsSleeps
    }
}
