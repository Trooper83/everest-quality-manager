package com.everlution.test.api.support.services

import kong.unirest.Unirest

class TestRunsService {

    def baseUrl
    def token

    TestRunsService(url, token) {
        this.baseUrl = url
        this.token = token
    }

    def createTestRun(String payload) {
        return Unirest.post(baseUrl + "/api/testRuns")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer: ${token}")
                .body(payload)
                .asString()
    }
}
