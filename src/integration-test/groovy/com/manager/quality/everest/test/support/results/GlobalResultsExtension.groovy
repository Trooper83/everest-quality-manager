package com.manager.quality.everest.test.support.results

import com.manager.quality.everest.test.api.support.models.LoginResponse
import kong.unirest.Unirest
import org.spockframework.runtime.extension.IGlobalExtension

class GlobalResultsExtension implements IGlobalExtension {

    @Override
    void stop() {
        if(System.getProperty("sendResults") == "true" && !ResultStore.getSent()) {
            def r = ResultStore.getResults()
            r.collect { it -> {
                if(it.testName.contains("_iteration_")) {
                    def index = it.testName.indexOf(" _iteration_")
                    it.testName = it.testName.substring(0, index)
                }
            }}

            def payload = new TestRunResults(name: null, project: null, testResults: r) //TODO: set project id and name
            def baseUrl = "/api" //TODO: Add URL to environment
            def token = Unirest.post(baseUrl + "/login")
                    .body("{\"username\": \"basic@basic.com\",\"password\": \"!Password#2022\"}")
                    .asObject(LoginResponse).getBody().access_token
            def resp = Unirest.post(baseUrl + "/testRuns")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer: ${token}")
                    .body(payload)
                    .asString()
            println(resp.body)
            ResultStore.setSent()
        }
    }
}
