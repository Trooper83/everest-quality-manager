package com.everlution.test.step

import com.everlution.Step
import com.everlution.TestStepService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class StepServiceSpec extends Specification implements ServiceUnitTest<TestStepService>, DataTest {

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

    void "count returns number of steps"() {
        setupData()

        expect:
        service.count() == 3
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
}
