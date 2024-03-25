package com.everlution.test.api.functional.specs

import com.everlution.AutomatedTestService
import com.everlution.ProjectService
import com.everlution.TestResultService
import com.everlution.TestRunService
import com.everlution.test.api.support.services.AuthService
import com.everlution.test.api.support.services.TestRunsService
import com.everlution.test.support.results.SendResults
import com.everlution.test.support.data.Credentials
import grails.testing.mixin.integration.Integration
import kong.unirest.Unirest
import spock.lang.Shared
import spock.lang.Specification

@Integration
@SendResults
class TestRunSpec extends Specification {

    @Shared String baseUrl

    AutomatedTestService automatedTestService
    ProjectService projectService
    TestResultService testResultService
    TestRunService testRunService

    def setup() {
        baseUrl = "http://localhost:${serverPort}"
    }

    void "400 when project null"() {
        given:
        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun('{"name": "testing"}')

        then:
        r.status == 400
    }

    void "400 when project not found"() {
        given:
        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)
        def payload = '{"project": "9999999", "name": "testing"}'

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 400
    }

    void "400 when name null"() {
        given:
        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)
        def p = projectService.list(max:1).first().id
        def payload = "{\"project\": \"${p}\"}"

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 400
    }

    void "400 when name blank"() {
        given:
        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)
        def p = projectService.list(max:1).first().id
        def payload = "{\"project\": \"${p}\", \"name\": \"\"}"

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 400
    }

    void "201 when test run created no results"() {
        given:
        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)
        def p = projectService.list(max:1).first().id
        def payload = "{\"project\": \"${p}\", \"name\": \"testing\"}"

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 201
        r.body ==~ /TestRun \d+ created/
    }

    void "201 when test run created with results"(String username, String password) {
        given:
        def token = new AuthService(baseUrl).login(username, password)
        def p = projectService.list(max:1).first().id
        def payload = "{\"project\": \"${p}\", \"name\": \"testing\", \"testResults\": [{\"testName\":\"test name\", \"result\": \"Passed\"}]}"

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 201
        r.body ==~ /TestRun \d+ created/

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "400 when automated test blank"() {
        given:
        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)
        def p = projectService.list(max:1).first().id
        def payload = "{\"project\": \"${p}\", \"name\": \"testing\", \"testResults\": [{\"testName\":\"\", \"result\": \"Passed\"}]}"

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 400
    }

    void "400 when result blank"() {
        given:
        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)
        def p = projectService.list(max:1).first().id
        def payload ="{\"project\": \"${p}\", \"name\": \"testing\", \"testResults\": [{\"testName\":\"testing\", \"result\": \"\"}]}"

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 400
    }

    void "transaction rolls back when exception thrown"() {
        given:
        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)
        def p = projectService.list(max:1).first()
        def a = automatedTestService.findOrSave(p, "transactional testing999")
        String str = "a" * 256 //exceeds testRun.name maxSize
        def payload = "{\"project\": \"${p.id}\", \"name\": \"${str}\", \"testResults\": [{\"testName\":\"transactional testing999\", \"result\": \"Passed\"}]}"

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 400
        testResultService.findAllByAutomatedTest(a).empty
    }

    void "request creates automated test and results and test run"() {
        given:
        def token = new AuthService(baseUrl).login(Credentials.BASIC.email, Credentials.BASIC.password)
        def p = projectService.list(max:1).first()
        def a = automatedTestService.findOrSave(p, "functional happy path testing")
        def at = automatedTestService.findOrSave(p, "functional happy path testing II")
        def payload = "{\"project\": \"${p.id}\", \"name\": \"Functional Testing Test Run\", \"testResults\": [{\"testName\":\"functional happy path testing\", \"result\": \"Passed\"}, {\"testName\":\"functional happy path testing II\", \"result\": \"Failed\", \"failureCause\": \"just because\"}]}"

        expect:
        testResultService.findAllByAutomatedTest(a).empty
        testResultService.findAllByAutomatedTest(at).empty

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 201
        def aResults = testResultService.findAllByAutomatedTest(a)
        aResults.size() == 1
        aResults.first().result == "PASSED"
        def atResults = testResultService.findAllByAutomatedTest(at)
        atResults.size() == 1
        atResults.first().result == "FAILED"
        atResults.first().failureCause == "just because"
        def start = r.body.indexOf(" ")
        def end = r.body.lastIndexOf(" ")
        def id = r.body.substring(start + 1, end)
        testRunService.get(id) != null
    }

    void "read only is not authorized"() {
        given:
        def token = new AuthService(baseUrl).login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)
        def p = projectService.list(max:1).first().id
        def payload = "{\"project\": \"${p}\", \"name\": \"testing\", \"testResults\": [{\"testName\":\"test name\", \"result\": \"Passed\"}]}"

        when:
        def r = new TestRunsService(baseUrl, token).createTestRun(payload)

        then:
        r.status == 403
    }

    void "401 returned when no auth header present"() {
        when:
        def p = projectService.list(max:1).first().id
        def r = Unirest.post(baseUrl + "/api/testRuns")
                .header("Content-Type", "application/json")
                .body("{\"project\": \"${p}\", \"name\": \"testing\"}")
                .asString()

        then:
        r.status == 401
    }
}
