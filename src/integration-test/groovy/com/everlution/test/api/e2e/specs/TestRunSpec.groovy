package com.everlution.test.api.e2e.specs

import com.everlution.test.api.support.services.AuthService
import com.everlution.test.api.support.services.TestRunsService
import com.everlution.test.support.data.Credentials
import spock.lang.Specification

class TestRunSpec extends Specification {

    void "201 when test run created no results"() {
        given:
        def baseUrl = "http://localhost:8080/api/testRuns"
        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)
        def payload = "{\"project\": \"1\", \"name\": \"testing\"}"

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 201
        r.body ==~ /TestRun \d+ created/
    }
}