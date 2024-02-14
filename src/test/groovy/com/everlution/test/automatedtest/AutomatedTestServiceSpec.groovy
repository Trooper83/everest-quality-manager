package com.everlution.test.automatedtest

import com.everlution.AutomatedTest
import com.everlution.AutomatedTestService
import com.everlution.Project
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
}
