package com.everlution.test.step

import com.everlution.Step
import com.everlution.StepService
import com.everlution.TestCase
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class StepServiceSpec extends Specification implements ServiceUnitTest<StepService>, DataTest {

    def setupSpec() {
        mockDomain(Step)
    }

    private void setupData() {
        new Step(action: "action", result: "result").save()
        new Step(action: "action", result: "result").save()
        new Step(action: "action", result: "result").save(flush: true)
    }

    void "get with valid id returns instance"() {
        setupData()

        expect: "valid instance"
        service.get(1) instanceof Step
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "delete with valid id deletes instance"() {
        given:
        def s = new Step(action: "action", result: "result").save(flush: true)

        expect:
        service.get(s.id) != null

        when:
        service.delete(s.id)
        currentSession.flush()

        then:
        service.get(s.id) == null
    }

    void "save with valid object returns instance"() {
        given:
        def step = new Step(action: 'action', result: 'result')

        when:
        def saved = service.save(step)

        then:
        saved instanceof Step
    }

    void "save with invalid object throws validation exception"() {
        given:
        def step = new Step()

        when:
        service.save(step)

        then:
        thrown(ValidationException)
    }
}
