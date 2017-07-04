package org.etive.city4age.repository

import grails.plugin.json.builder.JsonOutput
import groovy.json.JsonSlurper
import javax.net.ssl.HttpsURLConnection

class CentralRepositorySession {
    private final SSL = true
    private final String centralRepository = System.getenv("CENTRAL_ADDRESS")
    private final String username = System.getenv("CENTRAL_USERNAME")
    private final String password = System.getenv("CENTRAL_PASSWORD")
    private final String protocol = (SSL) ? "https://" : "http://"
    private final String apiVersion = "/api/0.1/"
    private final String loginRoute = apiVersion + "login"
    private final String logoutRoute = apiVersion + "logout"
    private final String careReceiverRoute = apiVersion + "add_care_receiver"
    private final String measureRoute = apiVersion + "add_measure"
    private final String actionRoute = apiVersion + "add_action"

    private mToken = null

    private static readJson(InputStream content) {
        def reader = new BufferedReader(new InputStreamReader(content))
        def jsonText = ""
        int cp
        while ((cp = reader.read()) != -1) {
            jsonText += cp as char
        }
        return (jsonText) ? new JsonSlurper().parseText(jsonText) : null
    }

    private getEncodedCredentials() {
        String credentials = username + ":" + password
        def encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes())
        return encodedCredentials
    }

    private getConnection(URL url) {
        def connection = url.openConnection()
        return (SSL) ? connection as HttpsURLConnection : connection as HttpURLConnection
    }

    private makePostConnection(String route, payload, useToken = false) {
        // Use of token to authenticate is NOT working
        def url = new URL(protocol + centralRepository + route)
        def connection = getConnection(url)
        connection.setDoOutput(true)
        connection.setDoInput(true)
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "application/json")
        if (useToken) {
            connection.setRequestProperty("Cookie", "session=" + mToken)
        }
        else {
            connection.setRequestProperty("Authorization", "Basic " + getEncodedCredentials())
        }

        def output = connection.getOutputStream()
        def json = JsonOutput.toJson(payload)
        output.write(json.getBytes())
        output.close()
        return connection
    }

    def login() {
        def url = new URL(protocol + centralRepository + loginRoute)
        def connection = getConnection(url)
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
        def url = new URL(protocol + centralRepository + logoutRoute)
        def connection = getConnection(url)
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
        def username = "bhx" + id
        // All payload parameters MUST be strings
        def payload = [
                pilot_user_source_id: id.toString(),
                username: username,
                password: username,
                valid_from: validFrom.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("Europe/London"))
        ]

        def connection = makePostConnection(careReceiverRoute, payload)
        def input = null
        try{
            def status = connection.getResponseCode()
            if (status == 200) {
                input = connection.getInputStream()
                def json = readJson(input)
                city4AgeId = json[username] as String
            }
        }
        finally {
            if (input) input.close()
        }
        if (!city4AgeId) return null
        return "eu:c4a:user:" + city4AgeId
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
