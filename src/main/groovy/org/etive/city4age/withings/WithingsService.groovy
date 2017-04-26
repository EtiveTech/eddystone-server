package org.etive.city4age.withings

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Response
import com.github.scribejava.core.model.SignatureType
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
import org.etive.city4age.withings.model.City4AgeDateUtils
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

    List<WithingsActivity> getActivityData(CareReceiver careReceiver) {
        final String startDate = City4AgeDateUtils.getSixMonthsAgo(false)
        final String yesterday = City4AgeDateUtils.getYesterday(false)
        OAuth1AccessToken accToken = new OAuth1AccessToken(careReceiver.accessKey, careReceiver.accessSecret)
        def activityData = []
        def apiMeasuresUrl = "https://wbsapi.withings.net/v2/measure?action=getactivity&userid="
        apiMeasuresUrl += careReceiver.withingsId
        apiMeasuresUrl += "&startdateymd=" + startDate
        apiMeasuresUrl +="&enddateymd=" + yesterday
        OAuthRequest request = new OAuthRequest(Verb.GET, apiMeasuresUrl, service)
        service.signRequest(accToken, request)
        Response response = request.send()
        try {
            def body = response.getBody()
            if (body) activityData = WithingsActivityParser.parse(body)
        } catch (Exception e) {
            def msg = e.getMessage()
//            log.info("cannot get activity data: " + msg)
        }
        return activityData
    }

    List<WithingsSleep> getSleepData(CareReceiver careReceiver) {
        final String startDate = City4AgeDateUtils.getAWeekAgo(true)
        final String yesterday = City4AgeDateUtils.getYesterday(true)
        OAuth1AccessToken accToken = new OAuth1AccessToken(careReceiver.accessKey, careReceiver.accessSecret)
        def sleepData = []
        def apiMeasuresUrl = "https://wbsapi.withings.net/v2/sleep?action=getsummary&userid="
        apiMeasuresUrl += careReceiver.withingsId
        apiMeasuresUrl += "&startdate=" + startDate
        apiMeasuresUrl += "&enddate=" + yesterday
        OAuthRequest request = new OAuthRequest(Verb.GET, apiMeasuresUrl, service)
        service.signRequest(accToken, request)
        Response response = request.send()
        try {
            def body = response.getBody()
            if (body) sleepData = WithingsSleepParser.parse(body)
        } catch (Exception e) {
            def msg = e.getMessage()
            // log.info("cannot get sleep data: " + msg)
        }
        return sleepData
    }
}
