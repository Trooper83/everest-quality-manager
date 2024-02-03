package com.everlution.test.testrun

import com.everlution.Project
import com.everlution.TestRun
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
}
