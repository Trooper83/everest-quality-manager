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

    def setup() {
    }

    def cleanup() {
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
}
