package com.everlution.test.service

import com.everlution.Step
import com.everlution.IStepService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class StepServiceSpec extends Specification {

    IStepService stepService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Step(...).save(flush: true, failOnError: true)
        //new Step(...).save(flush: true, failOnError: true)
        //Step step = new Step(...).save(flush: true, failOnError: true)
        //new Step(...).save(flush: true, failOnError: true)
        //new Step(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //step.id
    }

    void "test get"() {
        setupData()

        expect:
        stepService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Step> stepList = stepService.list(max: 2, offset: 2)

        then:
        stepList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        stepService.count() == 5
    }

    void "test delete"() {
        Long stepId = setupData()

        expect:
        stepService.count() == 5

        when:
        stepService.delete(stepId)
        sessionFactory.currentSession.flush()

        then:
        stepService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Step step = new Step()
        stepService.save(step)

        then:
        step.id != null
    }
}
