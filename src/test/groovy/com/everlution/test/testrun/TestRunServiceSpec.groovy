package com.everlution.test.testrun

import com.everlution.AutomatedTest
import com.everlution.AutomatedTestService
import com.everlution.Project
import com.everlution.TestRun
import com.everlution.TestRunResult
import com.everlution.TestRunService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class TestRunServiceSpec extends Specification implements ServiceUnitTest<TestRunService>, DataTest {

    def setupSpec() {
        mockDomains(TestRun, Project)
    }

    void "returns new with valid instance"() {
        when:
        def t = service.save(new TestRun(name: "name", project: new Project()))

        then:
        t != null
    }

    void "throws validation exception with invalid instance"() {
        when:
        service.save(new TestRun(name: "", project: null))

        then:
        thrown(ValidationException)
    }

    void "get returns instance"() {
        given:
        def t = service.save(new TestRun(name: "name", project: new Project()))

        when:
        def found = service.get(t.id)

        then:
        found != null
    }

    void "get returns null when not found"() {
        when:
        def t = service.get(null)

        then:
        t == null
    }

    void "createAndSave returns empty list when no results passed in"() {
        given:
        def p = new Project(name: "name", code: "nme").save()

        when:
        TestRun t = service.createAndSave("test run name", p, [])

        then:
        t.name == "test run name"
        t.testResults.empty
    }

    void "createAndSave throws validation exception with null project"() {
        given:
        service.automatedTestService = Mock(AutomatedTestService) {
            1 * findOrSave(_,_) >> {
                def a = new AutomatedTest()
                throw new ValidationException("Invalid", a.errors)
            }
        }
        when:
        service.createAndSave("test run", null, [new TestRunResult(testName: "123", result: "Passed")])

        then:
        thrown(ValidationException)
    }

    void "createAndSave with null results returns empty list"() {
        given:
        def p = new Project(name: "name", code: "nme").save()

        when:
        def r = service.createAndSave("test", p, null)

        then:
        r.testResults.empty
        noExceptionThrown()
    }

    void "createAndSave throws validation exception when automated test has errors"() {
        given:
        def p = new Project(name: "name", code: "nme").save()
        service.automatedTestService = Mock(AutomatedTestService) {
            1 * findOrSave(_,_) >> {
                def a = new AutomatedTest()
                throw new ValidationException("error", a.errors)
            }
        }

        when:
        service.createAndSave("test", p, [new TestRunResult(testName: "fullName", result: "Passed")])

        then:
        thrown(ValidationException)
    }

    void "findAllByProject returns all test runs in project"() {
        given:
        def p = new Project(name: "find me 123", code: "fm123").save()
        def t1 = new TestRun(name: "test run 123", project: p).save()
        def t2 = new TestRun(name: "test run 1234", project: p).save(flush: true)

        when:
        def r = service.findAllByProject(p, [:]).results

        then:
        r.size() == 2
        r.containsAll([t1, t2])
    }

    void "findAllByProject returns only test runs in project"() {
        given:
        def p1 = new Project(name: "find me 123", code: "fm123").save()
        def p2 = new Project(name: "dont find me 123", code: "dfm12").save()
        def t1 = new TestRun(name: "test run 123", project: p1).save()
        def t2 = new TestRun(name: "test run 1234", project: p2).save(flush: true)

        when:
        def r = service.findAllByProject(p1, [:]).results

        then:
        r.size() == 1
        r.contains(t1)
        !r.contains(t2)
    }

    void "findAllByProject returns empty list when project null"() {
        when:
        def r = service.findAllByProject(null, [:]).results

        then:
        noExceptionThrown()
        r.empty
    }

    void "findAllInProjectByName returns test run"(String q) {
        given:
        def p = new Project(name: "dont find me 123", code: "dfm12").save()
        def t = new TestRun(name: "First test run 123", project: p).save(flush: true)

        expect:
        t != null

        when:
        def results = service.findAllInProjectByName(p, q, [:]).results

        then:
        results.first().name == "First test run 123"

        where:
        q << ['first', 'fi', 'irs', 't te', 'FIRST', 'n 123']
    }

    void "findAllInProjectByName with string returns tests"(String s, int size, int count) {
        given:
        def p = new Project(name: "dont find me 123", code: "dfm12").save()
        def t = new TestRun(name: "First test run 123", project: p).save()
        def t1 = new TestRun(name: "Second test run 123", project: p).save()

        expect:
        t != null

        when:
        def r = service.findAllInProjectByName(p, s, [:])

        then:
        r.results.size() == size
        r.count == count

        where:
        s           | size | count
        ''          | 2    | 2
        'not found' | 0    | 0
        'test'      | 2    | 2
    }

    void "findAllInProjectByName with null project returns empty list"() {
        when:
        def r = service.findAllInProjectByName(null, "string", [:]).results

        then:
        r.empty
        noExceptionThrown()
    }

    void "findAllInProjectByName returns only test runs in project"() {
        given:
        def p1 = new Project(name: "find me 123", code: "fm123").save()
        def p2 = new Project(name: "dont find me 123", code: "dfm12").save()
        def t1 = new TestRun(name: "test run 123", project: p1).save()
        def t2 = new TestRun(name: "test run 1234", project: p2).save(flush: true)

        when:
        def r = service.findAllInProjectByName(p1, "test", [:]).results

        then:
        r.size() == 1
        r.contains(t1)
        !r.contains(t2)
    }
}