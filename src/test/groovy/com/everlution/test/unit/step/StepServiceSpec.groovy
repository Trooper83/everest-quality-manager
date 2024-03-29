package com.everlution.test.unit.step

import com.everlution.domains.Person
import com.everlution.domains.Project
import com.everlution.domains.Step
import com.everlution.services.step.StepService
import com.everlution.domains.StepTemplate
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class StepServiceSpec extends Specification implements ServiceUnitTest<StepService>, DataTest {

    def setupSpec() {
        mockDomain(Step)
    }

    def setup() {
        service.save(new Step(act: "action", result: "result"))
    }

    void "get with valid id returns instance"() {
        expect: "valid instance"
        service.get(1) instanceof Step
    }

    void "read returns instance"() {
        expect: "valid instance"
        service.read(1) instanceof Step
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "save with valid step returns instance"() {
        given:
        def step = new Step(act: 'action')

        when:
        def saved = service.save(step)

        then:
        service.get(saved.id) instanceof Step
    }

    void "save with invalid scenario throws validation exception"() {
        given:
        def step = new Step()

        when:
        service.save(step)

        then:
        thrown(ValidationException)
    }

    void "findAllByTemplate returns steps"() {
        given:
        def person = new Person(email: 'testing@tesitng123.com', password: 'Testingin!3431').save()
        def project = new Project(name: 'testing proj 123', code: 't12').save()
        def t = new StepTemplate(name: 'name', act: 'act', result: 'result', person: person, project: project).save()
        def s = new Step(act: '123', template: t)
        service.save(s)

        when:
        def l = service.findAllByTemplate(t)

        then:
        l.size() == 1
        l.first() == s
    }
}
