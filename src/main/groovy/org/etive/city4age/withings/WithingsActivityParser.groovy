/*
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 * Copyright: Etive Technologies Ltd. 2016 (www.etive.org). All rights reserved.
 * Created  : 14-Dec-2016
 * Author   : Craig, (c dot speedie at etive dot org)
 * Enquiries: please send any enquiries to hello at etive dot org
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */
package org.etive.city4age.withings;

import org.etive.city4age.withings.model.WithingsActivity
import grails.converters.JSON

class WithingsActivityParser {

    static List<WithingsActivity> parse(response) {
        def withingsActivities = []
        try {
            def activities = JSON.parse(response).body.activities
            for (activity in activities) {
                def withingsActivity = new WithingsActivity()
                withingsActivity.with {
                    setDate(activity.date as String)
                    setCalories(activity.calories as Float)
                    setTotalCalories(activity.totalcalories as Float)
                    setDistance(activity.distance as Float)
                    setSoft(activity.soft as Integer)
                    setModerate(activity.moderate as Integer)
                    setIntense(activity.intense as Integer)
                    setSteps(activity.steps as Integer)
                }
                withingsActivities << withingsActivity
            }
            withingsActivities.sort {a, b -> a.getDate() <=> b.getDate()}
        }
        catch (e) {
        }

        return withingsActivities
    }
}
