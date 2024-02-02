package com.everlution.test.api.functional.specs

import com.everlution.AutomatedTestService
import com.everlution.ProjectService
import com.everlution.TestResultService
import com.everlution.TestRunService
import grails.testing.mixin.integration.Integration
import kong.unirest.Unirest
import spock.lang.Shared
import spock.lang.Specification

@Integration
class TestRunSpec extends Specification {

    @Shared String url

    AutomatedTestService automatedTestService
    ProjectService projectService
    TestResultService testResultService
    TestRunService testRunService

    def setup() {
        url = "http://localhost:${serverPort}/api/testRuns"
    }

    void "400 when project null"() {
        when:
        def r = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body('{"name": "testing"}')
                .asEmpty()

        then:
        r.status == 400
    }

    void "400 when project not found"() {
        when:
        def r = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body('{"project": "9999999", "name": "testing"}')
                .asEmpty()

        then:
        r.status == 400
    }

    void "400 when name null"() {
        when:
        def p = projectService.list(max:1).first().id
        def r = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body("{\"project\": \"${p}\"}")
                .asEmpty()

        then:
        r.status == 400
    }

    void "400 when name blank"() {
        when:
        def p = projectService.list(max:1).first().id
        def r = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body("{\"project\": \"${p}\", \"name\": \"\"}")
                .asEmpty()

        then:
        r.status == 400
    }

    void "201 when test run created no results"() {
        when:
        def p = projectService.list(max:1).first().id
        def r = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body("{\"project\": \"${p}\", \"name\": \"testing\"}")
                .asString()

        then:
        r.status == 201
        r.body ==~ /TestRun \d+ created/
    }

    void "201 when test run created with results"() {
        when:
        def p = projectService.list(max:1).first().id
        def r = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body("{\"project\": \"${p}\", \"name\": \"testing\", \"testResults\": [{\"testName\":\"test name\", \"result\": \"Passed\"}]}")
                .asString()

        then:
        r.status == 201
        r.body ==~ /TestRun \d+ created/
    }

    void "400 when automated test blank"() {
        when:
        def p = projectService.list(max:1).first().id
        def r = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body("{\"project\": \"${p}\", \"name\": \"testing\", \"testResults\": [{\"testName\":\"\", \"result\": \"Passed\"}]}")
                .asString()

        then:
        r.status == 400
    }

    void "400 when result blank"() {
        when:
        def p = projectService.list(max:1).first().id
        def r = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body("{\"project\": \"${p}\", \"name\": \"testing\", \"testResults\": [{\"testName\":\"testing\", \"result\": \"\"}]}")
                .asString()

        then:
        r.status == 400
    }

    void "transaction rolls back when exception thrown"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.findOrSave(p, "transactional testing")
        String str = "a" * 256 //exceeds testRun.name maxSize

        when:
        def r = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body("{\"project\": \"${p.id}\", \"name\": \"${str}\", \"testResults\": [{\"testName\":\"transactional testing\", \"result\": \"Passed\"}]}")
                .asString()

        then:
        r.status == 400
        testResultService.findAllByAutomatedTest(a).empty
    }

    void "request creates automated test and results and test run"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.findOrSave(p, "functional happy path testing")
        def at = automatedTestService.findOrSave(p, "functional happy path testing II")

        expect:
        testResultService.findAllByAutomatedTest(a).empty
        testResultService.findAllByAutomatedTest(at).empty

        when:
        def r = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body("{\"project\": \"${p.id}\", \"name\": \"Functional Testing Test Run\", \"testResults\": [{\"testName\":\"functional happy path testing\", \"result\": \"Passed\"}, {\"testName\":\"functional happy path testing II\", \"result\": \"Failed\", \"failureCause\": \"just because\"}]}")
                .asString()

        then:
        r.status == 201
        def aResults = testResultService.findAllByAutomatedTest(a)
        aResults.size() == 1
        aResults.first().result == "Passed"
        def atResults = testResultService.findAllByAutomatedTest(at)
        atResults.size() == 1
        atResults.first().result == "Failed"
        atResults.first().failureCause == "just because"
        def start = r.body.indexOf(" ")
        def end = r.body.lastIndexOf(" ")
        def id = r.body.substring(start + 1, end)
        testRunService.get(id) != null
    }
}
