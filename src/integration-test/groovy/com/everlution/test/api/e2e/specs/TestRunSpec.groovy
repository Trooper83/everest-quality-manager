package com.everlution.test.api.e2e.specs

import com.everlution.test.api.support.services.AuthService
import com.everlution.test.api.support.services.TestRunsService
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import spock.lang.Specification

@SendResults
class TestRunSpec extends Specification {

    def baseUrl = System.getProperty("apiBaseUrl")
    def projectId = System.getProperty("apiProjectId")

    void "201 when test run created no results"() {
        given:

        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)
        def payload = "{\"project\": \"${projectId}\", \"name\": \"testing\"}"

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 201
        r.body ==~ /TestRun \d+ created/
    }

    void "201 when automated test exists"() {
        given:
        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)
        def payload = "{\"project\": \"${projectId}\", \"name\": \"E2E Test Run\", " +
                "\"testResults\": [{\"testName\":\"transactional testing\", \"result\": \"PASSED\"}, " +
                "{\"testName\":\"transactional testing\", \"result\": \"PASSED\"}]}"

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 201
        r.body ==~ /TestRun \d+ created/
    }

    void "401 when user not authenticated"() {
        given:
        def payload = "{\"project\": \"${projectId}\", \"name\": \"testing\"}"

        when:
        def r = new TestRunsService(baseUrl, "").createTestRun(payload)

        then:
        r.status == 401
    }
}