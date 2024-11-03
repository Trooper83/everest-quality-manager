package com.manager.quality.everest.test.unit.automatedtest

import com.manager.quality.everest.domains.AutomatedTest
import com.manager.quality.everest.services.automatedtest.AutomatedTestService
import com.manager.quality.everest.domains.Project
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class AutomatedTestServiceSpec extends Specification implements ServiceUnitTest<AutomatedTestService>, DataTest {

    def setupSpec() {
        mockDomains(AutomatedTest, Project)
    }

    void "findOrSave returns new instance when not found"() {
        when:
        def p = new Project(name: "123", code: "123").save()
        def t = service.findOrSave(p, "testing")

        then:
        t != null
    }

    void "findOrSave returns same instance when found"() {
        given:
        def p = new Project(name: "124", code: "124").save()
        def t = service.findOrSave(p, "testing")

        expect:
        AutomatedTest.count() == 1

        when:
        def test = service.findOrSave(p, "testing")

        then:
        t.id == test.id
        AutomatedTest.count() == 1
    }

    void "findOrSave creates new instance when not found"() {
        given:
        def p = new Project(name: "124", code: "124").save()
        def t = service.findOrSave(p, "testing")

        expect:
        AutomatedTest.count() == 1

        when:
        def test = service.findOrSave(p, "testing123")

        then:
        t.id != test.id
        AutomatedTest.count() == 2
    }

    void "findOrSave throws validation exception when project null"() {
        when:
        service.findOrSave(null, "testing")

        then:
        thrown(ValidationException)
    }

    void "findOrSave throws validation exception when fullName null"() {
        when:
        Project p = new Project(name: "124", code: "124").save()
        service.findOrSave(p, null)

        then:
        thrown(ValidationException)
    }

    void "findOrSave searches within project only"() {
        given:
        Project p = new Project(name: "124", code: "124").save()
        def first = service.findOrSave(p, "testing 123")

        when:
        Project pr = new Project(name: "125", code: "125").save()
        def second = service.findOrSave(pr, "testing 123")

        then:
        first != second
    }

    void "save persists instance"() {
        when:
        Project p = new Project(name: "125", code: "125").save()
        def a = service.save(new AutomatedTest(project: p, fullName: "testing 123"))

        then:
        a != null
        a instanceof AutomatedTest
    }

    void "save throws validationexception"() {
        when:
        service.save(new AutomatedTest(project: null, fullName: "testing 123"))

        then:
        thrown(ValidationException)
    }

    void "save throws validation exception when full name not unique in project"() {
        when:
        Project p = new Project(name: "125", code: "125").save()
        service.save(new AutomatedTest(project: p, fullName: "testing 123"))
        service.save(new AutomatedTest(project: p, fullName: "testing 123"))

        then:
        thrown(ValidationException)
    }

    void "findByProjectAndFullName returns automated test"() {
        given:
        Project p = new Project(name: "125", code: "126").save()
        def a = new AutomatedTest(project: p, fullName: "should be found").save(flush: true)

        expect:
        a != null

        when:
        def found = service.findByProjectAndFullName(p, "should be found")

        then:
        found != null
        found == a
    }

    void "findByProjectAndFullName returns null when fullName not found"(String name) {
        given:
        Project p = new Project(name: "127", code: "127").save()
        def a = new AutomatedTest(project: p, fullName: "should be found").save(flush: true)

        expect:
        a != null

        when:
        def found = service.findByProjectAndFullName(p, name)

        then:
        found == null
        noExceptionThrown()

        where:
        name << ["should not be found", null, "", " "]
    }

    void "findByProjectAndFullName returns null when project null"() {
        when:
        def found = service.findByProjectAndFullName(null, "should be found")

        then:
        found == null
        noExceptionThrown()
    }

    void "findAllByProject returns all tests in project"() {
        given:
        def p = new Project(name: "find me 123", code: "fm123").save()
        def t1 = new AutomatedTest(fullName: "first name", project: p).save()
        def t2 = new AutomatedTest(fullName: "second name", project: p).save(flush: true)

        when:
        def r = service.findAllByProject(p, [:]).results

        then:
        r.size() == 2
        r.containsAll([t1, t2])
    }

    void "findAllByProject returns only tests in project"() {
        given:
        def p1 = new Project(name: "find me 123", code: "fm123").save()
        def p2 = new Project(name: "dont find me 123", code: "dfm12").save()
        def t1 = new AutomatedTest(fullName: "first name", project: p1).save()
        def t2 = new AutomatedTest(fullName: "second name", project: p2).save(flush: true)

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

    void "findAllInProjectByFullName returns test run"(String q) {
        given:
        def p = new Project(name: "dont find me 123", code: "dfm12").save()
        def t = new AutomatedTest(fullName: "First test 123", project: p).save(flush: true)

        expect:
        t != null

        when:
        def results = service.findAllInProjectByFullName(p, q, [:]).results

        then:
        results.first().fullName == "First test 123"

        where:
        q << ['first', 'fi', 'irs', 't te', 'FIRST', 't 123']
    }

    void "findAllInProjectByFullName with string returns tests"(String s, int size, int count) {
        given:
        def p = new Project(name: "dont find me 123", code: "dfm12").save()
        def t = new AutomatedTest(fullName: "First test 123", project: p).save(flush: true)
        def t1 = new AutomatedTest(fullName: "Second test 123", project: p).save(flush: true)

        expect:
        t != null
        t1 != null

        when:
        def r = service.findAllInProjectByFullName(p, s, [:])

        then:
        r.results.size() == size
        r.count == count

        where:
        s           | size | count
        ''          | 2    | 2
        'not found' | 0    | 0
        'test'      | 2    | 2
    }

    void "findAllInProjectByFullName with null project returns empty list"() {
        when:
        def r = service.findAllInProjectByFullName(null, "string", [:]).results

        then:
        r.empty
        noExceptionThrown()
    }

    void "findAllInProjectByFullName returns only test runs in project"() {
        given:
        def p1 = new Project(name: "find me 123", code: "fm123").save()
        def p2 = new Project(name: "dont find me 123", code: "dfm12").save()
        def t = new AutomatedTest(fullName: "First test 123", project: p1).save()
        def t1 = new AutomatedTest(fullName: "Second test 123", project: p2).save(flush: true)

        when:
        def r = service.findAllInProjectByFullName(p1, "test", [:]).results

        then:
        r.size() == 1
        r.contains(t)
        !r.contains(t1)
    }

    void "get returns AutomatedTest"() {
        given:
        def p1 = new Project(name: "find me 123", code: "fm123").save()
        def t1 = new AutomatedTest(fullName: "Second test 123", project: p1).save(flush: true)

        when:
        def a = service.get(t1.id)

        then:
        a != null
    }

    void "get returns null when not found"() {
        when:
        def a = service.get(null)

        then:
        noExceptionThrown()
        a == null
    }

    void "countByProject returns number of tests in project"() {
        given:
        def p = new Project(name: "counting 123", code: "c123").save()
        new AutomatedTest(fullName: "counting 1", project: p).save()
        new AutomatedTest(fullName: "counting 2", project: p).save(flush: true)

        when:
        def c = service.countByProject(p)

        then:
        c == 2
    }

    void "countByProject returns 0 when project null"() {
        when:
        def c = service.countByProject(null)

        then:
        noExceptionThrown()
        c == 0
    }
}
