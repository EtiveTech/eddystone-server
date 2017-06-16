package org.etive.city4age.repository

import grails.plugin.json.builder.JsonOutput
import org.grails.web.json.JSONObject

/**
 * Created by user on 15/06/2017.
 */
class CentralRepositorySession {

    private final String centralRepository = System.getenv("CENTRAL_ADDRESS")
    private final String username = System.getenv("CENTRAL_USERNAME")
    private final String password = System.getenv("CENTRAL_PASSWORD")
    private final String protocol = "https://"
    private final String apiVersion = "/api/0.1/"
    private final String loginRoute = apiVersion + "login"
    private final String logoutRoute = apiVersion + "logout"
    private final String careReceiverRoute = apiVersion + "add_care_receiver"
    private final String measureRoute = apiVersion + "add_measure"
    private final String actionRoute = apiVersion + "add_action"

    private mToken = null

    private readJson(InputStream content) {
        def reader = new BufferedReader(new InputStreamReader(content))
        def jsonText = ""
        int cp
        while ((cp = reader.read()) != -1) {
            jsonText += cp as char
        }
        return (jsonText) ? JSONObject(jsonText) : null
    }

    private getEncodedCredentials() {
        String credentials = username + ":" + password
        def encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes())
        return encodedCredentials
    }

    private HttpURLConnection makePostConnection(route, payload, useToken = false) {
        def connection = new URL(protocol + centralRepository + route).openConnection() as HttpURLConnection
        connection.setRequestMethod("POST")
        connection.setDoOutput(true)
        connection.setRequestProperty("Content-Type", "application/json")
        if (useToken) {
            connection.setRequestProperty("Cookie", mToken)
        }
        else {
            connection.setRequestProperty("Authorization", "Basic " + getEncodedCredentials())
        }

        def output = connection.getOutputStream()
        output.write(JsonOutput.toJson(payload).getBytes())
        return connection
    }

    def login() {
        def connection = new URL(protocol + centralRepository + loginRoute).openConnection() as HttpURLConnection
        connection.setRequestMethod("GET")
        connection.setRequestProperty("Authorization", "Basic " + getEncodedCredentials())
        def content = connection.getInputStream()
        try{
            def status = connection.getResponseCode()
            if (status == 200) {
                def json = readJson(content)
                mToken = json.token
            }
        }
        finally {
            content.close()
        }
        return mToken != null
    }

    def logout() {
        def connection = new URL(protocol + centralRepository + logoutRoute).openConnection() as HttpURLConnection
        connection.setRequestMethod("GET")
        connection.setRequestProperty("Authorization", "Basic " + getEncodedCredentials())
        def status = connection.getResponseCode()
        def success = (status == 200)
        if (success) mToken = null
        return success
    }

    def getCity4AgeId(Long id, Date validFrom) {

        if (!mToken) return null

        def city4AgeId = ""
        def payload = [
                pilot_user_source_id: id,
                username: "bhx" + id,
                password: "bhx" + id,
                valid_from: validFrom.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        ]

        def connection = makePostConnection(careReceiverRoute, payload)
        def input = connection.getInputStream()
        try{
            def status = connection.getResponseCode()
            if (status == 200) {
                def json = readJson(input)
                city4AgeId = json.bimin_care_r as String
            }
        }
        finally {
            input.close()
        }
        if (!city4AgeId) return null
        return "eu:c4a:user:" + id
    }

    def sendMeasure(measure) {
        if (!mToken) return false

        def connection = makePostConnection(measureRoute, measure)
        def status = connection.getResponseCode()
        return (status == 200)
    }

    def sendAction(action) {
        if (!mToken) return false

        def connection = makePostConnection(actionRoute, action)
        def status = connection.getResponseCode()
        return (status == 200)
    }
}
