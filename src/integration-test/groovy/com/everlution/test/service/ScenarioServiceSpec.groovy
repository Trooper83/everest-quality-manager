package com.everlution.test.service

import com.everlution.Scenario
import com.everlution.ScenarioService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ScenarioServiceSpec extends Specification {

    ScenarioService scenarioService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Scenario(...).save(flush: true, failOnError: true)
        //new Scenario(...).save(flush: true, failOnError: true)
        //Scenario scenario = new Scenario(...).save(flush: true, failOnError: true)
        //new Scenario(...).save(flush: true, failOnError: true)
        //new Scenario(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //scenario.id
    }

    void "test get"() {
        setupData()

        expect:
        scenarioService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Scenario> scenarioList = scenarioService.list(max: 2, offset: 2)

        then:
        scenarioList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        scenarioService.count() == 5
    }

    void "test delete"() {
        Long scenarioId = setupData()

        expect:
        scenarioService.count() == 5

        when:
        scenarioService.delete(scenarioId)
        sessionFactory.currentSession.flush()

        then:
        scenarioService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Scenario scenario = new Scenario()
        scenarioService.save(scenario)

        then:
        scenario.id != null
    }
}
