package org.etive.city4age.withings

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Response
import com.github.scribejava.core.model.SignatureType
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
import grails.converters.JSON
import org.etive.city4age.withings.model.WithingsActivity
import org.etive.city4age.repository.CareReceiver
import org.etive.city4age.withings.model.WithingsSleep

class WithingsService {

    private OAuth10aService service

    WithingsService() {
        this.service = new ServiceBuilder()
                .apiKey(WithingsApi.CONS_KEY)
                .apiSecret(WithingsApi.CONS_SECRET)
                .signatureType(SignatureType.QueryString)
                .build(WithingsApi.instance())
    }

    List<WithingsActivity> getActivityData(CareReceiver careReceiver, Date startDate, Date endDate = null) {

        OAuth1AccessToken accToken = new OAuth1AccessToken(careReceiver.accessKey, careReceiver.accessSecret)
        def apiMeasuresUrl = "https://wbsapi.withings.net/v2/measure?action=getactivity&userid="
        apiMeasuresUrl += careReceiver.withingsId
        if (endDate) {
            apiMeasuresUrl += "&startdateymd=" + startDate.format("yyyy-MM-dd")
            apiMeasuresUrl +="&enddateymd=" + endDate.format("yyyy-MM-dd")
        }
        else {
            apiMeasuresUrl += "&date=" + startDate.format("yyyy-MM-dd")
        }
        OAuthRequest request = new OAuthRequest(Verb.GET, apiMeasuresUrl, service)
        service.signRequest(accToken, request)
        Response response = request.send()

        def withingsActivities = []
        try {
            def json = response.getBody()
            if (json) {
                def body = JSON.parse(json).body
                def activities = []
                if (body.activities) {
                    activities = body.activities
                }
                else {
                    // The Withings API has not returned an array so create one
                    if (containsActivity(body)) activities << body
                }

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
                if (withingsActivities.size() > 1) {
                    // Only need to sort if there are two or more items in the list
                    // Sorting a list of zero length will throw an exception
                    withingsActivities.sort {a, b -> a.getDate() <=> b.getDate()}
                }
            }
        } catch (Exception e) {
            def msg = e.getMessage()
//            log.info("cannot get activity data: " + msg)
        }
        return withingsActivities
    }

    List<WithingsSleep> getSleepData(CareReceiver careReceiver, Date startDate, Date endDate) {

        OAuth1AccessToken accToken = new OAuth1AccessToken(careReceiver.accessKey, careReceiver.accessSecret)
        def apiMeasuresUrl = "https://wbsapi.withings.net/v2/sleep?action=getsummary&userid="
        apiMeasuresUrl += careReceiver.withingsId
        apiMeasuresUrl += "&startdate=" + toEpoch(startDate)
        apiMeasuresUrl += "&enddate=" + toEpoch(endDate)
        OAuthRequest request = new OAuthRequest(Verb.GET, apiMeasuresUrl, service)
        service.signRequest(accToken, request)
        Response response = request.send()

        def withingsSleeps = []
        try {
            def json = response.getBody()
            if (json) {
                def series = JSON.parse(json).body.series
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
            }
        } catch (Exception e) {
            def msg = e.getMessage()
            // log.info("cannot get sleep data: " + msg)
        }
        return withingsSleeps
    }

    private static String toEpoch(Date date) {
        return ((int)(date.getTime() / 1000)).toString()
    }

    private static Boolean containsActivity(item) {
        return item.containsKey('date') && item.containsKey('calories') && item.containsKey('totalcalories') &&
                item.containsKey('distance') && item.containsKey('soft') && item.containsKey('moderate') &&
                item.containsKey('intense') && item.containsKey('steps')
    }

}
