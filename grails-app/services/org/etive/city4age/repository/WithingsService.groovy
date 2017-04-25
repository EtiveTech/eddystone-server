package org.etive.city4age.repository

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Response
import com.github.scribejava.core.model.SignatureType
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
import org.etive.city4age.withings.WithingsApi
import org.etive.city4age.withings.ActivityDataParser
import org.etive.city4age.withings.SleepDataParser
import org.etive.city4age.withings.model.City4AgeDateUtils
import org.etive.city4age.withings.model.WithingsData
import org.etive.city4age.withings.model.Sleep
import grails.transaction.Transactional

@Transactional
class WithingsService {

    private OAuth10aService service

    WithingsService() {
        this.service = new ServiceBuilder()
                .apiKey(WithingsApi.CONS_KEY)
                .apiSecret(WithingsApi.CONS_SECRET)
                .signatureType(SignatureType.QueryString)
                .callback(WithingsApi.CALLBACK)
                .build(WithingsApi.instance())
    }

    Map<String, List<WithingsData>> getWithingsData(CareReceiver careReceiver) {
        final String startDate = City4AgeDateUtils.getSixMonthsAgo(false)
        final String yesterday = City4AgeDateUtils.getYesterday(false)
        OAuth1AccessToken accToken = new OAuth1AccessToken(careReceiver.accessKey, careReceiver.accessSecret)
        Map<String, List<WithingsData>> userData = new HashMap<>()
        def apiMeasuresUrl = "https://wbsapi.withings.net/v2/measure?action=getactivity&userid="
        apiMeasuresUrl += careReceiver.withingsId
        apiMeasuresUrl += "&startdateymd=" + startDate
        apiMeasuresUrl +="&enddateymd=" + yesterday
        OAuthRequest request = new OAuthRequest(Verb.GET, apiMeasuresUrl, service)
        service.signRequest(accToken, request)
        Response response = request.send()
        try {
            String body = response.getBody()
            if (!body.isEmpty()) {
                final ActivityDataParser adp = new ActivityDataParser()
                userData = adp.parseActivityResponseBody(body)
            }
        } catch (Exception e) {
            log.info("cannot get activity data: " + e.getMessage())
        }
        return Collections.unmodifiableMap(userData)
    }

    List<Sleep> getSleepData(CareReceiver careReceiver) {
        final String startDate = City4AgeDateUtils.getAWeekAgo(true)
        final String yesterday = City4AgeDateUtils.getYesterday(true)
        OAuth1AccessToken accToken = new OAuth1AccessToken(careReceiver.accessKey, careReceiver.accessSecret)
        List<Sleep> sleepData = new ArrayList<>()
        def apiMeasuresUrl = "https://wbsapi.withings.net/v2/sleep?action=get&userid="
        apiMeasuresUrl += careReceiver.withingsId
        apiMeasuresUrl += "&startdate=" + startDate
        apiMeasuresUrl += "&enddate=" + yesterday
        OAuthRequest request = new OAuthRequest(Verb.GET, apiMeasuresUrl, service)
        service.signRequest(accToken, request)
        Response response = request.send()
        try {
            String body = response.getBody()
            if (!body.isEmpty()) {
                final SleepDataParser sdp = new SleepDataParser()
                sleepData = sdp.parseSleepResponseData(body)
            }
        } catch (Exception e) {
            log.info("cannot get sleep data: " + e.getMessage())
        }
        return Collections.unmodifiableList(sleepData)
    }
}
