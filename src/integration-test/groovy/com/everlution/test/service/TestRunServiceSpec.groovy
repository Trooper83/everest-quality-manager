package com.everlution.test.service

import com.everlution.ProjectService
import com.everlution.TestRun
import com.everlution.TestRunResult
import com.everlution.TestRunService
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.validation.ValidationException
import spock.lang.Specification

@Rollback
@Integration
class TestRunServiceSpec extends Specification {

    ProjectService projectService
    TestRunService testRunService

    void "save returns instance"() {
        given:
        def p = projectService.list(max:1).first()
        def t = new TestRun(name: "name of the run", project: p)

        when:
        def tr = testRunService.save(t)

        then:
        tr.id != null
    }

    void "get returns instance"() {
        given:
        def p = projectService.list(max:1).first()
        def t = new TestRun(name: "name of the run to find", project: p)
        def tr = testRunService.save(t)

        when:
        def found = testRunService.get(tr.id)

        then:
        found != null
    }

    void "createAndSave returns test run"() {
        when:
        def p = projectService.list(max:1).first()
        def t = testRunService.createAndSave("name", p, [])

        then:
        t != null
    }

    void "createAndSave throws validation exception when test result invalid"() {
        when:
        def p = projectService.list(max:1).first()
        def t = testRunService.createAndSave("name", p, [new TestRunResult(testName: "test", result: "I should fail")])

        then:
        thrown(ValidationException)
    }

    void "createAndSave throws validation exception when automated test invalid"() {
        when:
        def p = projectService.list(max:1).first()
        testRunService.createAndSave("name", p, [new TestRunResult(testName: "", result: "Failed")])

        then:
        thrown(ValidationException)
    }

    void "findAllByProject returns empty list when project null"() {
        when:
        def r = testRunService.findAllByProject(null, [:])

        then:
        r.results.empty
        noExceptionThrown()
    }

    void "findAllByProject returns test runs in project"() {
        given:
        def p = projectService.list(max: 1).first()
        def r = testRunService.createAndSave("test run 999", p, [])

        expect:
        r != null

        when:
        def tr = testRunService.findAllByProject(p, [:]).results

        then:
        tr.size() == 2
        tr.contains(r)
    }

    void "findAllInProjectByName returns empty list when project null"() {
        when:
        def r = testRunService.findAllInProjectByName(null, "test", [:])

        then:
        r.results.empty
        noExceptionThrown()
    }

    void "findAllInProjectByName returns test runs in project"() {
        given:
        def p = projectService.list(max: 1).first()
        def r = testRunService.createAndSave("test run 999", p, [])

        expect:
        r != null

        when:
        def tr = testRunService.findAllInProjectByName(p, "run", [:]).results

        then:
        tr.size() == 2
        tr.contains(r)
    }
}
